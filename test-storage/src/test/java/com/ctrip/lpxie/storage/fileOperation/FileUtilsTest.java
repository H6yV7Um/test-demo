package com.ctrip.lpxie.storage.fileOperation;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by lpxie on 2017/3/21.
 */
public class FileUtilsTest {
    MappedRandomAccessFile file;
    @Before
    public void init(){
        try {
            String fileName = this.getClass().getClassLoader().getResource("").getFile()+"testFile";
            System.out.println(fileName);
            File tempFile = new File(fileName);
            /*if(tempFile.exists())
                tempFile.delete();*/
            file = new MappedRandomAccessFile(tempFile,"rw",0);
//            file = new RandomAccessFile(tempFile,"rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRead(){
        long start = System.currentTimeMillis();
        String dataStr = "private static Logger logger = LoggerFactory.getLogger(SecOfflineSerializer.class)";
        for(int i = 0;i<1000;i++){
            byte[] data = dataStr.getBytes();
//            System.out.println("bytes length :" + data.length);
            FileUtils.write(file, i*data.length, data);
            byte[] readData = FileUtils.read(file, i*data.length);
            System.out.println(new String(readData));
        }
        System.out.println("time:"+(System.currentTimeMillis()-start));
    }

    @After
    public void destroy(){
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
