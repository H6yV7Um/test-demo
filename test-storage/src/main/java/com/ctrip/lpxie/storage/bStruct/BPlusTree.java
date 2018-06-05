package com.ctrip.lpxie.storage.bStruct;

import com.ctrip.lpxie.storage.fileOperation.MappedRandomAccessFile;
import com.google.common.collect.Lists;

import static com.ctrip.lpxie.storage.fileOperation.FileUtils.*;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.ctrip.lpxie.storage.GlobalConstant.*;
/**
 * Created by lpxie on 2017/3/21.
 * 最主要的是实现 put delete 操作
 * 默认实现非聚集索引，会有插入，一定节点的操作
 * todo 只修改对应的行，而不是整个块，这样可以大大提高效率，同时缓存最近修改的块，而不是最开始的部分块，也能提供整体的效率
 * todo 并发情况 保证fileLength是正确的 也就是在新建节点的时候需要加锁
 */
public class BPlusTree{

    private final MappedRandomAccessFile file;

    private int maxIndexRecords;//用于分裂节点
    private int minIndexRecords;//用于合并节点

    private int maxDataRecords;//用于分裂节点
    private int minDataRecords;//用于合并节点

    private int dataSize = 8;//创建表的时候，必须指定数据行的长度

    private final BlockNode root;//根节点，程序启动的时候读入文件的第一个磁盘块，保存在内存中

    private ReadWriteLock lock = new ReentrantReadWriteLock();//保证在并发情况下put和delete对树的修改能够正确

    public BPlusTree(final MappedRandomAccessFile file,int dataSize){//创建表的时候，必须指定数据行的长度
        this.file = file;
        root = new BlockNode();
        loadRoot();
        root.setParentNodeOffset(-1);//根节点没有父节点
        this.dataSize = dataSize;
        maxIndexRecords = (blockSize-9)/(8+8);
        minIndexRecords = (maxIndexRecords-1)/2;

        maxDataRecords = (blockSize-17)/(8+dataSize);
        minDataRecords = (maxDataRecords-1)/2;
    }

    private boolean loadRoot(){
        assert file != null;
        assert root != null;
        byte[] blockData =read(file,0);//根节点从0开始
        buildNode(blockData, root);
        return false;
    }

    /**
     * 构建给定的一个字节数组数据为一个内存节点
     * @param data
     * @param node
     */
    private void buildNode(byte[] data,BlockNode node){
        assert data != null;
        assert node != null;
        if(data.length == 0){//第一次还未初始化
            return;
        }
        if(data.length != blockSize)//数据节点长度必须和定义的节点size一致，否则解析会出错
            return;
        //解析必须和定义的协议一致
        //定义第一个字节表示是否叶子节点，0表示否，1表示是,fixme 用一个额外的字节存储是否叶子节点有点不妥
        //case:
        //0:1+8+{8+8}*n    type+parentOffset            索引号(indexNum)+对应的offset
        //1:1+8+8+{8+x}*n  type+parentOffset+nextOffset 索引号(indexNum)+对应的data  其中x=8*2^n
        //case:
        //1,如果是非叶子节点，后面的8个字节表示偏移量，最后以"/r"结束一条记录，即该索引号对应的磁盘块的位置
        //2,如果是叶子节点，后面是数据,最后以"/r"结束一条记录，为了计算方便，暂且将数据的长度设为8*2^n
        //数据节点的长度，应该是在创建表的时候动态指定的，所以在前面需要通过块size计算出来，这样这里可以拿到长度 fixme
        ByteBuffer temp = ByteBuffer.wrap(data);
        byte type = temp.get();
        node.setIsLeaf(type == 1);
        node.setParentNodeOffset(temp.getLong());
        switch (type){
            case 0:
                buildNLeafNode(temp,node);
                break;
            case 1:
                long nextDataOffset = temp.getLong();
                node.setNextNodeOffset(nextDataOffset);
                buildLeafNode(temp, node);
                break;
            default:
                break;
        }
    }

    /**
     * //0:1+{8+8}*n
     * @param temp
     * @param node
     */
    private void buildNLeafNode(ByteBuffer temp,BlockNode node){
        while (temp.hasRemaining()){
            if(temp.remaining() < 8)
                break;//表示这个磁盘块内容满了，在结束的时候不足8个字节可以读了
            long indexNum = temp.getLong();
            if(indexNum <= 0){//表示到达了记录的最后，这时候后面的字节数据不用读了
                break;
            }
            IndexNode indexNode = new IndexNode();
            long blockOffset = temp.getLong();
            node.getKeyNum().add(indexNum);
            indexNode.setIndexOffset(blockOffset);
            node.getKeyMap().put(indexNum,indexNode);
        }
    }

    byte[] realData = new byte[dataSize];//不要每次都申请，很耗时
    /**
     * //1:1+8+{8+x}*n 其中x=8*2^n
     * @param temp
     * @param node
     */
    private void buildLeafNode(ByteBuffer temp,BlockNode node){
        while (temp.hasRemaining()){
            try {
                if(temp.remaining() < 8){
                    break;//到达了记录的最后,不足8个字节了
                }
                long indexNum = temp.getLong();
                IndexNode indexNode = new IndexNode();
                if(indexNum <= 0){//表示到达了记录的最后，这时候后面的字节数据不用读了
                    break;
                }
                temp.get(realData);
                node.getKeyNum().add(indexNum);
                indexNode.setRealData(realData);
                node.getKeyMap().put(indexNum,indexNode);
            }catch (Exception exp){
                exp.printStackTrace();
            }
        }
    }

