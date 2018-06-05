package com.ctrip.lpxie.storage.bStruct;

import com.ctrip.lpxie.storage.fileOperation.BufferedRandomAccessFile;
import com.ctrip.lpxie.storage.fileOperation.MappedRandomAccessFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lpxie on 2017/3/21.
 */
public class BPlusTreeTest {
    BPlusTree bPlusTree;
    MappedRandomAccessFile file;
    int dataSize = 8;
    @Before
    public void init(){
        String fileName = this.getClass().getClassLoader().getResource("").getFile()+"table.btd";
        System.out.println(fileName);
        File tempFile = new File(fileName);
        if(tempFile.exists())
            tempFile.delete();
        try {
            //file = new RandomAccessFile(tempFile,"rw");
            file = new MappedRandomAccessFile(tempFile,"rw",0);
//            file = new BufferedRandomAccessFile(tempFile,"rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bPlusTree = new BPlusTree(file,dataSize);
    }

    /**
     * 没有性能提高...fixme
     */
    @Test
    public void parallelTest(){
        long start = System.currentTimeMillis();
        int ThreadNum = 4;
        final AtomicInteger count = new AtomicInteger(ThreadNum);
        for(int i = 0;i<ThreadNum;i++){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    testPutAndGet();
                    count.decrementAndGet();
                }
            };
            new Thread(runnable).start();
        }
        while (count.get() != 0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("all:"+(System.currentTimeMillis() - start)/1000);
    }

    String key = "hello";

    @Test
    public void readText(){
        String source = this.getClass().getClassLoader().getResource("").getFile()+"dump";
        System.out.println(source);
        File tempFile = new File(source);
        if(!tempFile.exists())
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        List<Long> keys = new ArrayList<>();

        try {
            FileReader reader = new FileReader(tempFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String strL = line.substring(line.indexOf("\"")+1,line.lastIndexOf("\""));
//                System.out.println(strL);
                keys.add(Long.parseLong(strL));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("read end");

        for(long key : keys){
            IndexNode data = new IndexNode();
            byte[] realData = new byte[dataSize];
            ByteBuffer buffer = ByteBuffer.wrap(realData);
            buffer.putLong(key);
            data.setRealData(buffer.array());
            bPlusTree.put(data, key);
        }
        bPlusTree.toString();
//        bPlusTree.get(2229110242920287883L);
        int notExist = 0;
        for(long key : keys){
            if(key == 2235295222101869377L || key == 2229110242920287883L){
                System.out.println();
            }
            byte[] result = bPlusTree.get(key);
            ByteBuffer buffer = ByteBuffer.wrap(result);
            long value = buffer.getLong();

            if(result.length<=0){
                notExist++;
            }
            System.out.println(key+" = "+value);
            bPlusTree.delete(key);
            bPlusTree.delete(key);
        }

        bPlusTree.toString();
    }

    @Test
    public void testToString(){

    }

    AtomicInteger index = new AtomicInteger(0);

    @Test
    public void testPutAndGet(){
        long start = System.currentTimeMillis();
        Random random = new Random();
        List<Long> keys = new ArrayList<>();//去重
//        for(int j = 0;j<10000;j++)
        for(int i = 0;i<500000;i++){
//            long key = Math.abs(random.nextLong());
            index.getAndIncrement();
            long key = index.get();
            keys.add(key);

            IndexNode data = new IndexNode();
            byte[] realData = new byte[dataSize];
            ByteBuffer buffer = ByteBuffer.wrap(realData);
            buffer.putLong(key);

            data.setRealData(buffer.array());
            bPlusTree.put(data, key);
            if(index.get()%10000 == 0)
            {
                System.out.println("current i :"+index.get());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("writeTime:"+((end-start)/1000));
        int notExist = 0;
        for(long key : keys){
            byte[] result = bPlusTree.get(key);
            if(result.length<=0){
                notExist++;
                bPlusTree.get(key);
            }
        }
        System.out.println("allNE:"+notExist);
        end = System.currentTimeMillis();
        System.out.println("allTime:"+((end-start)/1000));
    }

    @Test
    public void testGet(){
        key = "lpxie";
        for(int i = 0;i<3;i++){
            key += i;
            long indexNum = Math.abs(key.hashCode());
            byte[] result = bPlusTree.get(indexNum);
            String str = new String(result);
            System.out.println(str);
        }

        key = "hello";
        for(int i = 0;i<3;i++){
            key += i;
            long indexNum = Math.abs(key.hashCode());
            byte[] result = bPlusTree.get(indexNum);
            String str = new String(result);
            System.out.println(str);
        }
    }

    @Test
    public void testPut(){
        key = "lpxie";
        String value = "lpxie more welcome to come my bplustree ,hope you are happy";
        for(int i = 0;i<3;i++){
            key += i;
            IndexNode data = new IndexNode();
            byte[] realData = new byte[dataSize];
            ByteBuffer buffer = ByteBuffer.wrap(realData);
            buffer.put(value.getBytes(),0,dataSize);
            data.setRealData(buffer.array());
            long indexNum = Math.abs(key.hashCode());
            bPlusTree.put(data, indexNum);
        }
    }

    @Test
    public void testDelete(){
        key = "lpxie";
        for(int i = 0;i<3;i++){
            key += i;
            long indexNum = Math.abs(key.hashCode());

            byte[] result = bPlusTree.get(indexNum);
            String str = new String(result);
            System.out.println(str);

            bPlusTree.delete(indexNum);

            result = bPlusTree.get(indexNum);
            str = new String(result);
            System.out.println(str);
        }
    }

    @Test
    public void testBinarySearch(){
        List<Long> indexes = new ArrayList<>();
        indexes.add(15L);
        indexes.add(18L);
        indexes.add(20L);
        System.out.println(bPlusTree.binarySearch(indexes, 15));
    }


    @After
    public void destroy(){
        try {
            file.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
