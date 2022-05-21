package cn.csuosa.chatroomcli;

import cn.csuosa.chatroomcli.model.Channel;
import cn.csuosa.chatroomcli.proto.Request;
import javafx.application.Platform;

import java.util.HashMap;

public class Core
{
    public static HashMap<String, Channel> channelMap = new HashMap<>();

    public static void quitApplication()
    {
        System.exit(0);
    }

    public static void loginAction()
    {
    }

    public static void logoutAction()
    {
        Platform.runLater(()->{

        });
    }
}
