package cn.csuosa.chatroomcli;

import cn.csuosa.chatroomcli.model.Channel;
import cn.csuosa.chatroomcli.proto.Request;
import javafx.application.Platform;
import javafx.collections.FXCollections;

import java.util.HashMap;

public class Core
{
    public static HashMap<String, Channel> channelMap = new HashMap<>();

    public static void quitApplication()
    {
        System.exit(0);
    }

    /**
     * 连接登录后的操作，建立以用户Nick命名的新频道
     */
    public static void loginAction()
    {
        Main.socketChannel.writeAndFlush(Request.RequestPOJO.newBuilder()
                .setOperation(Request.RequestPOJO.Operation.CREATE_CHA)
                .setChannel(Request.Channel.newBuilder()
                        .setName(Main.config.getUserNick())
                        .setNick(Main.config.getUserNick())
                        .setTicket(Main.config.getPwd())
                        .build()));
    }

    public static void logoutAction()
    {
        Platform.runLater(()->{

        });
    }
}
