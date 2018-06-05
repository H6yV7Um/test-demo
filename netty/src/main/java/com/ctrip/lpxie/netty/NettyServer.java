package com.ctrip.lpxie.netty;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.channel.socket.oio.OioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by lpxie on 2016/9/11.
 */
public class NettyServer {
    ServerBootstrap bootstrap;
    Channel parentChannel;
    InetSocketAddress localAddress;

    MyChannelHandler channelHandler = new MyChannelHandler();

    public NettyServer(){
        bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),Executors.newCachedThreadPool()));

        ChannelPipeline pipeline = bootstrap.getPipeline();

        pipeline.addLast("encode",new StringEncoder());
        pipeline.addLast("decode",new StringDecoder());
        pipeline.addLast("servercnfactroy", channelHandler);
        bootstrap.bind(new InetSocketAddress(8080));

    }
}
