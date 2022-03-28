package game;

import chess.Chess;
import chess.ChessType;
import chess.IChess;
import cmd.CmdExecutor;
import cmd.CmdPackager;
import gui.App;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import map.CellBase;
import map.ICell;
import net.ServerSideThread;
import user.IUser;
import user.LocalUser;
import user.RemoteUser;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GameManagerServer implements IGameManager {
    private static GameManagerServer gameManagerServer = null;

    public static GameManagerServer getNewInstance() {
        gameManagerServer = null;
        return getInstance();
    }

    public static GameManagerServer getInstance() {
        if (gameManagerServer == null) {
            gameManagerServer = new GameManagerServer();
        }
        return gameManagerServer;
    }

    public List<CellBase> cells = new ArrayList<>();
    public List<IChess> chessList = new ArrayList<>();
    public List<Integer> chessInCellOrder = new ArrayList<>(16);

    public List<CellBase> getCells() {
        return cells;
    }

    public List<IChess> getChessList() {
        return chessList;
    }

    public List<Integer> getChessInCellOrder() {
        return chessInCellOrder;
    }

    public ServerSideThread serverSideThread = null;
    public CmdExecutor cmdExecutor = null;

    private Thread server;

    public GameManagerServer() {
        //生成棋子
        genChess(chessList);
        //生成顺序
        for (int i = 0; i < 16; i++) {
            chessInCellOrder.add(i);
        }
        Random random = new Random();
        //打乱棋子顺序
        for (int i = 0; i < chessInCellOrder.size(); i++) {
            Collections.swap(chessInCellOrder, i, random.nextInt(chessInCellOrder.size()));
        }

        initExecutor();
        serverSideThread = new ServerSideThread("2333"
                , msg -> {
            if (cmdExecutor != null) {
                cmdExecutor.execute(msg);
            }
        }, serverSideThread -> {
            //发送初始化信息并且切换到游戏场景
            serverSideThread.send(CmdPackager.getInstance().pack(
                    "init",
                    chessInCellOrder.stream().map(integer -> Integer.toString(integer)).collect(Collectors.toList()).toArray(new String[0])
            ));
            App.getInstance().changeScene("GamePage");
        });
        ActionPerformer.getInstance().isLocalTurn.setValue(true);
        server = new Thread(serverSideThread);

        //判断本地玩家是否输了
        LocalUser.getInstance().aliveChessCount.addListener((change, from, to) -> {
            if (to.equals(0)) {
                sendLose();
            }
        });

        server.start();
    }


    private void initExecutor() {
        this.cmdExecutor = new CmdExecutor(
                null
                , cmdMove -> {
            //cmdMove 有两个，0是要走的，1是目标
            RemoteUser.getInstance().setActionTarget(cells.get(Integer.parseInt(cmdMove[0])).getChess());
            RemoteUser.getInstance().setActionTarget(cells.get(Integer.parseInt(cmdMove[1])));
            ActionPerformer.getInstance().remoteAction(RemoteUser.getInstance());
        }, cmdLose -> {
            //cmdLose 一定是null，表示输了
            if (cmdLose == null) {
                //禁止本地玩家再行动了
                ActionPerformer.getInstance().isLocalTurn.setValue(false);
                App.getInstance().showMessage(Alert.AlertType.INFORMATION,"你赢了",
                        "恭喜你赢得了比赛","点击回到主菜单",btn->{
                            if (btn.isPresent() && btn.get() == ButtonType.OK) {
                                serverSideThread.close();
                                App.getInstance().changeScene("StartPage");
                            }
                        });
            }
        });
    }

    @Override
    public void sendOutAction(IUser user) {
        if (serverSideThread == null)
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
        serverSideThread.send(
                CmdPackager.getInstance().pack("moveto", params.toArray(new String[0]))
        );
    }

    @Override
    public void sendLose() {
        serverSideThread.send(
                CmdPackager.getInstance().pack("lose", null)
        );
        localUserLose();
    }

    @Override
    public void localUserLose() {
        //禁止本地玩家再行动了
        ActionPerformer.getInstance().isLocalTurn.setValue(false);
        App.getInstance().showMessage(Alert.AlertType.INFORMATION,"你输了",
                "很遗憾你输了比赛","点击回到主菜单",btn->{
                    if (btn.isPresent() && btn.get() == ButtonType.OK) {
                        serverSideThread.close();
                        App.getInstance().changeScene("StartPage");
                    }
                });
    }

    @Override
    public void sendCancel() {
        try {
            serverSideThread.cancel();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void genChess(List<IChess> chessList) {
        chessList.add(new Chess(ChessType.ELEPHANT, true, true));
        chessList.add(new Chess(ChessType.LION, true, true));
        chessList.add(new Chess(ChessType.TIGER, true, true));
        chessList.add(new Chess(ChessType.LEOPARD, true, true));
        chessList.add(new Chess(ChessType.WOLF, true, true));
        chessList.add(new Chess(ChessType.DOG, true, true));
        chessList.add(new Chess(ChessType.CAT, true, true));
        chessList.add(new Chess(ChessType.MOUSE, true, true));
        chessList.add(new Chess(ChessType.ELEPHANT, false, false));
        chessList.add(new Chess(ChessType.LION, false, false));
        chessList.add(new Chess(ChessType.TIGER, false, false));
        chessList.add(new Chess(ChessType.LEOPARD, false, false));
        chessList.add(new Chess(ChessType.WOLF, false, false));
        chessList.add(new Chess(ChessType.DOG, false, false));
        chessList.add(new Chess(ChessType.CAT, false, false));
        chessList.add(new Chess(ChessType.MOUSE, false, false));

    }
}
