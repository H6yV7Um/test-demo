package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/21.
 */
public class MapperListener extends LifecycleBase implements LifecycleListener,ContainerListener{

    private Connector connector = null;

    private Mapper mapper = null;

    public MapperListener(Mapper mapper, Connector connector) {
        this.mapper = mapper;
        this.connector = connector;
    }

    @Override
    protected void initInternal() {

    }

    @Override
    protected void startInternal() {
        Engine engine = (Engine) connector.getService().getContainer();
        addListeners(engine);

        Container[] conHosts = engine.findChildren();
        for (Container conHost : conHosts) {
            Host host = (Host) conHost;
            //if (!LifecycleState.NEW.equals(host.getState()))
            {
                registerHost(host);
            }
        }
    }

    @Override
    protected void stopInternal() {

    }

    private void addListeners(Container container) {
        container.addContainerListener(this);
        container.addLifecycleListener(this);
        for (Container child : container.findChildren()) {
            addListeners(child);
        }
    }

    private void registerContext(Context context) {

        String contextPath = context.getPath();
        if ("/".equals(contextPath)) {
            contextPath = "";
        }
        Container host = context.getParent();
        mapper.setContext(context);
        //javax.naming.Context resources = context.getResources();
        String[] welcomeFiles = context.findWelcomeFiles();
        //List<WrapperMappingInfo> wrappers = new ArrayList<WrapperMappingInfo>();

        for (Container container : context.findChildren()) {
            //prepareWrapperMappingInfo(context, (Wrapper) container, wrappers);
            registerWrapper((Wrapper)container);

        }

       /* mapper.addContextVersion(host.getName(), host, contextPath,
                context.getWebappVersion(), context, welcomeFiles, resources,
                wrappers);*/

    }

    private void registerHost(Host host) {

        String[] aliases = host.findAliases();
        //mapper.addHost(host.getName(), aliases, host);
        mapper.setHost(host);
        for (Container container : host.findChildren()) {
            //if (container.getState().isAvailable())
            {
                registerContext((Context) container);
            }
        }
    }

    private void registerWrapper(Wrapper wrapper) {
        mapper.setWrapper(wrapper);
        Context context = (Context) wrapper.getParent();
        String contextPath = context.getPath();
        if ("/".equals(contextPath)) {
            contextPath = "";
        }
        String version = context.getWebappVersion();
        String hostName = context.getParent().getName();

//        List<WrapperMappingInfo> wrappers = new ArrayList<WrapperMappingInfo>();
//        prepareWrapperMappingInfo(context, wrapper, wrappers);
//        mapper.addWrappers(hostName, contextPath, version, wrappers);

    }

    @Override
    public void lifecycleEvent(LifecycleEvent event) {

    }

    @Override
    public void containerEvent(ContainerEvent event) {

    }
}
