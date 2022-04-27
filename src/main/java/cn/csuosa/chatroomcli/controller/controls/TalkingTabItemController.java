package cn.csuosa.chatroomcli.controller.controls;

import cn.csuosa.chatroomcli.Main;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ObservableListValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.ArrayChangeListener;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TalkingTabItemController extends HBox implements Initializable
{
    private final String channelName;

    public TalkingTabItemController(String channelName)
    {
        this.channelName = channelName;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getClassLoader()
                .getResource("Fxml/TalkingTabItem-HBox.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }
}
