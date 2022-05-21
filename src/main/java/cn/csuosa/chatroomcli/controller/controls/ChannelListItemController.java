package cn.csuosa.chatroomcli.controller.controls;

import cn.csuosa.chatroomcli.Main;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChannelListItemController extends HBox implements Initializable
{
    private final String channelNameProperty;
    private final int onlineUserProperty;
    private final boolean publicChannelProperty;
    private final boolean joinedProperty;

    public ChannelListItemController(String channelName, int onlineUser, boolean isPublicChannel, boolean isJoined)
    {
        channelNameProperty = channelName;
        onlineUserProperty = onlineUser;
        publicChannelProperty = isPublicChannel;
        joinedProperty = isJoined;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getClassLoader()
                .getResource("Fxml/Control/ChannelListItem-HBox.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        {
            {
                Text textChannelName = (Text) this.lookup("#textChannelName");
                textChannelName.setText(channelNameProperty);

                Text textChannelInfo = (Text) this.lookup("#textChannelInfo");
                textChannelInfo.setText(String.format("(%s) 人数: %d", publicChannelProperty ? "公开" : "需要认证", onlineUserProperty));

                if (joinedProperty)
                {
                    textChannelName.getStyleClass().set(0, "text-channel-name-joined");
                    textChannelInfo.getStyleClass().set(0, "text-channel-info-joined");
                } else
                {
                    textChannelName.getStyleClass().set(0, "text-channel-name-normal");
                    textChannelInfo.getStyleClass().set(0, "text-channel-info-normal");
                }
            }
            {
                Button channelOperation = (Button) this.lookup("#channelOperation");
                if (joinedProperty)
                    channelOperation.setText("退出");
                else
                    channelOperation.setText("加入");

                channelOperation.setOnMouseEntered(mouseEvent -> channelOperation.getStyleClass().set(1, "button-channel-operation-mouse-on"));
                channelOperation.setOnMouseExited(mouseEvent -> channelOperation.getStyleClass().set(1, "button-channel-operation-normal"));
                channelOperation.setOnMousePressed(mouseEvent -> channelOperation.getStyleClass().set(1, "button-channel-operation-mouse-pressed"));
                channelOperation.setOnMouseReleased(mouseEvent -> {
                    if (channelOperation.isHover())
                        channelOperation.getStyleClass().set(1, "button-channel-operation-mouse-on");
                    else
                        channelOperation.getStyleClass().set(1, "button-channel-operation-normal");
                });
            }
        }

        this.getStyleClass().clear();
        this.getStyleClass().add("h-box-channel-list-item-normal");
        this.setOnMouseEntered(mouseEvent -> this.getStyleClass().set(0, "h-box-channel-list-item-mouse-on"));
        this.setOnMouseExited(mouseEvent -> this.getStyleClass().set(0, "h-box-channel-list-item-normal"));
        this.onMouseClickedProperty().addListener((observableValue, eventHandler, t1) ->
                this.getStyleClass().set(0, "h-box-channel-list-item-normal"));
        HBox.setHgrow(this, Priority.ALWAYS);
        HBox.setMargin(this, new Insets(0, 5, 0, 5));
    }
}
