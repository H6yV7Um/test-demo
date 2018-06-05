package com.ctrip.lpxie.basement.knight;

import com.ctrip.lpxie.basement.knight.structure.Request;
import com.ctrip.lpxie.basement.knight.structure.Response;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by lpxie on 2016/7/18.
 */
public class SocketWrapper {
    Request request = null;
    Response response = null;

    private String header = new String();
    private String body = new String();

    public SocketWrapper(InputStream inputStream,OutputStream outputStream) throws Exception{

        request = new Request();
        response = new Response();
        request.setInputStream(inputStream);
        response.setOutputStream(outputStream);
        ReaderStream.readStream(inputStream,this,request);
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }
}
