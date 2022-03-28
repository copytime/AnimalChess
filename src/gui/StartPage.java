package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class StartPage implements IComponent{
    @FXML
    private Button btnHost;
    @FXML
    private ImageView imagePic;
    @FXML ImageView imageTitle;


    @FXML
    void onBtnHost(ActionEvent event) throws IOException {
        App.getInstance().changeScene("HostPage");
    }

    @FXML
    void onBtnJoin(ActionEvent event) {
        App.getInstance().changeScene("JoinPage");
    }

    @Override
    public void init() throws IOException {
        imagePic.setImage(new Image("START_PAGE.png"));
        imageTitle.setImage(new Image("START_TITLE.png"));
    }
}
