package com.ctrip.lpxie.basement.knight.structure;

import com.ctrip.lpxie.basement.knight.SocketWrapper;

/**
 * Created by lpxie on 2016/7/19.
 */
public class HttpProcessor<S> implements Processor<S> {
    Request req;
    Response res;
    protected DefaultAdapter adapter;

    @Override
    public void doProcess(S o) {
        if(o instanceof SocketWrapper)
        {
            SocketWrapper socketWrapper = (SocketWrapper)o;
            req = new Request();
            res = new Response();
            parseHeader(socketWrapper.getHeader());
            //start to process req and res
            adapter.service(req,res);
        }
    }

    private void parseHeader(String headerStr){
        //spit by line
        String[] headers = headerStr.split("\r\n");
        for(String subHeader : headers){
            String[] subs = subHeader.split(":");
            String name = subs[0];
            String value = subs[1];
            req.getHeaders().put(name,value);
        }
    }

    //fixme contentType should be some constant or enum type
    private void parseBody(String contentType,String bodyStr){

    }
}
