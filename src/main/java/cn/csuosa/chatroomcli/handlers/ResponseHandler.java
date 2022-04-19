package cn.csuosa.chatroomcli.handlers;

import cn.csuosa.chatroomcli.Core;
import cn.csuosa.chatroomcli.GUIBootClass;
import cn.csuosa.chatroomcli.Main;
import cn.csuosa.chatroomcli.model.DisplayableChannelInfo;
import cn.csuosa.chatroomcli.model.Message;
import cn.csuosa.chatroomcli.proto.Request;
import cn.csuosa.chatroomcli.proto.Response;
import cn.csuosa.chatroomcli.sceneController.ConnectSceneController;
import com.google.protobuf.ByteString;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TableView;
import javafx.stage.Screen;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class ResponseHandler extends ChannelInboundHandlerAdapter
{
    /**
     * 心跳包
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
                case READER_IDLE -> {}
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
        Platform.runLater(()->{
            GUIBootClass.getStage().setScene(GUIBootClass.getConnectWindowScene());
            ConnectSceneController.initialize(GUIBootClass.getStage().getScene());
            GUIBootClass.getStage().setTitle("IRC-ChatRoom 连接与登录");
            GUIBootClass.getStage().setResizable(false);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            GUIBootClass.getStage().setX((screenBounds.getWidth() - GUIBootClass.getConnectWindowScene().getWidth()) / 2);
            GUIBootClass.getStage().setY((screenBounds.getHeight() - GUIBootClass.getConnectWindowScene().getHeight()) / 2);
            GUIBootClass.getStage().setMinWidth(GUIBootClass.getConnectWindowScene().getWidth());
            GUIBootClass.getStage().setMinHeight(GUIBootClass.getConnectWindowScene().getHeight());
        });
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception
    {
        Response.ResponsePOJO response = (Response.ResponsePOJO) msg;
        switch (response.getType())
        {
            case RETAIN ->{
            }
            case RESULT ->{
                if (response.getResult().getStCode() != 0)
                    System.out.println("Error: StCode-" + response.getResult().getStCode() + " | " + response.getResult().getMsg());
            }
            case PUSH_MSG -> {}
            case PUSH_CHA_LIST -> {
                Main.consoleLog.addMessage(new Message(new Date().getTime(), "", "", Message.MessageType.PLAIN_TEXT, ByteString.copyFrom("Update channel list".getBytes(StandardCharsets.UTF_8))));
                Core.channelInfoList.clear();
                response.getChannelInfoList().forEach(channelInfo -> Core.channelInfoList.add(new DisplayableChannelInfo(channelInfo.getName(), channelInfo.getMemberNum(), !channelInfo.getIsPublic(), channelInfo.getIsIN())));
                Platform.runLater(()-> ((TableView<DisplayableChannelInfo>)GUIBootClass.getTalkWindowScene().lookup("#tableChannelList")).setItems(Core.channelInfoList));
            }
            case PUSH_MEMBER_LIST -> {}
            case PUSH_SYS_INFO -> {}
            case UNRECOGNIZED -> {}
            default -> {}
        }
    }
}
