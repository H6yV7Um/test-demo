package com.ctrip.lpxie.storage.fileOperation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.ctrip.lpxie.storage.GlobalConstant.*;
/**
 * Created by lpxie on 2017/3/28.
 */
public class MappedRandomAccessFile extends RandomAccessFile {
    private MappedByteBuffer mappedByteBuffer;
    private int mappedSize = 1024*1024*1024;//10mb 1024*1024*1024*2  Integer.MAX_VALUE

    private long dataLength = 0;

    static Lock lock = new ReentrantLock();
    
    public MappedRandomAccessFile(String name, String mode,long dataLength) throws FileNotFoundException {
        super(name, mode);
        try {
            mappedByteBuffer = this.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, mappedSize);//2GB
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.dataLength = dataLength;
    }

    public MappedRandomAccessFile(File file, String mode,long dataLength) throws FileNotFoundException {
        super(file, mode);
        try {
            mappedByteBuffer = this.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, mappedSize);//2GB
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.dataLength = dataLength;
    }

    public int readMapped(byte b[], int off, int len){
        try{
            lock.lock();
    
            if(off + len <= mappedSize){
                mappedByteBuffer.position(off);
                mappedByteBuffer.get(b);
                return b.length;
            }else {
                return 0;
            }
        }finally {
            lock.unlock();
        }
    }

    public boolean writeMapped(byte b[],int off) throws IOException {
        try{
            lock.lock();
    
            if(off + b.length <= mappedSize){
                try {
                    mappedByteBuffer.position(off);
                    mappedByteBuffer.put(b);
                    if(off == dataLength)//通常off==dataLength
                        dataLength = off + blockSize;//当写入新的文件位置的时候就更新这个值,这个值永远表示最后一个写入字节的位置+1
                }catch (Exception exp){
                    exp.printStackTrace();
                }
                return true;
            }else {
                return false;
            }
        }finally {
            lock.unlock();
        }
    }

    public long limit(){
        return mappedByteBuffer.limit();
    }

    public long length() throws IOException{
        if(dataLength <= (mappedSize - blockSize))//最后一个做保留最后一块数据用
            return dataLength;
        else
            return super.length();
    }

    public int getMappedSize(){
        return mappedSize;
    }

    /**
     * 在MappedByteBuffer释放后再对它进行读操作的话就会引发jvm crash，在并发情况下很容易发生
     * 正在释放时另一个线程正开始读取，于是crash就发生了。所以为了系统稳定性释放前一般需要检
     * 查是否还有线程在读或写
     * @param mappedByteBuffer
     */
    public static void unmap(final MappedByteBuffer mappedByteBuffer) {
        try {
            if (mappedByteBuffer == null) {
                return;
            }
            mappedByteBuffer.force();
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                @SuppressWarnings("restriction")
                public Object run() {
                    try {
                        Method getCleanerMethod = mappedByteBuffer.getClass().getMethod("cleaner", new Class[0]);
                        getCleanerMethod.setAccessible(true);
                        sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(mappedByteBuffer, new Object[0]);
                        cleaner.clean();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("clean MappedByteBuffer completed");
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() throws IOException{
        unmap(mappedByteBuffer);
        super.close();
    }
}
