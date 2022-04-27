package cn.csuosa.chatroomcli.handlers;

import cn.csuosa.chatroomcli.Core;
import cn.csuosa.chatroomcli.GUIBootClass;
import cn.csuosa.chatroomcli.Main;
import cn.csuosa.chatroomcli.controller.controls.ChannelListItemController;
import cn.csuosa.chatroomcli.model.Channel;
import cn.csuosa.chatroomcli.model.Message;
import cn.csuosa.chatroomcli.proto.Request;
import cn.csuosa.chatroomcli.proto.Response;
import cn.csuosa.chatroomcli.controller.scene.ConnectSceneController;
import com.google.protobuf.ByteString;
import com.sun.javafx.collections.ObservableListWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

public class ResponseHandler extends ChannelInboundHandlerAdapter
{
    /**
     * 心跳包
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {
        if (evt instanceof IdleStateEvent)
        {
            switch (((IdleStateEvent) evt).state())
            {
                //定时发送心跳包
                case WRITER_IDLE, ALL_IDLE -> Main.socketChannel.writeAndFlush(Request.RequestPOJO.newBuilder()
                        .setOperation(Request.RequestPOJO.Operation.HEARTBEAT).build());
                case READER_IDLE ->
                {
                }
            }
        } else
        {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        Main.socketChannel = null;
        Core.logoutAction();
        Platform.runLater(() -> {
            Stage connectStage = GUIBootClass.getStage("connectStage");
            Scene scene = GUIBootClass.getConnectWindowScene();
            connectStage.show();
            ConnectSceneController.initializeControls(connectStage.getScene());
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            connectStage.setX((screenBounds.getWidth() - scene.getWidth()) / 2);
            connectStage.setY((screenBounds.getHeight() - scene.getHeight()) / 2);
            GUIBootClass.getStage("mainStage").close();
        });
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception
    {
        Response.ResponsePOJO response = (Response.ResponsePOJO) msg;
        switch (response.getType())
        {
            case RETAIN ->
            {
            }
            case RESULT ->
            {
                if (response.getResult().getStCode() != 0)
                    Main.consoleLog.addMessage(new Message(new Date().getTime(), "", "", Message.MessageType.PLAIN_TEXT, response.getResult().getMsgBytes()));
            }
            case PUSH_MSG ->
            {
                Response.Message it = response.getMessage();
                Core.channelMap.get(it.getChannel()).getMessageLog()
                        .addMessage(new Message(
                                it.getTimestamp(),
                                it.getChannel(),
                                it.getFromNick(),
                                Message.MessageType.values()[it.getType()],
                                it.getContent()
                        ));
            }
            case PUSH_CHA_LIST ->
            {
                Main.consoleLog.addMessage(new Message(new Date().getTime(), "", "", Message.MessageType.PLAIN_TEXT, ByteString.copyFrom("Update channel list".getBytes(StandardCharsets.UTF_8))));
                ObservableList<ChannelListItemController> listItems = new ObservableListWrapper<>(new ArrayList<>());
                response.getChannelInfoList().forEach(channelInfo -> {
                    if (!Core.channelMap.containsKey(channelInfo.getName()))
                        Core.channelMap.put(channelInfo.getName(), new Channel(channelInfo.getName(), channelInfo.getMemberNum(), channelInfo.getIsPublic(), channelInfo.getIsIN(), null, null));
                    else
                    {
                        Channel thisChannel = Core.channelMap.get(channelInfo.getName());
                        thisChannel.setOnlineMember(channelInfo.getMemberNum());
                        thisChannel.setJoined(channelInfo.getIsIN());
                    }
                    System.out.printf("%s %d %s %s%n", channelInfo.getName(), channelInfo.getMemberNum(), channelInfo.getIsPublic() ? "Public" : "Verification", channelInfo.getIsIN() ? "<joined>" : "<not joined>");
                    listItems.add(new ChannelListItemController(channelInfo.getName(), channelInfo.getMemberNum(), channelInfo.getIsPublic(), channelInfo.getIsIN()));
                });
                Platform.runLater(() -> {
                    GridPane gridPaneChannelList = (GridPane) GUIBootClass.getMainWindowScene().lookup("#gridPaneChannelList");
                    int i = 0;
                    gridPaneChannelList.getChildren().clear();
                    gridPaneChannelList.getRowConstraints().clear();
                    for (ChannelListItemController item : listItems)
                    {
                        gridPaneChannelList.addRow(i, item);
                        i++;
                    }
                });
            }
            case PUSH_LOGIN_USER_LIST ->
            {
            }
            case PUSH_CHA_MEMBER_LIST ->
            {
            }
            case PUSH_SYS_INFO ->
            {
            }
            case UNRECOGNIZED ->
            {
            }
            default ->
            {
            }
        }
    }
}
