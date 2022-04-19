package cn.csuosa.chatroomcli;

import cn.csuosa.chatroomcli.model.DisplayableChannelInfo;
import cn.csuosa.chatroomcli.proto.Request;
import cn.csuosa.chatroomcli.sceneController.TalkSceneController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Core
{
    public static ObservableList<DisplayableChannelInfo> channelInfoList = FXCollections.observableArrayList();

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
