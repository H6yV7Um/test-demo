package com.ctrip.lpxie.basement.knight.structure;

import com.google.common.collect.Maps;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by lpxie on 2016/7/19.
 */
public class Request {
    private String requestPath = null;

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    private String method = null;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    private String protocol = null;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    private final Map headers = Maps.newHashMap();

    private final Map parameters = Maps.newHashMap();

    MappingData mappingData = new MappingData();

    private InputStream inputStream = null;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private OutputStream outputStream = null;

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Map getHeaders() {
        return headers;
    }

    public Map getParameters() {
        return parameters;
    }

    public MappingData getMappingData() {
        return mappingData;
    }
}
