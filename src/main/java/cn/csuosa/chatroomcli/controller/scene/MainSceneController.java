package cn.csuosa.chatroomcli.controller.scene;

import cn.csuosa.chatroomcli.GUIBootClass;
import cn.csuosa.chatroomcli.Main;
import cn.csuosa.chatroomcli.proto.Request;
import j2html.TagCreator;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import static cn.csuosa.chatroomcli.Main.consoleLog;
import static j2html.TagCreator.*;

public class MainSceneController implements Initializable
{
    public VBox mainVBox;
    public HBox titleBar;
    public Button buttonExit;
    public Button buttonMinimize;
    public Text loginInfo;
    public WebView webView;
    public GridPane gridPaneChannelList;

    private boolean pwdIsVisible = false;

    /**
     * 退出程序
     */
    public void exitProgram()
    {
        GUIBootClass.getStage("mainStage").fireEvent(new WindowEvent(GUIBootClass.getStage("mainStage"), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    /**
     * 最小化窗口
     */
    public void minimizeWindow()
    {
        GUIBootClass.getStage("mainStage").setIconified(true);
    }

    /**
     * 显示设置窗口
     */
    public void showSettingWindow()
    {

    }

    /**
     * 显示关于窗口
     */
    public void showAboutWindow()
    {

    }

    /**
     * 断开连接
     */
    public void disconnectSocket()
    {
        if (Main.socketChannel.isOpen())
            Main.socketChannel.writeAndFlush(Request.RequestPOJO.newBuilder()
                    .setOperation(Request.RequestPOJO.Operation.LOGOUT).build());
    }

    /**
     * 切换密码可见性
     */
    public void togglePwdVisibility()
    {
        if (pwdIsVisible)
        {
            loginInfo.setText(String.format("%s  密码：%s",
                    Main.config.getUserAccount(),
                    Main.config.getPwd().equals("") ? "无" : "（点击查看）"));
            pwdIsVisible = false;
        } else
        {
            loginInfo.setText(String.format("%s  密码：%s",
                    Main.config.getUserAccount(),
                    Main.config.getPwd().equals("") ? "无" : Main.config.getPwd()));
            pwdIsVisible = true;
        }
    }

    /**
     * 显示创建频道窗口
     */
    public void showCreateChannelWindow()
    {

    }

    /**
     * 执行命令
     */
    public void executeCommand()
    {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Stage thisStage = GUIBootClass.getStage("mainStage");

        {//退出按钮 样式设定
            buttonExit.getStyleClass().clear();
            buttonExit.getStyleClass().add("button-close-base");
            buttonExit.getStyleClass().add("button-close-normal");
            buttonExit.setOnMouseEntered(mouseEvent -> buttonExit.getStyleClass().set(1,"button-close-mouse-on"));
            buttonExit.setOnMousePressed(mouseEvent -> buttonExit.getStyleClass().set(1,"button-close-mouse-pressed"));
            buttonExit.setOnMouseExited(mouseEvent -> buttonExit.getStyleClass().set(1,"button-close-normal"));
            buttonExit.setOnMouseReleased(mouseEvent -> {
                if (buttonExit.isHover())
                    buttonExit.getStyleClass().set(1, "button-close-mouse-on");
                else
                    buttonExit.getStyleClass().set(1, "button-close-normal");
            });
        }
        {//最小化按钮 样式设定
            buttonMinimize.getStyleClass().clear();
            buttonMinimize.getStyleClass().add("button-minimize-base");
            buttonMinimize.getStyleClass().add("button-minimize-normal");
            buttonMinimize.setOnMouseEntered(mouseEvent -> buttonMinimize.getStyleClass().set(1, "button-minimize-mouse-on"));
            buttonMinimize.setOnMouseExited(mouseEvent -> buttonMinimize.getStyleClass().set(1, "button-minimize-normal"));
            buttonMinimize.setOnMousePressed(mouseEvent -> buttonMinimize.getStyleClass().set(1, "button-minimize-mouse-pressed"));
            buttonMinimize.setOnMouseReleased(mouseEvent -> {
                if (buttonMinimize.isHover())
                    buttonMinimize.getStyleClass().set(1, "button-minimize-mouse-on");
                else
                    buttonMinimize.getStyleClass().set(1, "button-minimize-normal");
            });
        }

        //日志Web渲染
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

        {//拖拽标题栏重定位窗口
            AtomicReference<Double> mouseX_ori = new AtomicReference<>((double) 0);
            AtomicReference<Double> mouseY_ori = new AtomicReference<>((double) 0);
            AtomicReference<Double> stageX_ori = new AtomicReference<>((double) 0);
            AtomicReference<Double> stageY_ori = new AtomicReference<>((double) 0);

            titleBar.setOnMousePressed(mouseEvent -> {
                mouseX_ori.set(mouseEvent.getScreenX());
                mouseY_ori.set(mouseEvent.getScreenY());
                stageX_ori.set(thisStage.getX());
                stageY_ori.set(thisStage.getY());
            });
            titleBar.setOnMouseDragged(mouseEvent -> {//移动窗口
                double finalX = stageX_ori.get() + mouseEvent.getScreenX() - mouseX_ori.get();
                double finalY = stageY_ori.get() + mouseEvent.getScreenY() - mouseY_ori.get();
                finalX = Math.max(0, finalX);
                finalX = Math.min(GUIBootClass.getScreenBounds().getMaxX() - thisStage.getWidth(), finalX);
                finalY = Math.max(0, finalY);
                finalY = Math.min(GUIBootClass.getScreenBounds().getMaxY() - 25, finalY);
                thisStage.setX(finalX);
                thisStage.setY(finalY);
            });
        }
    }

    public static void initializeControls(Scene thisScene)
    {
        Platform.runLater(() -> {
            thisScene.setFill(Color.TRANSPARENT);
            ((Text) thisScene.lookup("#loginInfo")).setText(String.format("%s  密码：%s",
                    Main.config.getUserAccount(),
                    Main.config.getPwd().equals("") ? "无" : "（点击查看）"));
            ((Text) thisScene.lookup("#statusInfo")).setText("就绪.");
            ((WebView) thisScene.lookup("#webView")).getEngine().loadContent("<h1>TEST</h1>\n<p>Hello World~</p>");
        });
    }
}