    public  byte[] get(long indexNum){
        lock.writeLock().lock();
        try{
            return get(root,indexNum);
        }finally {
            lock.writeLock().unlock();
        }
    }
    byte[] nullByte = new byte[0];
    /**
     * 通常分两种情况：
     * 第一种是在非叶子节点找到，这时候需要进一步读取对应的磁盘块内容到内存
     * 第二种是在叶子节点找到，这时候直接返回结果，也就是递归结束的条件
     * @param node
     * @param indexNum
     */
    private byte[] get(BlockNode node,long indexNum){
        if(node.getKeyMap().containsKey(indexNum)){//在当前节点找到了需要的数据
            if(node.isLeaf()){
                return node.getKeyMap().get(indexNum).getRealData();
            }else {
                long nodeOffset = node.getKeyMap().get(indexNum).getIndexOffset();
                byte[] nodeByteData = read(file, nodeOffset);
                BlockNode newNode = new BlockNode();//构建一个新的内存节点
                buildNode(nodeByteData, newNode);
                return get(newNode, indexNum);
            }
        }else {
            if(node.isLeaf()){
                //没有找到需要的记录
                return nullByte;
            }else{
                long nodeIndex = binarySearch(node.getKeyNum(),indexNum);
                if(nodeIndex == -1){
                    //没有找到需要的记录
                    return nullByte;
                }
                long nodeOffset = node.getKeyMap().get(nodeIndex).getIndexOffset();
                byte[] nodeByteData = read(file, nodeOffset);
                BlockNode newNode = new BlockNode();//构建一个新的内存节点
                buildNode(nodeByteData, newNode);
                return get(newNode, indexNum);
            }
        }
    }

