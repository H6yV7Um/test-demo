package com.ctrip.lpxie.netty;

import org.jboss.netty.channel.*;

/**
 * Created by lpxie on 2016/9/11.
 */
public class MyChannelHandler extends SimpleChannelHandler {
    public void messageReceived(
            ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        System.out.println("received message:" + e.getMessage());
        e.getChannel().write("server received :" + e.getMessage());
        super.messageReceived(ctx, e);
    }

    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        super.writeRequested(ctx, e);
    }

    public void channelConnected(
            ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channel Connected......");
        super.channelConnected(ctx, e);
    }

    public void channelClosed(
            ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelClosed");
        super.channelClosed(ctx,e);
    }
}
