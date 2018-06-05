package com.ctrip.lpxie.storage.bStruct;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by lpxie on 2017/3/21.
 * 内存构建的磁盘块节点
 */
public class BlockNode {

    private volatile boolean isLeaf = false;//是否叶子节点

    private volatile long parentNodeOffset;
    //下一个节点的地址，即是下一个节点在文件中的偏移量 fixme
    private long nextNodeOffset;

    private volatile List<Long> keyNum = new ArrayList<>();//该盘块存储的数据

    private volatile Map<Long,IndexNode> keyMap = Maps.newHashMap();


    public boolean isLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public long getParentNodeOffset() {
        return parentNodeOffset;
    }

    public void setParentNodeOffset(long parentNodeOffset) {
        this.parentNodeOffset = parentNodeOffset;
    }

    public long getNextNodeOffset() {
        return nextNodeOffset;
    }

    public void setNextNodeOffset(long nextNodeOffset) {
        this.nextNodeOffset = nextNodeOffset;
    }

    public void setKeyNum(List<Long> keyNum) {
        this.keyNum = keyNum;
    }

    public List<Long> getKeyNum() {
        return keyNum;
    }

    public Map<Long, IndexNode> getKeyMap() {
        return keyMap;
    }
}
