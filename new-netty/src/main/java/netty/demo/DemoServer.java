package netty.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by lpxie on 2017/2/18.
 */
public class DemoServer {
    public static void main(String[] args){
        int port = 8080;
        bind(port);
    }

    static void bind(int port){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChildChannelHandler());

        try {
            ChannelFuture channelFuture = bootstrap.bind(port).sync();

//            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
        }

    }

    static class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
//            ch.pipeline().addLast("frameDecoder",new LineBasedFrameDecoder(80));
            ch.pipeline().addLast("lengthDecoder",new LengthFieldBasedFrameDecoder(80,0,4,0,4));
            ch.pipeline().addLast("stringDecoder",new StringDecoder());
            ch.pipeline().addLast(new TimeServerHandler());
        }
    }
}
