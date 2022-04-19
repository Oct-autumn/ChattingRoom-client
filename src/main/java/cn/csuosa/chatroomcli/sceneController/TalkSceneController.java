package cn.csuosa.chatroomcli.sceneController;

import cn.csuosa.chatroomcli.Core;
import cn.csuosa.chatroomcli.Main;
import cn.csuosa.chatroomcli.model.DisplayableChannelInfo;
import cn.csuosa.chatroomcli.model.Message;
import cn.csuosa.chatroomcli.proto.Request;
import j2html.TagCreator;
import j2html.attributes.Attr;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Vector;

import static cn.csuosa.chatroomcli.Main.consoleLog;
import static j2html.TagCreator.*;
import static javafx.scene.text.Font.font;

public class TalkSceneController implements Initializable
{
    public Button buttonDisconnect;
    public Text loginInfo;
    public TableView<DisplayableChannelInfo> tableChannelList;
    public WebView webView;

    private boolean pwdIsVisible = false;

    public void disconnectSocket()
    {
        if (Main.socketChannel.isOpen())
            Main.socketChannel.writeAndFlush(Request.RequestPOJO.newBuilder()
                    .setOperation(Request.RequestPOJO.Operation.LOGOUT).build());
    }

    public void togglePwdVisibility()
    {
        if (pwdIsVisible)
        {
            loginInfo.setText(String.format("成功连接. 昵称：%s  密码：%s",
                    Main.config.getUserNick(),
                    Main.config.getPwd().equals("") ? "无" : "（点击查看）"));
            pwdIsVisible = false;
        } else
        {
            loginInfo.setText(String.format("成功连接. 昵称：%s  密码：%s",
                    Main.config.getUserNick(),
                    Main.config.getPwd().equals("") ? "无" : Main.config.getPwd()));
            pwdIsVisible = true;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        loginInfo.setText("LOGIN INFO");
        Core.channelInfoList = FXCollections.observableArrayList(
                new DisplayableChannelInfo("PublicChannel", 0, false, false)
        );

        tableChannelList.getColumns().clear();

        tableChannelList.getColumns().add(new TableColumn<DisplayableChannelInfo, String>("频道名称")
        {{
            minWidthProperty().setValue(100);
            prefWidthProperty().setValue(100);
            maxWidthProperty().setValue(340);
            this.setEditable(false);
            //this.
            this.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        }});
        tableChannelList.getColumns().add(new TableColumn<DisplayableChannelInfo, String>("在线人数")
        {{
            resizableProperty().setValue(false);
            minWidthProperty().setValue(85);
            prefWidthProperty().setValue(85);
            maxWidthProperty().setValue(85);
            this.setEditable(false);
            this.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOnlineMember()));
        }});
        tableChannelList.getColumns().add(new TableColumn<DisplayableChannelInfo, HBox>("需要认证")
        {{
            resizableProperty().setValue(false);
            sortableProperty().setValue(false);
            minWidthProperty().setValue(85);
            prefWidthProperty().setValue(85);
            maxWidthProperty().setValue(85);
            this.setEditable(false);
            this.setCellValueFactory(cellData -> {
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().add(cellData.getValue().getRequireVerify().getCheckBox().getValue());
                return getHBoxObservableValue(hBox);
            });
        }});

        tableChannelList.getColumns().add(new TableColumn<DisplayableChannelInfo, HBox>("已加入")
        {{
            resizableProperty().setValue(false);
            sortableProperty().setValue(false);
            minWidthProperty().setValue(70);
            prefWidthProperty().setValue(70);
            maxWidthProperty().setValue(70);
            this.setEditable(false);
            this.setCellValueFactory(cellData -> {
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().add(cellData.getValue().getJoined().getCheckBox().getValue());
                return getHBoxObservableValue(hBox);
            });
        }});

        tableChannelList.setItems(Core.channelInfoList);
        consoleLog.addListener((observableValue, messages, t1) -> {
            String rawHtml = TagCreator.document(
                    html(
                            body(
                                    main(
                                            each(t1, i -> div(
                                                    div(
                                                            String.format("System | %s", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(i.getTimestamp())))
                                                    ).attr("style=\"color:green\"")
                                                    ,
                                                    div(
                                                            i.getContent().toString(StandardCharsets.UTF_8)
                                                    ).attr("style=\"color:black\"")
                                            )))
                            )
                    )
            );

            Platform.runLater(() -> webView.getEngine().loadContent(rawHtml));
        });
    }

    public static void initialize(Scene thisScene)
    {
        Platform.runLater(() -> {
            ((Text) thisScene.lookup("#loginInfo")).setText(String.format("成功连接. 昵称：%s  密码：%s",
                    Main.config.getUserNick(),
                    Main.config.getPwd().equals("") ? "无" : "（点击查看）"));
            ((Text) thisScene.lookup("#statusInfo")).setText("就绪.");
            ((WebView) thisScene.lookup("#webView")).getEngine().loadContent("<h1>TEST</h1>\n<p>Hello World~</p>");

            TableView<DisplayableChannelInfo> tableChannelList = (TableView) thisScene.lookup("#tableChannelList");

            tableChannelList.getColumns().addListener(new ListChangeListener<>()
            {
                private boolean suspended = false;

                @Override
                public void onChanged(Change<? extends TableColumn<DisplayableChannelInfo, ?>> change)
                {
                    if (!suspended)
                        while (change.next())
                        {
                            if (!change.wasPermutated() && !change.wasUpdated())
                            {
                                suspended = true;
                                tableChannelList.getColumns().setAll(change.getRemoved());
                            }
                        }
                    else
                        suspended = false;
                }
            });
        });
    }

    private static ObservableValue<HBox> getHBoxObservableValue(HBox hBox)
    {
        return new ObservableValue<>()
        {
            @Override
            public void addListener(InvalidationListener invalidationListener)
            {

            }

            @Override
            public void removeListener(InvalidationListener invalidationListener)
            {

            }

            @Override
            public void addListener(ChangeListener<? super HBox> changeListener)
            {

            }

            @Override
            public void removeListener(ChangeListener<? super HBox> changeListener)
            {

            }

            @Override
            public HBox getValue()
            {
                return hBox;
            }
        };
    }
}
