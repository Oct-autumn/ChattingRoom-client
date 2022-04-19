package cn.csuosa.chatroomcli;

import cn.csuosa.chatroomcli.handlers.ResponseHandler;
import cn.csuosa.chatroomcli.proto.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import javafx.scene.control.Button;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public class SocketClient
{
    private final Bootstrap bootstrap = new Bootstrap();
    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    public SocketClient()
    {
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    protected void initChannel(SocketChannel socketChannel)
                    {
                        socketChannel.pipeline().addLast("decoder", new ProtobufDecoder(Response.ResponsePOJO.getDefaultInstance()));
                        socketChannel.pipeline().addLast("encoder", new ProtobufEncoder());
                        socketChannel.pipeline().addLast(new IdleStateHandler(5,5,5, TimeUnit.SECONDS));
                        socketChannel.pipeline().addLast(new ResponseHandler());
                    }
                });
    }

    /**
     * 新建Socket链接
     * @param url 连接目标url
     * @param port 连接目标port
     * @param buttonConnect 连接按钮
     */
    public void newConnect(String url, int port, Button buttonConnect)
    {
        new Thread()
        {
            @Override
            public void run()
            {

                ChannelFuture channelFuture = bootstrap.connect(url, port);
                channelFuture.awaitUninterruptibly();
                if (channelFuture.isSuccess())
                    Main.socketChannel = channelFuture.channel();
                buttonConnect.setDisable(false);
                try
                {
                    channelFuture.channel().closeFuture().sync();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }
}