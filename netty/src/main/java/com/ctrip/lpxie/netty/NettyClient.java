package com.ctrip.lpxie.netty;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.oio.OioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created by lpxie on 2017/2/8.
 */
public class NettyClient {
    public static void main(String[] args){
        ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory());

        ChannelPipeline pipeline = bootstrap.getPipeline();

        MyChannelHandler channelHandler = new MyChannelHandler();

        pipeline.addLast("encode", new StringEncoder());
        pipeline.addLast("decode", new StringDecoder());
        pipeline.addLast("servercnfactroy", channelHandler);

        ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8080));

        for(;;){
//            future.getChannel().setInterestOps(Channel)
            if(future.getChannel().isConnected()){
                break;
            }
        }
        future.getChannel().setInterestOps(Channel.OP_WRITE);
        future.getChannel().write("First message");



//        future.getChannel().close();

//        bootstrap.releaseExternalResources();
    }
}
