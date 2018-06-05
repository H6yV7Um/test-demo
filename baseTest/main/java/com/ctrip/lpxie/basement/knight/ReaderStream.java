package com.ctrip.lpxie.basement.knight;

import com.ctrip.lpxie.basement.knight.structure.Request;

import java.io.*;

/**
 * Created by lpxie on 2016/7/18.
 */
public class ReaderStream {

    public static void readStream(InputStream inputStream,SocketWrapper socketWrapper,Request request) throws Exception{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        //1,header 以后是/r/n 2，body以后是/r/n，到此结束
        //header里面可以加上body的length，单位是字节
        try {
            String line = bufferedReader.readLine();
            StringBuilder headerStr = new StringBuilder();
            StringBuilder bodyStr = new StringBuilder();
            int contentLength = 0;
            if(!line.equals("")){
                String[] strings = line.split(" ");
                request.setMethod(strings[0]);
                request.setRequestPath(strings[1]);
                request.setProtocol(strings[2]);
            }
            while (line != null){
                if(!line.equals("") && line.contains(":")){
                    String[] strings = line.split(":");
                    request.getHeaders().put(strings[0],strings[1]);
                }
                headerStr.append(line).append("\r\n");
                if(line.contains("Content-Length"))
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                char[] chars = new char[4096];
                if(contentLength > 0)
                    chars = new char[contentLength];
                line = bufferedReader.readLine();
                if(line.equals("")){
                    //while (line != null || !line.equals(""))
                    {
                        bufferedReader.read(chars,0,contentLength);//fixme how to add char array capacity when source length is more bigger
                        bodyStr .append(new String(chars).trim());
                            break;
                    }
                }
            }

            socketWrapper.setBody(bodyStr.toString());
            socketWrapper.setHeader(headerStr.toString());
        } catch (Exception e) {
            e.printStackTrace();
//            throw new Exception("TIME OUT");
        }
    }
}
