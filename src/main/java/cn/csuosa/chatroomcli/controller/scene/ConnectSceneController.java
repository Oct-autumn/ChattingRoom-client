package cn.csuosa.chatroomcli.controller.scene;

import cn.csuosa.chatroomcli.GUIBootClass;
import cn.csuosa.chatroomcli.Main;
import cn.csuosa.chatroomcli.controller.controls.LoginBoxController;
import cn.csuosa.chatroomcli.controller.controls.RegisterBoxController;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import static cn.csuosa.chatroomcli.Main.*;

public class ConnectSceneController implements Initializable
{
    public TextField textFieldServerURL;
    public TextField textFieldServerPort;
    public Button buttonToggleConnection;
    public HBox titleBar;
    public Button buttonMinimize;
    public Button buttonExit;
    public VBox mainVBox;

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

    /**
     * 连接聊天服务器
     */
    public void connectToServer()
    {
        {//非空检查
            boolean isComplete = true;
            if (textFieldServerURL.getText().equals(""))
            {
                textFieldServerURL.requestFocus();  //获取焦点
                textFieldServerURL.getStyleClass().set(2, "text-field-status-error");
                isComplete = false;
            }
            if (textFieldServerPort.getText().equals(""))
            {
                if (isComplete) textFieldServerPort.requestFocus(); //获取焦点
                textFieldServerPort.getStyleClass().set(2, "text-field-status-error");
                isComplete = false;
            }
            if (!isComplete) return;
        }

        Main.config.setServerURL(textFieldServerURL.getText());
        Main.config.setServerPort(textFieldServerPort.getText());

        //锁定服务器信息输入框、锁定Toggle按钮
        buttonToggleConnection.setDisable(true);
        textFieldServerURL.setDisable(true);
        textFieldServerPort.setDisable(true);

        if (Main.socketChannel != null && Main.socketChannel.isOpen())
            Main.socketChannel.close();
        socketClient.newConnect(Main.config.getServerURL(), Integer.parseInt(Main.config.getServerPort()));

        //异步等待事件：Socket建立后 锁定输入框，解锁Toggle按钮，并设置为断开连接
        waitingQueue.newWait("Socket", buttonToggleConnection, true, (lockItem, eventItem) ->
                Platform.runLater(() -> {
                    buttonToggleConnection.setDisable(false);
                    if (Main.socketChannel == null)
                    {
                        //解锁服务器信息输入框并告警
                        textFieldServerURL.getStyleClass().set(2, "text-field-status-error");
                        textFieldServerURL.setDisable(false);
                        textFieldServerPort.getStyleClass().set(2, "text-field-status-error");
                        textFieldServerPort.setDisable(false);
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("连接失败");
                        alert.setContentText("连接服务器失败，请检查网络连接与填写的服务器信息");
                        alert.showAndWait();
                        return;
                    }

                    textFieldServerURL.getStyleClass().set(2, "text-field-status-ok");
                    textFieldServerPort.getStyleClass().set(2, "text-field-status-ok");

                    buttonToggleConnection.setText("断开连接");
                    buttonToggleConnection.setOnAction(actionEvent -> disconnect());
                }));
        /*Stage mainStage = GUIBootClass.getStage("mainStage");

            MainSceneController.initializeControls(mainStage.getScene());
            mainStage.show();
            GUIBootClass.getStage("connectStage").close();
            Core.loginAction();*/
    }

