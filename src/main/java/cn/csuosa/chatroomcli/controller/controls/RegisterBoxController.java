package cn.csuosa.chatroomcli.controller.controls;

import cn.csuosa.chatroomcli.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterBoxController extends VBox implements Initializable
{
    public Label LabelUserLogin;

    public TextField textFieldUserEmail;
    public PasswordField textFieldUserPwd;
    public PasswordField textFieldUserPwdConfirm;

    private final VBox mainVBox;

    public RegisterBoxController(VBox mainVBox)
    {
        this.mainVBox = mainVBox;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getClassLoader()
                .getResource("Fxml/Control/RegisterBox-VBox.fxml"));

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

    /**
     * 注册账号操作
     */
    public void registerAccount()
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
            //检查
            if (textFieldUserEmail.getText().equals(""))
            {
                textFieldUserEmail.requestFocus();  //获取焦点
                textFieldUserEmail.getStyleClass().set(2, "text-field-status-error");
                isComplete = false;
            }
            if (textFieldUserPwd.getText().equals(""))
            {
                if (isComplete) textFieldUserPwd.requestFocus(); //获取焦点
                textFieldUserPwd.getStyleClass().set(2, "text-field-status-error");
                isComplete = false;
            }
            if (textFieldUserPwdConfirm.getText().equals(""))
            {
                if (isComplete) textFieldUserPwdConfirm.requestFocus(); //获取焦点
                textFieldUserPwdConfirm.getStyleClass().set(2, "text-field-status-error");
                isComplete = false;
            }
            if (!isComplete) return;
        }

        //检查两次输入密码是否一致
        if (!textFieldUserPwdConfirm.getText().equals(textFieldUserPwd.getText()))
        {
            textFieldUserPwdConfirm.requestFocus();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("异常");
            alert.setContentText("两次输入的密码不一致");
            alert.showAndWait();
            return;
        }

        //检查输入的是否为合法的Email地址
        if (!textFieldUserEmail.getText().matches("^\\w+([-+.]\\w+)*@(\\w+([-.]\\w+)*\\.)+\\w+([-.]\\w+)*$"))
        {
            textFieldUserPwdConfirm.requestFocus();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("异常");
            alert.setContentText("这不是一个合法的Email地址");
            alert.showAndWait();
            return;
        }

        //检查密码长度是否大于6
        if (textFieldUserPwd.getText().length() < 6)
        {
            textFieldUserPwd.requestFocus();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("警告");
            alert.setContentText("不安全的密码（密码长度小于6）");
            alert.showAndWait();
            return;
        }

        
    }

    public void switchToLoginControl()
    {
        LoginBoxController loginBoxController = new LoginBoxController(mainVBox);
        mainVBox.getChildren().set(3, loginBoxController);
        loginBoxController.initializeControls();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        {//Label样式设定
            LabelUserLogin.getStyleClass().clear();
            LabelUserLogin.getStyleClass().addAll("label-textButton-base", "label-textButton-normal");
            //Label labelRegisterAccount = (Label) this.lookup("#labelRegisterAccount");
            LabelUserLogin.setOnMouseEntered(mouseEvent -> LabelUserLogin.getStyleClass().set(1, "label-textButton-mouse-on"));
            LabelUserLogin.setOnMouseExited(mouseEvent -> LabelUserLogin.getStyleClass().set(1, "label-textButton-normal"));
            LabelUserLogin.setOnMousePressed(mouseEvent -> LabelUserLogin.getStyleClass().set(1, "label-textButton-mouse-pressed"));
            LabelUserLogin.setOnMouseReleased(mouseEvent -> {
                if (LabelUserLogin.isHover())
                    LabelUserLogin.getStyleClass().set(1, "label-textButton-mouse-on");
                else
                    LabelUserLogin.getStyleClass().set(1, "label-textButton-normal");
            });
        }

        setTextFieldStyle(textFieldUserEmail);
        setTextFieldStyle(textFieldUserPwd);

        {
            textFieldUserPwdConfirm.getStyleClass().clear();
            textFieldUserPwdConfirm.getStyleClass().addAll("text-field-base", "text-field-normal", "text-field-status-unfocused");

            textFieldUserPwdConfirm.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
                if (!textFieldUserPwdConfirm.getText().equals(textFieldUserPwd.getText()))
                    textFieldUserPwdConfirm.getStyleClass().set(2, "text-field-status-warning");
                else if (textFieldUserPwdConfirm.getText().equals(""))
                    textFieldUserPwdConfirm.getStyleClass().set(2, "text-field-status-error");
                else if (t1)
                    textFieldUserPwdConfirm.getStyleClass().set(2, "text-field-status-focused");
                else
                    textFieldUserPwdConfirm.getStyleClass().set(2, "text-field-status-unfocused");
            });

            textFieldUserPwdConfirm.textProperty().addListener((observableValue, s, t1) -> {
                if (!textFieldUserPwdConfirm.getText().equals(textFieldUserPwd.getText()))
                    textFieldUserPwdConfirm.getStyleClass().set(2, "text-field-status-warning");
                else if (t1.isEmpty())
                    textFieldUserPwdConfirm.getStyleClass().set(2, "text-field-status-error");
                else if (textFieldUserPwdConfirm.isFocused())
                    textFieldUserPwdConfirm.getStyleClass().set(2, "text-field-status-focused");
                else
                    textFieldUserPwdConfirm.getStyleClass().set(2, "text-field-status-unfocused");
            });
        }
    }

    private void setTextFieldStyle(TextField textField)
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
}
