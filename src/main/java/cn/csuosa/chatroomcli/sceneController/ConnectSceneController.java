package cn.csuosa.chatroomcli.sceneController;

import cn.csuosa.chatroomcli.Core;
import cn.csuosa.chatroomcli.GUIBootClass;
import cn.csuosa.chatroomcli.Main;
import cn.csuosa.chatroomcli.proto.Request;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static cn.csuosa.chatroomcli.Main.socketClient;

public class ConnectSceneController implements Initializable
{
    public TextField textFieldServerURL;
    public TextField textFieldServerPort;
    public TextField textFieldUserNick;
    public PasswordField pwdField;
    public Button buttonConnect;

    public void startConnect()
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
            GUIBootClass.getStage().setScene(GUIBootClass.getTalkWindowScene());
            TalkSceneController.initialize(GUIBootClass.getStage().getScene());
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            GUIBootClass.getStage().setX((screenBounds.getWidth() - GUIBootClass.getTalkWindowScene().getWidth()) / 2);
            GUIBootClass.getStage().setY((screenBounds.getHeight() - GUIBootClass.getTalkWindowScene().getHeight()) / 2);
            GUIBootClass.getStage().setMinWidth(GUIBootClass.getTalkWindowScene().getWidth());
            GUIBootClass.getStage().setMinHeight(GUIBootClass.getTalkWindowScene().getHeight());
            GUIBootClass.getStage().setTitle("IRC-ChatRoom 聊天");
            GUIBootClass.getStage().setResizable(true);
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

    public static void initialize(Scene thisScene)
    {
        Platform.runLater(() -> {
            ((TextField) thisScene.lookup("#textFieldServerURL")).setText(Main.config.getServerURL());
            ((TextField) thisScene.lookup("#textFieldServerPort")).setText(Main.config.getServerPort());
            ((TextField) thisScene.lookup("#textFieldUserNick")).setText(Main.config.getUserNick());
            ((PasswordField) thisScene.lookup("#pwdField")).setText(Main.config.getPwd());
        });
    }
}
