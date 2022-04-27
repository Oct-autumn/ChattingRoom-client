package cn.csuosa.chatroomcli.controller.scene;

import cn.csuosa.chatroomcli.Core;
import cn.csuosa.chatroomcli.GUIBootClass;
import cn.csuosa.chatroomcli.Main;
import cn.csuosa.chatroomcli.proto.Request;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import static cn.csuosa.chatroomcli.Main.socketClient;

public class ConnectSceneController implements Initializable
{
    public TextField textFieldServerURL;
    public TextField textFieldServerPort;
    public TextField textFieldUserNick;
    public PasswordField pwdField;
    public Button buttonConnect;
    public HBox titleBar;
    public Button buttonMinimize;
    public Button buttonExit;

    /**
     * 退出程序
     */
    public void exitProgram()
    {
        GUIBootClass.getStage("connectStage").fireEvent(new WindowEvent(GUIBootClass.getStage("connectStage"), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    /**
     * 最小化窗口
     */
    public void minimizeWindow()
    {
        GUIBootClass.getStage("connectStage").setIconified(true);
    }

    public void connectAndRegister()
    {
        {//非空检查

        }
    }

    /**
     * 登录并连接服务器
     */
    public void connectAndLogin()
    {
        {//非空检查
            boolean isComplete = true;
            if (textFieldServerURL.getText().equals(""))
            {
                textFieldServerURL.requestFocus();
                isComplete = false;
            }
            if (textFieldServerPort.getText().equals(""))
            {
                if (isComplete) textFieldServerPort.requestFocus();
                isComplete = false;
            }
            // TODO: 限制最短昵称
            if (textFieldUserNick.getText().equals(""))
            {
                if (isComplete) textFieldUserNick.requestFocus();
                isComplete = false;
            }
            // TODO: 限制最短密码
            if (!pwdField.getText().equals("") && (pwdField.getText().length() < 4 || pwdField.getText().length() > 32))
            {
                if (isComplete) pwdField.requestFocus();
                isComplete = false;
            }
            if (!isComplete) return;
        }

        buttonConnect.setDisable(true);
        boolean reconnect = false;
        if (!Objects.equals(textFieldServerURL.getText(), Main.config.getServerURL()))
        {
            Main.config.setServerURL(textFieldServerURL.getText());
            reconnect = true;
        }
        if (!Objects.equals(textFieldServerPort.getText(), Main.config.getServerURL()))
        {
            Main.config.setServerPort(textFieldServerPort.getText());
            reconnect = true;
        }

        Main.config.setUserNick(textFieldUserNick.getText());
        Main.config.setPwd(pwdField.getText());

        if (reconnect || Main.socketChannel == null)
        {
            if (Main.socketChannel != null && Main.socketChannel.isOpen())
                Main.socketChannel.writeAndFlush(Request.RequestPOJO.newBuilder()
                        .setOperation(Request.RequestPOJO.Operation.LOGOUT).build());
            socketClient.newConnect(Main.config.getServerURL(), Integer.parseInt(Main.config.getServerPort()), buttonConnect);
        }

        while (buttonConnect.isDisabled())
        {
            try
            {
                Thread.sleep(0);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        if (Main.socketChannel != null)
        {
            Stage mainStage = GUIBootClass.getStage("mainStage");

            MainSceneController.initializeControls(mainStage.getScene());
            mainStage.show();
            GUIBootClass.getStage("connectStage").close();
            Core.loginAction();
        } else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("连接失败");
            alert.setContentText("连接服务器失败，请检查网络连接与填写的服务器信息");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Stage thisStage = GUIBootClass.getStage("connectStage");

        {//退出按钮 样式设定
            buttonExit.getStyleClass().clear();
            buttonExit.getStyleClass().add("button-close-base");
            buttonExit.getStyleClass().add("button-close-normal");
            buttonExit.setOnMouseEntered(mouseEvent -> buttonExit.getStyleClass().set(1, "button-close-mouse-on"));
            buttonExit.setOnMousePressed(mouseEvent -> buttonExit.getStyleClass().set(1, "button-close-mouse-pressed"));
            buttonExit.setOnMouseExited(mouseEvent -> buttonExit.getStyleClass().set(1, "button-close-normal"));
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

        //输入框样式
        setTextFieldFocusStyle(textFieldServerURL);
        setTextFieldFocusStyle(textFieldServerPort);
        setTextFieldFocusStyle(textFieldUserNick);
        setTextFieldFocusStyle(pwdField);

        //URL字符检查
        textFieldServerURL.setTextFormatter(new TextFormatter<TextFormatter.Change>(c -> {
            if (c.isContentChange() && c.isAdded() && !c.getText().matches("[^\\s]*"))
            {
                c.setText("");
                c.setCaretPosition(Math.max(c.getCaretPosition() - 1, 0));
                c.setAnchor(Math.max(c.getAnchor() - 1, 0));
            }
            return c;
        }));
        //端口号检查
        textFieldServerPort.setTextFormatter(new TextFormatter<TextFormatter.Change>(c -> {
            if (c.isContentChange() && c.isAdded())
                if (!c.getControlNewText().matches("[1-9][0-9]*")
                        || Integer.parseInt(c.getControlNewText()) > 65535)
                {
                    c.setText("");
                    c.setCaretPosition(Math.max(c.getCaretPosition() - 1, 0));
                    c.setAnchor(Math.max(c.getAnchor() - 1, 0));
                }
            return c;
        }));
        //昵称检查
        textFieldUserNick.setTextFormatter(new TextFormatter<TextFormatter.Change>(c -> {
            if (c.isContentChange() && c.isAdded() && (!c.getText().matches("^\\w+$") || c.getControlNewText().length() > 32))
            {
                c.setText("");
                c.setCaretPosition(Math.max(c.getCaretPosition() - 1, 0));
                c.setAnchor(Math.max(c.getAnchor() - 1, 0));
            }
            return c;
        }));
        //密码检查
        pwdField.setTextFormatter(new TextFormatter<TextFormatter.Change>(c -> {
            if (c.isContentChange() && c.isAdded() && (!c.getText().matches("^[\\w]+$") || c.getControlNewText().length() > 32))
            {
                c.setText("");
                c.setCaretPosition(Math.max(c.getCaretPosition() - 1, 0));
                c.setAnchor(Math.max(c.getAnchor() - 1, 0));
            }
            return c;
        }));

        textFieldServerURL.setText(Main.config.getServerURL());
        textFieldServerPort.setText(Main.config.getServerPort());
        textFieldUserNick.setText(Main.config.getUserNick());
        pwdField.setText(Main.config.getPwd());

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

    private void setTextFieldFocusStyle(TextField textField)
    {
        //初始化样式
        textField.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), null)));
        textField.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        textField.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.LIGHTGRAY, 5, 0.5, 0, 0));

        textField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (textField.getText().equals(""))
                textField.setBackground(new Background(new BackgroundFill(Color.PINK, new CornerRadii(5), null)));
            else
                textField.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), null)));

            if (observableValue.getValue())
            {
                textField.setBorder(new Border(new BorderStroke(Color.DODGERBLUE, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
                textField.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.DEEPSKYBLUE, 5, 0.25, 0, 0));
            } else
            {
                textField.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
                textField.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.LIGHTGRAY, 5, 0.5, 0, 0));
            }
        });
    }

    public static void initializeControls(Scene thisScene)
    {
        Platform.runLater(() -> {
            thisScene.setFill(Color.TRANSPARENT);
            /*GaussianBlur gaussianBlur = new GaussianBlur();
            gaussianBlur.setRadius(10.5);
            thisScene.lookup("#mainVBox").setEffect(gaussianBlur);*/
            ((TextField) thisScene.lookup("#textFieldServerURL")).setText(Main.config.getServerURL());
            ((TextField) thisScene.lookup("#textFieldServerPort")).setText(Main.config.getServerPort());
            ((TextField) thisScene.lookup("#textFieldUserNick")).setText(Main.config.getUserNick());
            ((PasswordField) thisScene.lookup("#pwdField")).setText(Main.config.getPwd());
        });
    }
}
