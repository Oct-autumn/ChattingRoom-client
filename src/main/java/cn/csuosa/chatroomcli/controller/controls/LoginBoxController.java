package cn.csuosa.chatroomcli.controller.controls;

import cn.csuosa.chatroomcli.Core;
import cn.csuosa.chatroomcli.GUIBootClass;
import cn.csuosa.chatroomcli.Main;
import cn.csuosa.chatroomcli.controller.scene.MainSceneController;
import cn.csuosa.chatroomcli.proto.Request;
import com.google.protobuf.ByteString;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static cn.csuosa.chatroomcli.Main.loginStatus;

public class LoginBoxController extends VBox implements Initializable
{
    public Label labelRegisterAccount;
    public Label labelForgotPwd;
    public TextField textFieldUserAccount;
    public TextField textFieldUserPwd;
    public Button buttonLogin;
    public Button buttonLoginAsNobody;

    private final VBox mainVBox;

    public LoginBoxController(VBox mainVBox)
    {
        this.mainVBox = mainVBox;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getClassLoader()
                .getResource("Fxml/Control/LoginBox-VBox.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try
        {
            fxmlLoader.load();
        } catch (IOException exception)
        {
            throw new RuntimeException(exception);
        }
    }

    public void switchToRegisterBox()
    {
        Main.config.setUserAccount(textFieldUserAccount.getText());
        Main.config.setPwd(textFieldUserPwd.getText());

        RegisterBoxController registerBoxController = new RegisterBoxController(mainVBox);
        mainVBox.getChildren().set(3, registerBoxController);
    }

    public void userLoginAction()
    {
        if (Main.socketChannel == null)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("提醒");
            alert.setContentText("请先连接至聊天服务器");
            alert.showAndWait();
            return;
        }

        {//非空检查
            boolean isComplete = true;
            if (textFieldUserAccount.getText().equals(""))
            {
                textFieldUserAccount.requestFocus();  //获取焦点
                textFieldUserAccount.getStyleClass().set(2, "text-field-status-error");
                isComplete = false;
            }
            if (textFieldUserPwd.getText().equals(""))
            {
                if (isComplete) textFieldUserPwd.requestFocus(); //获取焦点
                textFieldUserPwd.getStyleClass().set(2, "text-field-status-error");
                isComplete = false;
            }
            if (!isComplete) return;
        }

        Main.config.setUserAccount(textFieldUserAccount.getText());
        Main.config.setPwd(textFieldUserPwd.getText());

        Request.User.Builder userBuilder = Request.User.newBuilder()
                .setPwd(ByteString.copyFrom(Main.config.getPwd().getBytes()));

        if (Main.config.getUserAccount().matches("^\\d+$"))
            userBuilder.setUid(Long.parseLong(Main.config.getUserAccount()));
        else
            userBuilder.setEmail(Main.config.getUserAccount());

        Main.waitingQueue.newWait("LOGIN", null, true, (lockItem, eventItem) -> {
            String msg = (String) eventItem;
            if (msg.startsWith("Welcome"))
            {
                Platform.runLater(() -> {
                    Stage mainStage = GUIBootClass.getStage("mainStage");
                    mainStage.show();
                    MainSceneController.initializeControls(mainStage.getScene());
                    GUIBootClass.getStage("connectStage").close();
                    Core.loginAction();
                });
                loginStatus = 2;
            }
            else
            {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("失败");
                    alert.setContentText(msg);
                    alert.showAndWait();
                });
            }
        });

        Main.socketChannel.writeAndFlush(Request.RequestPOJO.newBuilder()
                .setOperation(Request.RequestPOJO.Operation.LOGIN)
                .setUser(userBuilder.build())
                .build());
    }

    public void nobodyLoginAction()
    {
        if (Main.socketChannel == null)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("提醒");
            alert.setContentText("请先连接至聊天服务器");
            alert.showAndWait();
            return;
        }

        Main.waitingQueue.newWait("LOGIN", null, true, (lockItem, eventItem) -> {
            Platform.runLater(() -> {
                Stage mainStage = GUIBootClass.getStage("mainStage");
                mainStage.show();
                MainSceneController.initializeControls(mainStage.getScene());
                GUIBootClass.getStage("connectStage").close();
                Core.loginAction();
            });
            loginStatus = 1;
        });

        Main.socketChannel.writeAndFlush(Request.RequestPOJO.newBuilder()
                .setOperation(Request.RequestPOJO.Operation.LOGIN)
                .build());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        {//Label样式设定
            //Label labelRegisterAccount = (Label) this.lookup("#labelRegisterAccount");
            labelRegisterAccount.getStyleClass().clear();
            labelRegisterAccount.getStyleClass().addAll("label-textButton-base", "label-textButton-normal");
            labelRegisterAccount.setOnMouseEntered(mouseEvent -> labelRegisterAccount.getStyleClass().set(1, "label-textButton-mouse-on"));
            labelRegisterAccount.setOnMouseExited(mouseEvent -> labelRegisterAccount.getStyleClass().set(1, "label-textButton-normal"));
            labelRegisterAccount.setOnMousePressed(mouseEvent -> labelRegisterAccount.getStyleClass().set(1, "label-textButton-mouse-pressed"));
            labelRegisterAccount.setOnMouseReleased(mouseEvent -> {
                if (labelRegisterAccount.isHover())
                    labelRegisterAccount.getStyleClass().set(1, "label-textButton-mouse-on");
                else
                    labelRegisterAccount.getStyleClass().set(1, "label-textButton-normal");
            });

            //Label labelForgotPwd = (Label) this.lookup("#labelForgotPwd");
            labelForgotPwd.getStyleClass().clear();
            labelForgotPwd.getStyleClass().addAll("label-textButton-base", "label-textButton-normal");
            labelForgotPwd.setOnMouseEntered(mouseEvent -> labelForgotPwd.getStyleClass().set(1, "label-textButton-mouse-on"));
            labelForgotPwd.setOnMouseExited(mouseEvent -> labelForgotPwd.getStyleClass().set(1, "label-textButton-normal"));
            labelForgotPwd.setOnMousePressed(mouseEvent -> labelForgotPwd.getStyleClass().set(1, "label-textButton-mouse-pressed"));
            labelForgotPwd.setOnMouseReleased(mouseEvent -> {
                if (labelForgotPwd.isHover())
                    labelForgotPwd.getStyleClass().set(1, "label-textButton-mouse-on");
                else
                    labelForgotPwd.getStyleClass().set(1, "label-textButton-normal");
            });
        }

        setTextFieldFocusStyle(textFieldUserAccount);
        setTextFieldFocusStyle(textFieldUserPwd);
    }

    private void setTextFieldFocusStyle(TextField textField)
    {
        //初始化样式
        textField.getStyleClass().clear();
        textField.getStyleClass().addAll("text-field-base", "text-field-normal", "text-field-status-unfocused");

        textField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (textField.getText().equals(""))
                textField.getStyleClass().set(2, "text-field-status-warning");
            else if (t1)
                textField.getStyleClass().set(2, "text-field-status-focused");
            else
                textField.getStyleClass().set(2, "text-field-status-unfocused");
        });

        textField.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.isEmpty())
                textField.getStyleClass().set(2, "text-field-status-warning");
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

    public void initializeControls()
    {
        textFieldUserAccount.setText(Main.config.getUserAccount());
        textFieldUserPwd.setText(Main.config.getPwd());
    }
}