    public void disconnect()
    {
        buttonToggleConnection.setDisable(true);
        Main.socketChannel.close();
        //异步等待事件：Socket断开后 解锁输入框，解锁Toggle按钮，并设置为连接服务器
        waitingQueue.newWait("Socket", buttonToggleConnection, true, (lockItem, eventItem) -> {
            Platform.runLater(() -> {
                textFieldServerURL.getStyleClass().set(2, "text-field-status-unfocused");
                textFieldServerURL.setDisable(false);
                textFieldServerPort.getStyleClass().set(2, "text-field-status-unfocused");
                textFieldServerPort.setDisable(false);

                buttonToggleConnection.setDisable(false);
                buttonToggleConnection.setText("连接服务器");
                buttonToggleConnection.setOnAction(actionEvent -> connectToServer());
            });
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Stage thisStage = GUIBootClass.getStage("connectStage");

        {//退出按钮 样式设定
            buttonExit.getStyleClass().clear();
            buttonExit.getStyleClass().addAll("button-close-base", "button-close-normal");
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
            buttonMinimize.getStyleClass().addAll("button-minimize-base", "button-minimize-normal");
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
        {//连接/断开连接按钮 样式设定
            buttonToggleConnection.getStyleClass().clear();
            buttonToggleConnection.getStyleClass().addAll("button-toggle-connection-base", "button-toggle-connection-connect");
            buttonToggleConnection.disabledProperty().addListener((observableValue, aBoolean, t1) -> {
                if (t1)
                    buttonToggleConnection.getStyleClass().set(1, "button-toggle-connection-disabled");
                else if (Main.socketChannel == null)
                    buttonToggleConnection.getStyleClass().set(1, "button-toggle-connection-connect");
                else
                    buttonToggleConnection.getStyleClass().set(1, "button-toggle-connection-disconnect");
            });
        }

        //输入框样式
        setTextFieldFocusStyle(textFieldServerURL);
        setTextFieldFocusStyle(textFieldServerPort);

        //端口号检查
        textFieldServerPort.setTextFormatter(new TextFormatter<TextFormatter.Change>(c -> {
            if (c.isContentChange() && c.isAdded())
                if (!c.getControlNewText().matches("(^[1-9]\\d{0,4}$)|(^0$)")
                        || Integer.parseInt(c.getControlNewText()) > 65535)
                {
                    c.setText("");
                    c.setCaretPosition(Math.max(c.getCaretPosition() - 1, 0));
                    c.setAnchor(Math.max(c.getAnchor() - 1, 0));
                }
            return c;
        }));

        textFieldServerURL.setText(Main.config.getServerURL());
        textFieldServerPort.setText(Main.config.getServerPort());

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
        textField.getStyleClass().clear();
        textField.getStyleClass().addAll("text-field-base", "text-field-normal", "text-field-status-unfocused");

        textField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (textField.getText().equals(""))
                textField.getStyleClass().set(2, "text-field-status-error");
            else if (t1)
                textField.getStyleClass().set(2, "text-field-status-focused");
            else
                textField.getStyleClass().set(2, "text-field-status-unfocused");
        });

        textField.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.isEmpty())
                textField.getStyleClass().set(2, "text-field-status-error");
            else if (textField.isFocused())
                textField.getStyleClass().set(2, "text-field-status-focused");
            else
                textField.getStyleClass().set(2, "text-field-status-unfocused");
        });

        textField.disabledProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1)
                textField.getStyleClass().set(1, "text-field-disabled");
            else
                textField.getStyleClass().set(1, "text-field-normal");
        });
    }

    public static void initializeControls(Scene thisScene)
    {

        Platform.runLater(() -> {
            LoginBoxController loginBoxController = new LoginBoxController((VBox) thisScene.lookup("#mainVBox"));

            ((VBox) thisScene.lookup("#mainVBox")).getChildren().set(3, loginBoxController);


            thisScene.setFill(Color.TRANSPARENT);
            /*GaussianBlur gaussianBlur = new GaussianBlur();
            gaussianBlur.setRadius(10.5);
            thisScene.lookup("#mainVBox").setEffect(gaussianBlur);*/
            ((TextField) thisScene.lookup("#textFieldServerURL")).setText(Main.config.getServerURL());
            ((TextField) thisScene.lookup("#textFieldServerPort")).setText(Main.config.getServerPort());
        });
    }


}
