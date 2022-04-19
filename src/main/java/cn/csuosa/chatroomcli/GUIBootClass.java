package cn.csuosa.chatroomcli;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.util.Objects;

public class GUIBootClass extends Application
{
    @Getter
    private static Scene connectWindowScene;
    @Getter
    private static Scene talkWindowScene;
    @Getter
    private static Stage stage;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        try
        {
            connectWindowScene = new Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getClassLoader()
                    .getResource("FormsXML/ConnectWindow.fxml"))));    //从FXML加载窗口布局
            talkWindowScene = new Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getClassLoader()
                    .getResource("FormsXML/TalkWindow.fxml"))));    //从FXML加载窗口布局
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        GUIBootClass.stage = stage;

        stage.setOnCloseRequest((event) -> Core.quitApplication()); //窗口时自动停止所有进程并退出

        try
        {
            stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getClassLoader()
                    .getResourceAsStream("img/icon.png"))));
            stage.setScene(connectWindowScene);
            stage.setTitle("IRC-ChatRoom 连接与登录");
            stage.setResizable(false);
            stage.show();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - connectWindowScene.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - connectWindowScene.getHeight()) / 2);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

