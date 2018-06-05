package com.ctrip.lpxie.storage.fileOperation;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.ctrip.lpxie.storage.GlobalConstant.*;
/**
 * Created by lpxie on 2017/3/21.
 * 用于操作文件的随机读取
 */
public class FileUtils {
    static byte[] nullByte = new byte[0];
    static byte[] result = new byte[blockSize];
    public static byte[] read(MappedRandomAccessFile file,long offset){
        try {
            if(file.length() < blockSize){
                return nullByte;
            }else {
                int count = 0;
                if(file instanceof MappedRandomAccessFile && (offset + blockSize) <= file.getMappedSize()){
                    count = file.readMapped(result, (int) offset, blockSize);
                }
                if(count != 0)
                    return result;
                assert offset <= file.length();
                file.seek(offset);
                file.read(result,0,blockSize);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nullByte;
    }

    /**
     *
     * @param file
     * @param offset 不能超过文件长度
     * @param bytes
     */
    public static void write(MappedRandomAccessFile file,long offset,byte[] bytes){
        try {
            boolean success = false;
            if(file instanceof MappedRandomAccessFile && (offset + bytes.length) <= file.getMappedSize()){
                success = file.writeMapped(bytes, (int) offset);
            }
            if(success)
                return;
            assert offset <= file.length();
            file.seek(offset);
            file.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
