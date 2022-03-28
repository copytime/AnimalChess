package gui;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class GamePage implements IComponent {

    @FXML
    private VBox vBoxContainer;

    @Override
    public void init() throws IOException {
        Parent infoZone = App.getInstance().loadZone("InfoZone");
        Parent gameZone = App.getInstance().loadZone("GameZone");
        vBoxContainer.getChildren().add(gameZone);
        vBoxContainer.getChildren().add(infoZone);
    }
}
