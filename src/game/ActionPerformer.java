package game;

import chess.ChessState;
import chess.IChess;
import javafx.beans.property.SimpleBooleanProperty;
import map.ICell;
import user.IUser;

public class ActionPerformer {
    private static ActionPerformer instance;

    public static ActionPerformer getInstance() {
        if (instance == null) {
            instance = new ActionPerformer();
        }
        return instance;
    }

//    public boolean isLocalTurn = true;
    public SimpleBooleanProperty isLocalTurn = new SimpleBooleanProperty(true);

    //远程玩家
    public void remoteAction(IUser remoteUser) {
        System.out.println("perform remote action");
        if (remoteUser.getTargetCell() == null) {
            return;
        }
        if (remoteUser.getSelectedChess() == remoteUser.getTargetCell().getChess() && remoteUser.getSelectedChess().getPreChessState().equals(ChessState.HIDDEN)) {
            //翻转该棋子
            remoteUser.getSelectedChess().makeShow();
            cleanUserState(remoteUser);
            startLocalTurn();
        } else if (remoteUser.getSelectedChess().canMoveTo(remoteUser.getTargetCell())) {
            remoteUser.getSelectedChess().moveTo(remoteUser.getTargetCell());
            cleanUserState(remoteUser);
            startLocalTurn();
        }
    }



    //开始行动
    public void action(IUser user) {
        if (user.getTargetCell() == null) {
            return;
        }
        if (user.getSelectedChess() == user.getTargetCell().getChess() && user.getSelectedChess().getPreChessState().equals(ChessState.HIDDEN)) {
            //翻转该棋子
            endLocalTurn();
            user.getSelectedChess().makeShow();
            user.getSelectedChess().makeUnselect();
            sendOutAction(user);
            cleanUserState(user);
        } else if (user.getSelectedChess().canMoveTo(user.getTargetCell())) {
            //edit: 不需要判断是不是会"送死"，直接走就行了
//            IChess enemy = user.getTargetCell().getChess();
//            if (enemy == null) {
//                //无子占用，直接走
//                user.getSelectedChess().moveTo(user.getTargetCell());
//                endTurn(user);
//            } else {
//                RefereeResult result = user.getSelectedChess().canWinOrFlat(enemy);
//                if (!result.equals(RefereeResult.LOSE)) {
//                    //平手或者打赢
//                    endTurn(user);
//                    user.getSelectedChess().moveTo(user.getTargetCell(), result);
//                }
//            }
            //敌人不能是自己的棋子
            IChess enemy = user.getTargetCell().getChess();
            if (enemy == null || !enemy.isLocal()) {
                endLocalTurn();
                user.getSelectedChess().moveTo(user.getTargetCell());
                sendOutAction(user);
                cleanUserState(user);
            }
        }
    }

    private void sendOutAction(IUser user) {
        GameFactory.getInstance().getGameManager().sendOutAction(user);
    }

    private void cleanUserState(IUser user) {
        user.cleanUserState();
    }

    private void endLocalTurn() {
        this.isLocalTurn.setValue(false);
    }

    private void startLocalTurn() {
        this.isLocalTurn.setValue(true);
    }

}
