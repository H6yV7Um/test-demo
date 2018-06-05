package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/21.
 */
public class Mapper {

    /*public void map(MessageBytes host, MessageBytes uri, String version,
                    MappingData mappingData)
            throws Exception {

        if (host.isNull()) {
            host.getCharChunk().append(defaultHostName);
        }
        host.toChars();
        uri.toChars();
        internalMap(host.getCharChunk(), uri.getCharChunk(), version,
                mappingData);

    }*/
    protected Context context = null;

    protected Host host = null;

    protected Wrapper wrapper = null;

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Wrapper getWrapper() {
        return wrapper;
    }

    public void setWrapper(Wrapper wrapper) {
        this.wrapper = wrapper;
    }
}
