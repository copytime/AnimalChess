package game;

import chess.Chess;
import chess.ChessType;
import chess.IChess;
import cmd.CmdExecutor;
import cmd.CmdPackager;
import gui.App;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import map.CellBase;
import net.ClientSideThread;
import user.IUser;
import user.LocalUser;
import user.RemoteUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameManagerClient implements IGameManager {
    private static GameManagerClient gameManagerClient = null;

    public static GameManagerClient getNewInstance(String ipAddr) {
        gameManagerClient = null;
        return getInstance(ipAddr);
    }

    public static GameManagerClient getInstance(String ipAddr) {
        if (gameManagerClient == null) {
            gameManagerClient = new GameManagerClient(ipAddr);
        }
        return gameManagerClient;
    }

    public List<CellBase> cells = new ArrayList<>();
    public List<IChess> chessList = new ArrayList<>();
    public List<Integer> chessInCellOrder = new ArrayList<>(16);

    public ClientSideThread clientSideThread = null;
    public CmdExecutor cmdExecutor = null;

    @Override
    public List<CellBase> getCells() {
        return cells;
    }

    @Override
    public List<IChess> getChessList() {
        return chessList;
    }

    @Override
    public List<Integer> getChessInCellOrder() {
        return chessInCellOrder;
    }

    public GameManagerClient(String ipAddr) {
        initExecutor();
        genChess(chessList);
        clientSideThread = new ClientSideThread(ipAddr, "2333",
                msg -> {
                    if (cmdExecutor != null) {
                        cmdExecutor.execute(msg);
                    }
                }, clientSideThread -> {
            System.out.println("connected");
        }, filed -> {
            App.getInstance().showMessage(Alert.AlertType.ERROR,
                    "连接错误", "尝试连接到指定ip失败",
                    "请检查ip地址后重新连接");
        });
        Thread server = new Thread(clientSideThread);
        LocalUser.getInstance().aliveChessCount.addListener((change, from, to) -> {
            if (to.equals(0)) {
                sendLose();
            }
        });
        server.start();
    }


    private void initExecutor() {
        this.cmdExecutor = new CmdExecutor(
                cmdInit -> {
                    //cmdInit 有16个，前8个是服务器的棋子，后8个是客户端的棋子
                    for (int i = 0; i < cmdInit.length; i++) {
                        chessInCellOrder.add(Integer.parseInt(cmdInit[i]));
                    }
                    ActionPerformer.getInstance().isLocalTurn.setValue(false);
                    App.getInstance().changeScene("GamePage");
                }
                , cmdMove -> {
            //cmdMove 有两个，0是要走的，1是目标
            System.out.println("client:" + cmdMove[0] + "," + cmdMove[1]);
            RemoteUser.getInstance().setActionTarget(cells.get(Integer.parseInt(cmdMove[0])).getChess());
            RemoteUser.getInstance().setActionTarget(cells.get(Integer.parseInt(cmdMove[1])));
            ActionPerformer.getInstance().remoteAction(RemoteUser.getInstance());
        }, cmdLose -> {
            //cmdLose 一定是null，表示输了
            if (cmdLose == null) {
                //禁止本地玩家再行动了
                ActionPerformer.getInstance().isLocalTurn.setValue(false);
                App.getInstance().showMessage(Alert.AlertType.INFORMATION, "你赢了",
                        "恭喜你赢得了比赛", "点击回到主菜单", btn -> {
                            if (btn.isPresent() && btn.get() == ButtonType.OK) {
                                App.getInstance().changeScene("StartPage");
                            }
                        });
            }
        });
    }


    @Override
    public void sendOutAction(IUser user) {
        if (clientSideThread == null)
            return;
        var selectedCell = user.getSelectedChess().getCell();
        var targetCell = user.getTargetCell();
        if (selectedCell == null) {
            return;
        }
        int self = cells.indexOf(selectedCell);
        int tgt = cells.indexOf(targetCell);
        List<String> params = new ArrayList<>();
        if (self != -1 && tgt != -1) {
            params.add(Integer.toString(self));
            params.add(Integer.toString(tgt));
        }
        clientSideThread.send(
                CmdPackager.getInstance().pack("moveto", params.toArray(new String[0]))
        );
    }

    @Override
    public void sendLose() {
        clientSideThread.send(
                CmdPackager.getInstance().pack("lose", null)
        );
        localUserLose();
    }

    @Override
    public void sendCancel() {
        //客户端不需要取消连接
    }

    @Override
    public void localUserLose() {
        //禁止本地玩家再行动了
        ActionPerformer.getInstance().isLocalTurn.setValue(false);
        App.getInstance().showMessage(Alert.AlertType.INFORMATION, "你输了",
                "很遗憾你输了比赛", "点击回到主菜单", btn -> {
                    if (btn.isPresent() && btn.get() == ButtonType.OK) {
                        App.getInstance().changeScene("StartPage");
                    }
                });
    }


    private void genChess(List<IChess> chessList) {
        chessList.add(new Chess(ChessType.ELEPHANT, false, true));
        chessList.add(new Chess(ChessType.LION, false, true));
        chessList.add(new Chess(ChessType.TIGER, false, true));
        chessList.add(new Chess(ChessType.LEOPARD, false, true));
        chessList.add(new Chess(ChessType.WOLF, false, true));
        chessList.add(new Chess(ChessType.DOG, false, true));
        chessList.add(new Chess(ChessType.CAT, false, true));
        chessList.add(new Chess(ChessType.MOUSE, false, true));
        chessList.add(new Chess(ChessType.ELEPHANT, true, false));
        chessList.add(new Chess(ChessType.LION, true, false));
        chessList.add(new Chess(ChessType.TIGER, true, false));
        chessList.add(new Chess(ChessType.LEOPARD, true, false));
        chessList.add(new Chess(ChessType.WOLF, true, false));
        chessList.add(new Chess(ChessType.DOG, true, false));
        chessList.add(new Chess(ChessType.CAT, true, false));
        chessList.add(new Chess(ChessType.MOUSE, true, false));

    }
}
