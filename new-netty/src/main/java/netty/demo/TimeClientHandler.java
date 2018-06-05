package netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by lpxie on 2017/2/18.
 */
public class TimeClientHandler extends SimpleChannelInboundHandler {
    private final ByteBuf firstMessage;

    TimeClientHandler(){
        //byte[] request = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
        byte[] request = ("QUERY TIME ORDER").getBytes();
        firstMessage = Unpooled.buffer(request.length+4);
        firstMessage.writeInt(request.length);
        firstMessage.writeBytes(request);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*ByteBuf buf = (ByteBuf)msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req,"utf-8");*/
        String body = (String)msg;
        System.out.println("Now is:"+body);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
