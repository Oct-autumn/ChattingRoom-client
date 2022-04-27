package cn.csuosa.chatroomcli.controller.scene;

import cn.csuosa.chatroomcli.GUIBootClass;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class TalkingSceneController implements Initializable
{
    public VBox mainVBox;
    public HBox titleBar;
    public Button buttonExit;
    public Button buttonMaximize;
    public Button buttonMinimize;
    /**
     * 退出程序
     */
    public void exitProgram()
    {
        GUIBootClass.getStage("talkingStage").fireEvent(new WindowEvent(GUIBootClass.getStage("talkingStage"), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    /**
     * 最大化窗口
     */
    public void maximizeWindow()
    {
        GUIBootClass.getStage("talkingStage").setMaximized(!GUIBootClass.getStage("talkingStage").isMaximized());
    }

    /**
     * 最小化窗口
     */
    public void minimizeWindow()
    {
        GUIBootClass.getStage("talkingStage").setIconified(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Stage thisStage = GUIBootClass.getStage("talkingStage");

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
        {//最大化按钮 样式设定
            buttonMaximize.getStyleClass().clear();
            buttonMaximize.getStyleClass().add("button-maximize-base");
            buttonMaximize.getStyleClass().add("button-maximize-normal");
            buttonMaximize.setOnMouseEntered(mouseEvent -> buttonMaximize.getStyleClass().set(1, "button-maximize-mouse-on"));
            buttonMaximize.setOnMouseExited(mouseEvent -> buttonMaximize.getStyleClass().set(1, "button-maximize-normal"));
            buttonMaximize.setOnMousePressed(mouseEvent -> buttonMaximize.getStyleClass().set(1, "button-maximize-mouse-pressed"));
            buttonMaximize.setOnMouseReleased(mouseEvent -> {
                if (buttonMaximize.isHover())
                    buttonMaximize.getStyleClass().set(1, "button-maximize-mouse-on");
                else
                    buttonMaximize.getStyleClass().set(1, "button-maximize-normal");
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
}
