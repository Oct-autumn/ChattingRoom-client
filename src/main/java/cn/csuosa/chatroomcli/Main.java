package cn.csuosa.chatroomcli;

import cn.csuosa.chatroomcli.common.MessageLog;
import cn.csuosa.chatroomcli.common.SocketWaitingReplyEvent;
import cn.csuosa.chatroomcli.config.Configuration;
import cn.csuosa.chatroomcli.proto.Request;
import io.netty.channel.Channel;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main
{
    public static final Configuration config = new Configuration();
    public static final SocketClient socketClient = new SocketClient();
    public static Channel socketChannel = null;
    public static final MessageLog consoleLog = new MessageLog();
    public static final BlockingQueue<SocketWaitingReplyEvent> waitingQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args)
    {
        //程序退出时的收尾工作
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (socketChannel != null && socketChannel.isOpen())
                Main.socketChannel.writeAndFlush(Request.RequestPOJO.newBuilder()
                        .setOperation(Request.RequestPOJO.Operation.LOGOUT).build());
            //关闭线程池
            socketClient.getEventLoopGroup().shutdownGracefully();
            System.out.println("Do shutdown work");
        }));

        GUIBootClass.main(args);
    }
}
