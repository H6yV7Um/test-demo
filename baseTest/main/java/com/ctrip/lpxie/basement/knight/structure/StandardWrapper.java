package com.ctrip.lpxie.basement.knight.structure;

import com.ctrip.lpxie.basement.knight.servlet.Servlet;

/**
 * Created by lpxie on 2016/7/19.
 */
public class StandardWrapper extends ContainerBase implements Wrapper {
    private Servlet servlet = null;

    public Servlet getServlet() {
        return servlet;
    }

    public void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }

    public StandardWrapper(){
        super();
        pipeline.setBasic(new StandardWrapperValve());
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
    public String getName() {
        return name;
    }
}
