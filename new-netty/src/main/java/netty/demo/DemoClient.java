package netty.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by lpxie on 2017/2/18.
 */
public class DemoClient {
    public static void main(String[] args){
        connect("127.0.0.1",8080);
    }

    static void connect(String host,int port){
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
//                        ch.pipeline().addLast("frameDecoder",new LineBasedFrameDecoder(80));
                        ch.pipeline().addLast("lengthDecoder",new LengthFieldBasedFrameDecoder(80,0,4,0,4));
                        ch.pipeline().addLast("stringDecoder",new StringDecoder());
                        ch.pipeline().addLast(new TimeClientHandler());
                    }
                });

        try {
            ChannelFuture channelFuture = bootstrap.connect(host,port).sync();
//            channelFuture.channel().close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
//            workerGroup.shutdownGracefully();
        }

    }
}
