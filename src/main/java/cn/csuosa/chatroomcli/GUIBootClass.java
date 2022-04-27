package cn.csuosa.chatroomcli;

import cn.csuosa.chatroomcli.controller.scene.ConnectSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class GUIBootClass extends Application
{
    @Getter
    private static Scene connectWindowScene;
    @Getter
    private static Scene mainWindowScene;
    @Getter
    private static Scene talkingWindowScene;
    @Getter
    private static Rectangle2D screenBounds;

    //TODO private static HBox channelListItem_

    private static final HashMap<String, Stage> stageHashMap = new HashMap<>();

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        screenBounds = Screen.getPrimary().getVisualBounds();
        {//初始化stageMap
            stageHashMap.put("connectStage", stage);
            stageHashMap.put("mainStage", new Stage());
            stageHashMap.put("talkingStage", new Stage());
        }
        try
        {
            {//从FXML加载窗口布局
                connectWindowScene = new Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getClassLoader()
                        .getResource("Fxml/Window/ConnectWindow.fxml")))); //登录界面
                mainWindowScene = new Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getClassLoader()
                        .getResource("Fxml/Window/MainWindow.fxml"))));    //主界面
                talkingWindowScene = new Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getClassLoader()
                        .getResource("Fxml/Window/TalkingWindow.fxml")))); //聊天界面
            }
            {//从FXML加载组件布局

            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        stage.setOnCloseRequest((event) -> Core.quitApplication()); //窗口时自动停止所有进程并退出
        stageHashMap.get("mainStage").setOnCloseRequest((event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("退出");
            alert.setContentText("确定退出IRC？");
            alert.showAndWait();
            if (alert.getResult().equals(ButtonType.OK)) Core.quitApplication();
            event.consume();
        }); //窗口时自动停止所有进程并退出

        try
        {
            stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getClassLoader()
                    .getResourceAsStream("img/icon.png"))));
            stage.setScene(connectWindowScene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("IRC-ChatRoom 连接与登录");
            stage.setResizable(false);
            ConnectSceneController.initializeControls(stage.getScene());
            stage.show();
            stage.setX((screenBounds.getWidth() - connectWindowScene.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - connectWindowScene.getHeight()) / 2);

            Stage mainStage = stageHashMap.get("mainStage");
            mainStage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getClassLoader()
                    .getResourceAsStream("img/icon.png"))));
            mainStage.setScene(mainWindowScene);
            mainStage.initStyle(StageStyle.TRANSPARENT);
            mainStage.setTitle("IRC-ChatRoom 聊天");
            mainStage.setX(screenBounds.getWidth()-400);
            mainStage.setY(20);
            mainStage.setMinWidth(mainWindowScene.getWidth());
            mainStage.setMinHeight(mainWindowScene.getHeight());
            mainStage.setMaxWidth(450);
            mainStage.setMaxHeight(screenBounds.getHeight());
            mainStage.setResizable(false);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static Stage getStage(String stageName)
    {
        return stageHashMap.get(stageName);
    }
}