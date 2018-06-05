package com.ctrip.lpxie.basement.ribbon;

import com.google.common.collect.Lists;
import com.netflix.client.DefaultLoadBalancerRetryHandler;
import com.netflix.client.RetryHandler;
import com.netflix.loadbalancer.*;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import com.netflix.loadbalancer.reactive.ServerOperation;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.StringEntity;
import rx.Observable;

import java.util.List;

/**
 * Created by lpxie on 2016/6/23.
 */
public class MyClient {
    private final ILoadBalancer loadBalancer;
    // retry handler that does not retry on same server, but on a different server
    private RetryHandler retryHandler = new DefaultLoadBalancerRetryHandler(0, 1, true);//default retry handler
    private String path;
    private List<Server> serverList;

    private int connectTime = 100;
    private int socketTime = 500;

    public void setConnectTime(int connectTime) {
        this.connectTime = connectTime;
    }

    public void setSocketTime(int socketTime) {
        this.socketTime = socketTime;
    }

    public MyClient(List<Server> serverList,String path){
        loadBalancer = LoadBalancerBuilder.newBuilder().buildFixedServerListLoadBalancer(serverList);
        this.path = path;
        this.serverList = serverList;
    }

    public void setRetryHandler(RetryHandler retryHandler){
        this.retryHandler = retryHandler;
    }

    public Object execute(Object content){
        return LoadBalancerCommand.<Object>builder()
                .withLoadBalancer(loadBalancer)
                .withRetryHandler(retryHandler)
                .build()
                .submit(new MyOperation(content)).toBlocking().first();
    }

    public void setServerList(List<Server> serverList) {
        this.serverList = serverList;
        ((BaseLoadBalancer) loadBalancer).setServersList(Lists.newArrayList(serverList));//overwrite the old servers
    }

    public LoadBalancerStats getLoadBalancerStats() {
        return ((BaseLoadBalancer) loadBalancer).getLoadBalancerStats();
    }



    public static void main(String[] args){
        MyClient  myClient = new MyClient(Lists.newArrayList(new Server("10.15.99.133", 8080),//10.15.99.133:8080,10.15.99.134:8080,10.15.99.135:8080
                new Server("10.15.99.134", 8080), new Server("10.15.99.135", 8080))
                ,"/riskgraph/riskGraph/searchD");
        for(int i = 0 ;i<1000 ;i++)
        System.out.println(myClient.execute("[\"orderData|2|2348083896\"]"));

        for(int i = 0 ;i<1000 ;i++)
            System.out.println(myClient.execute("[\"orderData|2|2348083896\"]"));
        System.out.println("end");
    }



    class MyOperation implements ServerOperation {
        Object content;
        public MyOperation(Object content){
            this.content = content;
        }

        /**
         * change the code to apply yourself
         * logical
         * @param server
         * @return
         */
        @Override
        public Observable<String> call(Server server){
            try {
                String result = Request.Post("http://" + server.getHost() + ":" + server.getPort() + path)
                        .body(new StringEntity(content.toString()))
                        .connectTimeout(connectTime)
                        .socketTimeout(socketTime)
                        .execute().returnContent()
                        .asString();
                return Observable.just(result);
            }catch (Exception exp){
                return Observable.error(exp);
            }
        }

        public Observable<String> call(Object o){
            return null;
        }
    }
}
