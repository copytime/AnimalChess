package gui;

import com.sun.javafx.image.impl.ByteGrayAlpha;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import net.ICallBack;
import numberPropertyExtension.SimpleIntegerPropertyExt;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class App extends Application {
    private static App app = null;
    private static Stage mainStage = null;

    public static App getInstance() {
        return app;
    }

    public SimpleIntegerPropertyExt chessCount = new SimpleIntegerPropertyExt(6);
    public SimpleIntegerProperty test = new SimpleIntegerProperty(5);

    @Override
    public void start(Stage stage) throws Exception {
        app = this;
        mainStage = stage;
        changeScene("StartPage");
//        changeScene("JoinPage");
        stage.setTitle("斗兽棋");
        stage.setResizable(false);
        stage.setWidth(630);
        stage.setHeight(780);
        stage.show();
    }

    public void changeScene(String sceneName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                URL res = getClass().getResource(sceneName + ".fxml");
                System.out.println("Loading : " + res);
                FXMLLoader loader = new FXMLLoader(res);
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                IComponent controller = loader.getController();
                mainStage.setScene(new Scene(root));
                try {
                    controller.init();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Parent loadZone(String zoneName) throws IOException {
        URL res = getClass().getResource("zone/" + zoneName + ".fxml");
        System.out.println("Loading zone: " + res);
        FXMLLoader loader = new FXMLLoader(res);
        Parent root = loader.load();
        IComponent controller = loader.getController();
        controller.init();
        return root;
    }

    public void showMessage(Alert.AlertType type, String title, String head, String msg, ICallBack<Optional<ButtonType>> callBack) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(type);
                alert.setTitle(title);
                alert.setHeaderText(head);
                alert.setContentText(msg);
                callBack.invoke(alert.showAndWait());
            }
        });
    }

    public void showMessage(Alert.AlertType type, String title, String head, String msg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(type);
                alert.setTitle(title);
                alert.setHeaderText(head);
                alert.setContentText(msg);
                alert.show();
            }
        });
    }
}