    /**
     * 每次从根节点开始
     * @param data
     * @param indexNum
     * @return
     */
    public boolean put(IndexNode data,long indexNum){
        lock.writeLock().lock();
        try{
            return put(root, data, indexNum, 0);
        }finally {
            lock.writeLock().unlock();
        }
    }
    /**
     * 如果数据节点以及存在，直接覆盖原来的值
     * 步骤如下；
     * 1，读取对应的磁盘块到内存，更新里面的数据
     * 2，判断节点的记录数量是否满足t<n<2t-1
     * 3, 如果不满足条件则调整磁盘块的录数量，分割与合并
     * 4，将数据写入对应的磁盘块，即在文件中对应的偏移位置
     * 5，结束
     * 将已经封装好的数据节点存入磁盘文件
     * 这里的data一定是数据节点，因为非数据节点是在变化的过程中动态产生的
     * @param data 里面的byte[]就是需要存储的数据，里面的indexOffset默认是空，因为数据节点没有这个值
     * @param indexNum 通过key的映射生产的数字
     * @return
     */
    private boolean put(BlockNode node,IndexNode data,long indexNum,long currentNodeOffset){
        if(node.getKeyMap().containsKey(indexNum)){//在当前节点找到了需要的数据
            if(node.isLeaf()){
                node.getKeyMap().put(indexNum, data);//直接更新原来的数据
                write(file,currentNodeOffset, packData(node));//写入文件对应的偏移位置
                return true;
            }else {
                long nodeOffset = node.getKeyMap().get(indexNum).getIndexOffset();
                byte[] nodeByteData = read(file, nodeOffset);
                BlockNode newNode = new BlockNode();//构建一个新的内存节点
                buildNode(nodeByteData, newNode);
                newNode.setParentNodeOffset(currentNodeOffset);
                //更新节点到文件
                write(file,nodeOffset, packData(newNode));
                return put(newNode, data, indexNum,nodeOffset);
            }
        }else {
                long nodeIndex = binarySearch(node.getKeyNum(), indexNum);
                if(nodeIndex == -1){//说明这是新的记录，需要插入到文件末尾然后判断条件是否满足，然后做出调整
                    //当前节点是找到的最下层的节点
                    if(node.isLeaf()){
                        //这个要复杂很多：
                        if(indexNum < node.getKeyNum().get(0)){//比最小的key还要小，这种情况需要新增一个节点
                            //移动当前元素到第一个位置
                            List<Long> tempKeyNum = Lists.newArrayList();
                            tempKeyNum.add(indexNum);
                            tempKeyNum.addAll(node.getKeyNum());
                            node.setKeyNum(tempKeyNum);
                            node.getKeyMap().put(indexNum, data);

                            //这时候需要判断记录数量是否满足t<n<2t-1
                            //如果不满足条件需要拆分
                            if(node.getKeyNum().size() > maxDataRecords){
                                //调整
                                splitNode(node,currentNodeOffset);
                            }else{
                                //直接写入文件
                                write(file, currentNodeOffset, packData(node));
                            }
                            //更新父节点最小的索引值为当前的索引值
                            long parentParentOffset = 0;
                            while (parentParentOffset != -1){
                                long currentParentOffset = node.getParentNodeOffset();
                                byte[] nodeByteData = read(file, currentParentOffset);
                                BlockNode parentNode = new BlockNode();//构建一个新的内存节点
                                buildNode(nodeByteData, parentNode);
                                parentNode.getKeyMap().put(indexNum, parentNode.getKeyMap().remove(parentNode.getKeyNum().get(0)));
                                parentNode.getKeyNum().set(0, indexNum);
                                write(file, currentParentOffset, packData(parentNode));//写入文件
                                node = parentNode;
                                parentParentOffset = parentNode.getParentNodeOffset();
                            }
                            root.getKeyMap().put(indexNum, root.getKeyMap().remove(root.getKeyNum().get(0)));
                            root.getKeyNum().set(0,indexNum);//更新当前内存根节点的值
                        }else{
                            //never happen
                        }
                    }else {
                        if(node.getKeyNum().size() <= 0){//初始情况
                            root.getKeyNum().add(indexNum);
                            IndexNode firstIndexNode = new IndexNode();
                            firstIndexNode.setIndexOffset(blockSize);
                            root.getKeyMap().put(indexNum, firstIndexNode);
                            write(file, currentNodeOffset, packData(root));//写入文件对应的偏移位置
                            BlockNode firstNode = new BlockNode();
                            firstNode.setIsLeaf(true);
                            firstNode.setParentNodeOffset(currentNodeOffset);
                            firstNode.getKeyNum().add(indexNum);
                            firstNode.getKeyMap().put(indexNum, data);
                            write(file, root.getKeyMap().get(indexNum).getIndexOffset(), packData(firstNode));//写入文件对应的偏移位置
                        }

                        try{
                            if(indexNum < node.getKeyNum().get(0)){
                                //通过当前最小的索引一直往下找，直到找到数据节点，然后进行调整
                                long nodeOffset = 0;//记录最后一个节点的偏移量
                                long parentOffset = currentNodeOffset;
                                while (!node.isLeaf()){
                                    long tmpIndexNum = node.getKeyNum().get(0);
                                    nodeOffset = node.getKeyMap().get(tmpIndexNum).getIndexOffset();
                                    byte[] nodeByteData = read(file, nodeOffset);
                                    BlockNode newNode = new BlockNode();//构建一个新的内存节点
                                    buildNode(nodeByteData, newNode);
                                    node = newNode;
                                    if(!node.isLeaf())
                                        parentOffset = nodeOffset;
                                }
                                node.setParentNodeOffset(parentOffset);
                                //更新节点到文件
                                write(file, nodeOffset, packData(node));
                                return put(node, data, indexNum,nodeOffset);
                            }else {
                                //never happen
                            }
                        }catch (Exception exp){
                            exp.printStackTrace();
                        }
                    }
                }else {
                    if(node.isLeaf()){
                        //到达叶子节点，不进行往下的查找，在当前节点中插入当前元素
                        //将这个数据添加到当前节点的中，进行keyNum的重排序,
                        //这时候需要判断记录数量是否满足t<n<2t-1
                        //如果不满足条件需要拆分
                        long adjoinNum = binarySearch(node.getKeyNum(),indexNum);
                        if(adjoinNum != indexNum){//不是重复键 才更新keyNum，否则只更新keyMap
                            List<Long> tempKeyNum = Lists.newArrayList();//fixme 相同的indexNum在这里会重复
                            for(long tmp : node.getKeyNum()){
                                tempKeyNum.add(tmp);
                                if(tmp == adjoinNum)
                                    tempKeyNum.add(indexNum);//插入indexNum到列表中
                            }
                            node.setKeyNum(tempKeyNum);
                        }
                        node.getKeyMap().put(indexNum,data);

                        if(node.getKeyNum().size() > maxDataRecords){
                            //调整
                            splitNode(node,currentNodeOffset);
                        }else {
                            //直接写入文件
                            write(file, currentNodeOffset, packData(node));
                        }
                    }else {
                        long nodeOffset = node.getKeyMap().get(nodeIndex).getIndexOffset();
                        byte[] nodeByteData = read(file, nodeOffset);
                        BlockNode newNode = new BlockNode();//构建一个新的内存节点
                        buildNode(nodeByteData,newNode);
                        newNode.setParentNodeOffset(currentNodeOffset);
                        //更新节点到文件
                        write(file, nodeOffset, packData(newNode));
                        return put(newNode, data, indexNum, nodeOffset);
                    }
                }
        }
        return true;
    }

