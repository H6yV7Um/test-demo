package com.ctrip.lpxie.basement.knight.structure;

import com.google.common.collect.Maps;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by lpxie on 2016/7/19.
 */
public class Response {
    private final Map headers = Maps.newHashMap();

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
}
