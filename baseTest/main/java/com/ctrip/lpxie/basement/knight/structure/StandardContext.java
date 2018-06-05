package com.ctrip.lpxie.basement.knight.structure;

import com.ctrip.lpxie.basement.knight.servlet.FilterDef;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lpxie on 2016/7/19.
 */
public class StandardContext extends ContainerBase implements Context {

    private Map<String,String> servletMappings = new HashMap<String, String>();

    private HashMap<String, FilterDef> filterDefs =
            new HashMap<String, FilterDef>();

    public HashMap<String, FilterDef> getFilterDefs() {
        return filterDefs;
    }

    private Object applicationLifecycleListenersObjects[] =
            new Object[0];

    private String wrapperClassName = StandardWrapper.class.getName();

    public StandardContext(){
        super();
        pipeline.setBasic(new StandardContextValve());
    }

    @Override
    public Pipeline getPipeline() {
        return pipeline;
    }

    @Override
    protected void initInternal() {

    }

    @Override
    protected void startInternal() {

    }

    @Override
    protected void stopInternal() {

    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public void setPath(String path) {

    }

    @Override
    public String getWebappVersion() {
        return null;
    }

    @Override
    public String[] findWelcomeFiles() {
        return new String[0];
    }
}
