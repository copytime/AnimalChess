package gui;

import game.GameFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class HostPage implements IComponent{

    private GameFactory gameFactory = null;

    @FXML
    private Button btnHome;

    @FXML
    void onBtnHome(ActionEvent event) {
        gameFactory.getGameManager().sendCancel();
        App.getInstance().changeScene("StartPage");
    }


    @Override
    public void init() throws IOException {
        gameFactory = new GameFactory();
    }
}
