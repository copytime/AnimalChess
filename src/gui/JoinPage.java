package gui;

import game.GameFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class JoinPage implements IComponent {

    @FXML
    private TextField textFiledIp0;

    @FXML
    private TextField textFiledIp1;

    @FXML
    private TextField textFiledIp2;

    @FXML
    private TextField textFiledIp3;

    @FXML
    private Button btnConnect;

    @FXML
    private Button btnHome;

    private GameFactory gameFactory = null;

    @FXML
    void onBtnConnect(ActionEvent event) {
        String ip0 = textFiledIp0.getText();
        String ip1 = textFiledIp1.getText();
        String ip2 = textFiledIp2.getText();
        String ip3 = textFiledIp3.getText();
        ip0 = ip0.isBlank()? "127":ip0;
        ip1 = ip1.isBlank()? "0":ip1;
        ip2 = ip2.isBlank()? "0":ip2;
        ip3 = ip3.isBlank()? "1":ip3;
        ipAddr = ip0+"."
                +ip1+"."
                +ip2+"."
                +ip3;
        System.out.println(ipAddr);
        //(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)
        if (ipAddr.matches("(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)")){
            //TODO：连接失败后恢复可点击
            System.out.println("match");
            gameFactory = new GameFactory(ipAddr);
        }
    }

    @FXML
    void onBtnHome(ActionEvent event) {
        App.getInstance().changeScene("StartPage");
    }

    private String ipAddr = "";

    @Override
    public void init() throws IOException {
        this.ipAddr = "";
        textFiledIp0.setTextFormatter(makeTextFormatter());
        textFiledIp1.setTextFormatter(makeTextFormatter());
        textFiledIp2.setTextFormatter(makeTextFormatter());
        textFiledIp3.setTextFormatter(makeTextFormatter());
        btnConnect.requestFocus();
    }
    private TextFormatter makeTextFormatter(){
        return new TextFormatter<String>((TextFormatter.Change c) -> {
            if (c.getControlNewText().matches("25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d")) {
                return c;
            } else
            if (c.getText().isEmpty()) {
                return c;
            }
            return null;
        });
    }
}
