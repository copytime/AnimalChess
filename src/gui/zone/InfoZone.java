package gui.zone;

import game.ActionPerformer;
import game.GameFactory;
import gui.IComponent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.IOException;

public class InfoZone implements IComponent {


    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button btnLose;

    @FXML
    void onBtnLose(ActionEvent event) {
        //TODO:只发送了，还没真显示输了
        GameFactory.getInstance().getGameManager().sendLose();
    }

    @FXML
    private Label labelInfo;

    private Thread countDownThread = null;

    @Override
    public void init() throws IOException {
        ActionPerformer.getInstance().isLocalTurn.addListener((change, from, to) -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    labelInfo.setText(to ? "该你行动了" : "对方思考中");
                    if (to) {
                        countDownThread = new Thread(new Runnable() {
                            private final double total = 100;
                            private double now = 100;

                            private double percent() {
                                return now / total;
                            }

                            @Override
                            public void run() {
                                while (now > 0) {
                                    now--;
                                    progressBar.setProgress(percent());
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                GameFactory.getInstance().getGameManager().sendLose();
                            }
                        });
                        countDownThread.start();
                    } else {
                        if (countDownThread != null) {
                            countDownThread.stop();
                        }
                        progressBar.setProgress(1);
                    }
                }
            });
        });

        labelInfo.setText(ActionPerformer.getInstance().isLocalTurn.get() ? "该你行动了" : "对方思考中");

    }
}
