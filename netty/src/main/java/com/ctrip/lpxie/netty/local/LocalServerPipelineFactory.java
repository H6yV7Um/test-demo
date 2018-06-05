package com.ctrip.lpxie.netty.local;

import java.util.concurrent.Executor;

import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;

public class LocalServerPipelineFactory implements ChannelPipelineFactory {

    private final ExecutionHandler executionHandler;

    public LocalServerPipelineFactory(Executor eventExecutor) {
        executionHandler = new ExecutionHandler(eventExecutor);
    }

    public ChannelPipeline getPipeline() throws Exception {
        final ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast("executor", executionHandler);
        pipeline.addLast("handler", new EchoCloseServerHandler());
        return pipeline;
    }

    static class EchoCloseServerHandler implements ChannelUpstreamHandler, ChannelDownstreamHandler {
        public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
                throws Exception {
            if (e instanceof MessageEvent) {
                final MessageEvent evt = (MessageEvent) e;
                String msg = (String) evt.getMessage();
                if (msg.equalsIgnoreCase("quit")) {
                    Channels.close(e.getChannel());
                    return;
                }
            }
            ctx.sendUpstream(e);
        }

        public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) {
            if (e instanceof MessageEvent) {
                final MessageEvent evt = (MessageEvent) e;
                String msg = (String) evt.getMessage();
                if (msg.equalsIgnoreCase("quit")) {
                    Channels.close(e.getChannel());
                    return;
                }
                System.err.println("SERVER:" + msg);
                // Write back
                Channels.write(e.getChannel(), msg);
            }
            ctx.sendDownstream(e);
        }
    }
}
