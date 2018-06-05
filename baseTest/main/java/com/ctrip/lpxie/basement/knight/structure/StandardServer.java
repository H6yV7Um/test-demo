package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/19.
 */
public class StandardServer extends LifecycleBase implements Server{
    private Service services[] = new Service[0];

    public StandardServer(){
        super();//it is must

    }
    /**
     * Add a new Service to the set of defined Services.
     *
     * @param service The Service to be added
     */
    @Override
    public void addService(Service service) {

        service.setServer(this);

        synchronized (services) {
            Service results[] = new Service[services.length + 1];
            System.arraycopy(services, 0, results, 0, services.length);
            results[services.length] = service;
            services = results;
        }

    }

    @Override
    public Service[] findServices() {
        return services;
    }

    @Override
    protected void initInternal() {
        System.out.println("StandardServer initInternal");
        // Initialize our defined Services
        for (int i = 0; i < services.length; i++) {
            services[i].init();
        }
    }

    @Override
    protected void startInternal() {
        System.out.println("StandardServer startInternal");
        synchronized (services) {
            for (int i = 0; i < services.length; i++) {
                services[i].start();
            }
        }
    }

    @Override
    protected void stopInternal() {

    }
}