    private void splitNode(BlockNode node,long currentNodeOffset){
        int midIndex = (node.getKeyNum().size() + 1) / 2;
        long midKeyNum = node.getKeyNum().get(midIndex);
        List<Long> keyNum = node.getKeyNum();
        List<Long> leftList = new ArrayList<>();
        leftList.addAll(keyNum.subList(0, midIndex));
        List<Long> rightList = new ArrayList<>();
        rightList.addAll(keyNum.subList(midIndex, keyNum.size()));
        if(node.getParentNodeOffset() == -1){//这个是根节点，新建一个节点作为根节点，同时这个节点存储在位置0，根节点需要保存在第一个位置
            BlockNode leftNode = new BlockNode();
            leftNode.setKeyNum(leftList);
            leftNode.setParentNodeOffset(0);//指向根节点
            leftNode.setIsLeaf(node.isLeaf());

            BlockNode rightNode = new BlockNode();
            rightNode.setParentNodeOffset(0);
            rightNode.setKeyNum(rightList);
            rightNode.setIsLeaf(node.isLeaf());

            for(long indexNum : rightList){
                IndexNode tmpRightIndexNode = node.getKeyMap().remove(indexNum);
                rightNode.getKeyMap().put(indexNum,tmpRightIndexNode);
            }
            IndexNode rightNodeIndex = new IndexNode();
            try {
                //创建父IndexNode
                long rightNodeOffset = file.length();
                rightNodeIndex.setIndexOffset(rightNodeOffset);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //把新的右边节点写入文件
            write(file, rightNodeIndex.getIndexOffset(), packData(rightNode));//写数据到入文件对应的偏移位置，这个新的数据，添加到文件末尾

            leftNode.getKeyMap().putAll(node.getKeyMap());

            BlockNode newRootNode = new BlockNode();
            newRootNode.setParentNodeOffset(-1);

            //重置root的值
            root.getKeyNum().clear();
            root.getKeyMap().clear();

            newRootNode.getKeyNum().add(leftNode.getKeyNum().get(0));
            IndexNode leftNodeIndex = new IndexNode();
            try {
                //创建父IndexNode
                long leftNodeOffset = file.length();
                leftNodeIndex.setIndexOffset(leftNodeOffset);
            } catch (IOException e) {
                e.printStackTrace();
            }
            newRootNode.getKeyMap().put(leftNode.getKeyNum().get(0), leftNodeIndex);
            newRootNode.getKeyNum().add(rightNode.getKeyNum().get(0));
            newRootNode.getKeyMap().put(rightNode.getKeyNum().get(0), rightNodeIndex);
            //跟新根节点内存映射
            root.setKeyNum(newRootNode.getKeyNum());
            root.getKeyMap().putAll(newRootNode.getKeyMap());
            //把左节点写入文件
            write(file, leftNodeIndex.getIndexOffset(), packData(leftNode));//写入数据到文件对应的偏移位置，这个是老数据，还是写的原来的位置
            //把根节点写入文件
            write(file,currentNodeOffset, packData(newRootNode));//写入数据到文件对应的偏移位置，这个是老数据，还是写的原来的位置
        }else {
            BlockNode rightNode = new BlockNode();
            rightNode.setParentNodeOffset(node.getParentNodeOffset());
            rightNode.setKeyNum(rightList);
            rightNode.setIsLeaf(node.isLeaf());
            for(long indexNum : rightList){
                IndexNode tmpRightIndexNode = node.getKeyMap().remove(indexNum);
                rightNode.getKeyMap().put(indexNum,tmpRightIndexNode);
            }
            IndexNode rightNodeIndex = new IndexNode();

            try {
                //创建父IndexNode
                long rightNodeOffset = file.length();
                rightNodeIndex.setIndexOffset(rightNodeOffset);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //把新的右边节点写入文件
            write(file, rightNodeIndex.getIndexOffset(), packData(rightNode));//写数据到入文件对应的偏移位置，这个新的数据，添加到文件末尾

            if(!rightNode.isLeaf()){//更新索引节点的子节点的父节点的parentOffset值
                    for(Map.Entry<Long,IndexNode> item : rightNode.getKeyMap().entrySet()){
                        byte[] childNodeByteData = read(file, item.getValue().getIndexOffset());
                        BlockNode childNode = new BlockNode(); //构建一个新的内存节点
                        buildNode(childNodeByteData, childNode);
                        childNode.setParentNodeOffset(rightNodeIndex.getIndexOffset());//更新parentOffset的值为leftOffset
                        write(file, item.getValue().getIndexOffset(), packData(childNode));
                    }
            }

            node.setKeyNum(leftList);
            //非根节点直接把原来的节点写入文件
            write(file, currentNodeOffset, packData(node));//写入数据到文件对应的偏移位置，这个是老数据，还是写的原来的位置
            //处理父节点
            byte[] parentNodeByte = read(file,node.getParentNodeOffset());
            BlockNode parentNode = new BlockNode();
            buildNode(parentNodeByte,parentNode);

            long adjoinNum = binarySearch(parentNode.getKeyNum(),midKeyNum);
            if(adjoinNum != midKeyNum){//不是重复键 才更新keyNum，否则只更新keyMap
                List<Long> tempKeyNum = Lists.newArrayList();
                for(long tmp : parentNode.getKeyNum()){
                    tempKeyNum.add(tmp);
                    if(tmp == adjoinNum)
                        tempKeyNum.add(midKeyNum);//插入indexNum到列表中
                }
                parentNode.setKeyNum(tempKeyNum);
            }
            parentNode.getKeyMap().put(midKeyNum, rightNodeIndex);
            if(parentNode.getKeyNum().size() > maxIndexRecords){
              splitNode(parentNode,node.getParentNodeOffset());
            }
            else{
                //如果parentNode是根节点，把当前的内存索引更新
                if(parentNode.getParentNodeOffset() == -1){
                    root.setKeyNum(parentNode.getKeyNum());
                    root.getKeyMap().clear();
                    root.getKeyMap().putAll(parentNode.getKeyMap());
                }
                //把父节点写入文件
                write(file, node.getParentNodeOffset(), packData(parentNode));//写入数据到文件对应的偏移位置，这个是老数据，还是写的原来的位置
            }
        }
    }

    /**
     * 构建一个磁盘数据块
     * 同样风两种情况
     * @param node
     */
    private byte[] packData(BlockNode node){
        if(node.isLeaf()) {
            return packLeafData(node);
        }else {
            return packNLeafData(node);
        }
    }

    //fixme 这个要保证线程安全
    byte[] zeroBytes = new byte[blockSize];//0数据 用于防止脏数据
    ByteBuffer buffer = ByteBuffer.allocate(blockSize);//fixme 不要每次都新申请空间
    private byte[] packNLeafData(BlockNode node){
        buffer.clear();
        buffer.flip();
        buffer.limit(blockSize);
        byte type = 0;
        buffer.put(type);
        buffer.putLong(node.getParentNodeOffset());
        for(long indexNum : node.getKeyNum()){
            buffer.putLong(indexNum);
            try {
                buffer.putLong(node.getKeyMap().get(indexNum).getIndexOffset());
            }catch (Exception exp){
                exp.printStackTrace();
            }
        }
        while (buffer.hasRemaining())
            buffer.put((byte)0);
        return buffer.array();
    }

    private byte[] packLeafData(BlockNode node){
        /*buffer.clear();
        buffer.put(zeroBytes);
        buffer.flip();*/
        buffer.clear();
        buffer.flip();
        buffer.limit(blockSize);
        byte type = 1;
        buffer.put(type);
        buffer.putLong(node.getParentNodeOffset());
        buffer.putLong(node.getNextNodeOffset());
        for(long indexNum : node.getKeyNum()){
            buffer.putLong(indexNum);
            try {
                buffer.put(node.getKeyMap().get(indexNum).getRealData());
            }catch (Exception exp){
                System.out.println("buffer"+"data length:"+ node.getKeyMap().get(indexNum).getRealData().length);
                exp.printStackTrace();
            }
        }
        while (buffer.hasRemaining())
            buffer.put((byte)0);
        return buffer.array();
    }
    /**
     * 对已经排序的数组进行二分查找：
     * 1，如果找到，直接返回；
     * 2，如果没有找到，返回列表里面小于当前值最近的值
     * 3, 如果比最小的值还小，返回-1
     * @param keyNum
     * @return
     */
    public long binarySearch(List<Long> keyNum,long indexNum){
        if(keyNum.size()<=0)
            return -1;
        int mid = keyNum.size()/2;
        long midValue = keyNum.get(mid);
        if(indexNum < midValue){
            if(keyNum.size()<=1)//midValue >= indexNum
                return -1;
             return binarySearch(keyNum.subList(0,mid),indexNum);
        }else if (indexNum > midValue){
            if(keyNum.size()<=1)//midValue >= indexNum
                return keyNum.get(0);
            return binarySearch(keyNum.subList(mid,keyNum.size()),indexNum);
        }else {
            return midValue;
        }
    }
    /**
     * 将已经通过key生成的索引节点编号的数据删除 TODO
     * @param indexNum
     * @return
     */
    public boolean delete(long indexNum){
        lock.writeLock().lock();
        try {
            return delete(root, indexNum, 0);
        }finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 从根节点开始查找需要删除的索引：
     * 1，没有找到，没有这个索引，直接返回
     * 2，找到了：
     * 2.1，在当前节点（数据|叶子 节点）删除这个节点
     * 2.2，判断这个节点是是否是当前节点的第一个节点：
     * 2.2.1，是头节点，更新第二个元素为头节点，更新父节点索引编号为新的这个节点编号，地址不变，依次往上判断，直到到达根节点
     * 2.2.2，不是头节点，更新父节点的索引值，直接返回
     * 2.3，判断当前节点的索引数量是否满足t<n<2t-1 keyNum.size>minDataSize
     * 2.3.1,满足，结束返回
     * 2.3.2，不满足，优先判断左节点（如果有，没有就判断右节点）是否满足减去一个值后，数量>minDataSize
     * 2.3.2.1,邻节点满足，借一个元素到当前节点，更新当前节点的值，更新父节点的值
     * 2.3.2.2，邻节点借走后不满足，与邻节点合并,保持靠左边原则，更新父节点的值，这时候父节点也少了一个节点，同样判断父节点是否满足，一次直到根节点，结束
     * @param node
     * @param indexNum
     * @return
     */
    private boolean delete(BlockNode node,long indexNum,long currentNodeOffset){
        if(node.getKeyMap().containsKey(indexNum)){
            if(node.isLeaf()){
                long firstIndexNum = node.getKeyNum().get(0);
                node.getKeyMap().remove(indexNum);//2.1
                node.getKeyNum().remove(indexNum);
                write(file, currentNodeOffset, packData(node));//写入文件对应的偏移位置
                if(node.getKeyNum().size() <= 0){//没有节点可以删除了
                    long parentOffset = node.getParentNodeOffset();
                    byte[] parentNodeByteData = read(file, parentOffset);
                    BlockNode parentNode = new BlockNode();//构建一个新的内存节点
                    buildNode(parentNodeByteData, parentNode);
                    parentNode.getKeyNum().remove(indexNum);
                    parentNode.getKeyMap().remove(indexNum);
                    write(file, parentOffset, packData(parentNode));//写入文件对应的偏移位置
                    if(parentNode.getParentNodeOffset() == -1){//根节点 一定是根节点
                        root.setKeyNum(parentNode.getKeyNum());
                        root.getKeyMap().clear();
                        root.getKeyMap().putAll(parentNode.getKeyMap());
                    }
                    return true;
                }
                if(firstIndexNum == indexNum){//2.2.1
                    long parentOffset = node.getParentNodeOffset();
                    long initNewFirstIndexNum = node.getKeyNum().get(0);//也就是原来列表里面第二个元素

                    while (parentOffset != -1){//不是根节点

                        byte[] parentNodeByteData = read(file, parentOffset);
                        BlockNode parentNode = new BlockNode();//构建一个新的内存节点
                        buildNode(parentNodeByteData, parentNode);
                        int parentIndex = parentNode.getKeyNum().indexOf(indexNum);
                        long initNewFirstParentIndexNum =  parentNode.getKeyNum().get(0);//保留原来父节点的第一个元素
                        if(parentIndex != -1){//父节点包含这个索引
                            parentNode.getKeyNum().set(parentIndex,initNewFirstIndexNum);
                            parentNode.getKeyMap().put(initNewFirstIndexNum, parentNode.getKeyMap().remove(indexNum));
                            write(file, parentOffset, packData(parentNode));//写入文件对应的偏移位置
                            if(parentNode.getParentNodeOffset() == -1){//更新内存消息
                                root.setKeyNum(parentNode.getKeyNum());
                                root.getKeyMap().clear();
                                root.getKeyMap().putAll(parentNode.getKeyMap());
                            }
                        }
                        parentOffset = parentNode.getParentNodeOffset();
                        if(initNewFirstParentIndexNum == indexNum){
                            continue;//继续更新上级节点的indexNum这个索引值
                        }else{
                            break;//当前节点里面的第一个索引值不是indexNum了，这时候不需要再往上更新了
                        }
                    }
                }else{//2.2.2
                    //nothing to do
                }
                //2.3
                if(node.getKeyNum().size() >= minDataRecords){//2.3.1
                    return true;
                }else{//2.3.2
                    //2.3.2.1
                    while (currentNodeOffset != 0){//直到根节点
                        long parentOffset = node.getParentNodeOffset();
                        byte[] parentNodeByteData = read(file, parentOffset);
                        BlockNode parentNode = new BlockNode();//构建一个新的内存节点
                        buildNode(parentNodeByteData, parentNode);

                        int currentIndex =  parentNode.getKeyNum().indexOf(node.getKeyNum().get(0));//当前节点的第一个indexNum在父节点的位置 fixme 这里可以这样做 因为前面已经把父节点的索引值都更新了，这时候去第一个元素为索引值逻辑是正确的
                        int leftIndex = currentIndex -1;
                        int rightIndex = currentIndex + 1;
                        if(currentIndex == 0 && parentNode.getKeyNum().size() == 1){//当前节点没有左节点和右节点，那么父节点有且只有一个节点,1,如果当前节点是非叶子节点，直接把父节点删除，把当前节点作为父节点;2如果是叶子节点，直接返回
                            if(!node.isLeaf()){
                                node.setParentNodeOffset(-1);//作为父节点
                                write(file, 0, packData(node));
                                //更新内存
                                root.setKeyNum(node.getKeyNum());
                                root.getKeyMap().clear();
                                root.getKeyMap().putAll(node.getKeyMap());

                                for(Map.Entry<Long,IndexNode> item : node.getKeyMap().entrySet()){
                                    byte[] childNodeByteData = read(file, item.getValue().getIndexOffset());
                                    BlockNode childNode = new BlockNode(); //构建一个新的内存节点
                                    buildNode(childNodeByteData, childNode);
                                    childNode.setParentNodeOffset(0);//更新parentOffset的值为leftOffset
                                    write(file, item.getValue().getIndexOffset(), packData(childNode));
                                }
                                return true;//到达了根节点 直接返回
                            }else
                                return true;
                            //从父节点借
                            //1，如果如节点也只有
                        }
                        /*if(rightIndex >= parentNode.getKeyNum().size() || currentIndex < 0){//fixme 右节点游标超过了最大边界，或者当前列表不包含子节点的的索引值
                            //nothing to do
                            return true;
                        }*/
                        if(leftIndex >= 0){//有左节点
                            long leftIndexNum = parentNode.getKeyNum().get(leftIndex);
                            long leftOffset = parentNode.getKeyMap().get(leftIndexNum).getIndexOffset();
                            byte[] leftNodeByteData = read(file,leftOffset);
                            BlockNode leftNode = new BlockNode();//构建一个新的内存节点
                            buildNode(leftNodeByteData, leftNode);

                            if((!leftNode.isLeaf() && ((leftNode.getKeyNum().size() - 1 >= minIndexRecords))) || ((leftNode.isLeaf()) && (leftNode.getKeyNum().size() - 1 >= minDataRecords))){//可以从左节点借
                                //左节点借最右边的索引值
                                long donateNum = leftNode.getKeyNum().remove(leftNode.getKeyNum().size()-1);
                                IndexNode donateIndex = leftNode.getKeyMap().remove(donateNum);
                                write(file,leftOffset,packData(leftNode));
                                //更新捐赠节点的子节点的parentOffset
                                long childOffset = donateIndex.getIndexOffset();
                                if(childOffset > 0){//表示子节点是索引节点
                                    byte[] childNodeByteData = read(file, childOffset);
                                    BlockNode childNode = new BlockNode(); //构建一个新的内存节点
                                    buildNode(childNodeByteData, childNode);
                                    childNode.setParentNodeOffset(currentNodeOffset);//子节点更新parentOffset的值为当前节点的值 fixme currentNodeOffset动态更新
                                    write(file, childOffset, packData(childNode));
                                }else {
                                    //这个是叶子节点没有子节点了，不用调整
                                    //nothing to do
                                }

                                //父节点删除老的索引值
                                parentNode.getKeyMap().remove(node.getKeyNum().get(0));

                                node.getKeyNum().add(0, donateNum);//添加到第一个位置
                                node.getKeyMap().put(donateNum, donateIndex);
                                write(file, currentNodeOffset, packData(node));

                                parentNode.getKeyNum().set(currentIndex, donateNum);

                                IndexNode newIndex = new IndexNode();
                                newIndex.setIndexOffset(currentNodeOffset);
                                parentNode.getKeyMap().put(donateNum, newIndex);

                                write(file, parentOffset, packData(parentNode));

                                if(parentNode.getParentNodeOffset() == -1){//更新内存消息
                                    root.setKeyNum(parentNode.getKeyNum());
                                    root.getKeyMap().clear();
                                    root.getKeyMap().putAll(parentNode.getKeyMap());
                                }
                            } else {//左节点不能借，与左节点合并
                                leftNode.getKeyNum().addAll(node.getKeyNum());
                                leftNode.getKeyMap().putAll(node.getKeyMap());
                                write(file, leftOffset, packData(leftNode));

                                parentNode.getKeyNum().remove(node.getKeyNum().get(0));//父节点里面删除子节点的索引值
                                parentNode.getKeyMap().remove(node.getKeyNum().get(0));
                                write(file, parentOffset, packData(parentNode));
                                if(parentNode.getParentNodeOffset() == -1){//更新内存消息
                                    root.setKeyNum(parentNode.getKeyNum());
                                    root.getKeyMap().clear();
                                    root.getKeyMap().putAll(parentNode.getKeyMap());
                                }
                                //更新被合并节点的子节点的parentOffset为新的当前节点的offset
                                if (!node.isLeaf()){//fixme 只更新当前节点下的第一层子节点的parentOffset
                                    for(Map.Entry<Long,IndexNode> item : node.getKeyMap().entrySet()){
                                        byte[] childNodeByteData = read(file, item.getValue().getIndexOffset());
                                        BlockNode childNode = new BlockNode(); //构建一个新的内存节点
                                        buildNode(childNodeByteData, childNode);
                                        childNode.setParentNodeOffset(leftOffset);//更新parentOffset的值为leftOffset
                                        write(file, item.getValue().getIndexOffset(), packData(childNode));
                                    }
                                }
                            }
                        }else if(rightIndex > 0){//没有左节点，向右节点借
                            long rightIndexNum = parentNode.getKeyNum().get(rightIndex);
                            long rightOffset = parentNode.getKeyMap().get(rightIndexNum).getIndexOffset();
                            byte[] rightNodeByteData = read(file,rightOffset);
                            BlockNode rightNode = new BlockNode();//构建一个新的内存节点
                            buildNode(rightNodeByteData, rightNode);

                            if((!rightNode.isLeaf() && (rightNode.getKeyNum().size() - 1 >= minIndexRecords)) || ((rightNode.isLeaf()) && (rightNode.getKeyNum().size() - 1 >= minDataRecords))){//可以从右节点借
                                //右节点借最左边的索引值
                                long donateNum = rightNode.getKeyNum().remove(0);
                                IndexNode donateIndex = rightNode.getKeyMap().remove(donateNum);

                                write(file,rightOffset,packData(rightNode));
                                //更新捐赠节点的子节点的parentOffset
                                long childOffset = donateIndex.getIndexOffset();
                                if(childOffset > 0){//非叶子(数据)节点
                                    byte[] childNodeByteData = read(file, childOffset);
                                    BlockNode childNode = new BlockNode(); //构建一个新的内存节点
                                    buildNode(childNodeByteData, childNode);
                                    childNode.setParentNodeOffset(currentNodeOffset);//更新parentOffset的值为当前节点的值 fixme currentNodeOffset动态更新
                                    write(file, childOffset, packData(childNode));

                                }else {
                                    //这个是叶子节点，不用调整
                                    //nothing to do
                                }

                                //父节点删除老的索引值
                                parentNode.getKeyMap().remove(donateNum);

                                node.getKeyNum().add(donateNum);//添加到最后一个位置
                                node.getKeyMap().put(donateNum, donateIndex);
                                write(file, currentNodeOffset, packData(node));
                                parentNode.getKeyNum().set(rightIndex, rightNode.getKeyNum().get(0));//原来的第二个位置元素
                                IndexNode newIndex = new IndexNode();
                                newIndex.setIndexOffset(rightOffset);
                                parentNode.getKeyMap().put(rightNode.getKeyNum().get(0), newIndex);
                                write(file, parentOffset, packData(parentNode));
                                if(parentNode.getParentNodeOffset() == -1){//更新内存消息
                                    root.setKeyNum(parentNode.getKeyNum());
                                    root.getKeyMap().clear();
                                    root.getKeyMap().putAll(parentNode.getKeyMap());
                                }
                            }else {//右节点不能借，与右节点合并
                                node.getKeyNum().addAll(rightNode.getKeyNum());
                                node.getKeyMap().putAll(rightNode.getKeyMap());
                                write(file, currentNodeOffset, packData(node));
                                try {
                                    parentNode.getKeyNum().remove(rightNode.getKeyNum().get(0));//父节点里面删除子节点的索引值
                                    parentNode.getKeyMap().remove(rightNode.getKeyNum().get(0));
                                }catch (Exception exp){
                                    exp.printStackTrace();
                                }
                                write(file, parentOffset, packData(parentNode));
                                if(parentNode.getParentNodeOffset() == -1){//更新内存消息
                                    root.setKeyNum(parentNode.getKeyNum());
                                    root.getKeyMap().clear();
                                    root.getKeyMap().putAll(parentNode.getKeyMap());
                                }
                                //更新被合并节点的子节点的parentOffset为新的当前节点的offset
                                if (!node.isLeaf()){//fixme 只更新当前节点下的第一层子节点的parentOffset
                                    for(Map.Entry<Long,IndexNode> item : node.getKeyMap().entrySet()){
                                        byte[] childNodeByteData = read(file, item.getValue().getIndexOffset());
                                        BlockNode childNode = new BlockNode(); //构建一个新的内存节点
                                        buildNode(childNodeByteData, childNode);
                                        childNode.setParentNodeOffset(currentNodeOffset);//更新parentOffset的值为currentNodeOffset
                                        write(file, item.getValue().getIndexOffset(), packData(childNode));
                                    }
                                }
                            }
                        }else {
                            System.out.println("error");
                        }
                        if(parentNode.getKeyNum().size() >= minIndexRecords)//在当前节点结束
                            break;
                        node = parentNode;//fixme 更新父节点为当前节点，进行下次迭代
                        currentNodeOffset = parentOffset;//fixme currentNodeOffset动态更新
                    }
                }
            }else {//非叶子节点找到了这个元素,则继续递归向下找，直到到达叶子节点 fixme 这种情况只有indexNum在root里面存在或者经过 几次递归后在某个索引节点
                while (!node.isLeaf()){
                    try {
                        long childOffset = node.getKeyMap().get(indexNum).getIndexOffset();
                        byte[] childNodeByteData = read(file, childOffset);
                        BlockNode childNode = new BlockNode(); //构建一个新的内存节点
                        buildNode(childNodeByteData, childNode);
                        node = childNode;
                        currentNodeOffset = childOffset;//必须更新这个地址值
                    }catch (Exception exp){
                        exp.printStackTrace();
                    }
                }
                delete(node,indexNum,currentNodeOffset);
            }
        }else{//当前节点没有匹配的节点，查找邻节点
            if(node.isLeaf()){//如果到达了叶子节点也没有找到则直接返回
                return true;
            }else {
                while (!node.isLeaf()){
                    long adjoinNum = binarySearch(node.getKeyNum(),indexNum);
                    if(adjoinNum < 0){//没有找到
                        return true;
                    }else {
                        long childOffset = node.getKeyMap().get(adjoinNum).getIndexOffset();
                        byte[] childNodeByteData = read(file, childOffset);
                        BlockNode childNode = new BlockNode(); //构建一个新的内存节点
                        buildNode(childNodeByteData, childNode);
                        node = childNode;
                        currentNodeOffset = childOffset;//必须更新这个地址值
                    }
                    if(node.isLeaf() && node.getKeyNum().size() < minDataRecords){
                        System.out.println("");
                    }
                }
                delete(node,indexNum,currentNodeOffset);
            }
        }
        return true;
    }

    public void sync(){
        try {
            file.getFD().sync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString(){
        return "";
       /* HashMap<String,Set<String>> record = new HashMap<>();
        StringBuilder builder = new StringBuilder("Root\r\n");
        findNode(0,root,builder,0,record);
        System.out.println(builder.toString());
        return builder.toString();*//* HashMap<String,Set<String>> record = new HashMap<>();
        StringBuilder builder = new StringBuilder("Root\r\n");
        findNode(0,root,builder,0,record);
        System.out.println(builder.toString());
        return builder.toString();*/
    }

    private StringBuilder findNode(long currentOffset,BlockNode node,StringBuilder builder,int level,HashMap<String,Set<String>> record){
        builder.append("level:"+level+"\n");
        level++;
        if(!record.containsKey(level+" "+currentOffset+"")){
            record.put(level+" "+currentOffset+"",new HashSet<String>());
        }
        for(long key : node.getKeyNum()){
            BlockNode childNode = new BlockNode();
            long childOffset = node.getKeyMap().get(key).getIndexOffset();
            if(childOffset == 0){
                record.remove(level + " "+currentOffset+"");
                if(!record.containsKey(currentOffset+"")){
                    record.put(currentOffset+"",new HashSet<String>());
                }
                record.get(currentOffset+"").add(key+"");
                builder.append("data:"+key+"\n");
                continue;
            }
            byte[] childByte = read(file, childOffset);
            buildNode(childByte, childNode);
            builder.append("offset:" + currentOffset + "\n\r");

            record.get(level+" "+currentOffset+"").add(childOffset + "");
            findNode(childOffset, childNode, builder, level, record);
        }
        return builder;
    }
}
