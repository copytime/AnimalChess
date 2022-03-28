package user;

import chess.ChessState;
import chess.IChess;
import game.ActionPerformer;
import map.ICell;
import numberPropertyExtension.SimpleIntegerPropertyExt;

public class LocalUser implements IUser {
    public IChess getSelectedChess() {
        return selectedChess;
    }

    public ICell getTargetCell() {
        return targetCell;
    }


    private static LocalUser localUser = null;

    public static LocalUser getInstance() {
        if (localUser == null) {
            localUser = new LocalUser(8);
        }
        return localUser;
    }


    public SimpleIntegerPropertyExt aliveChessCount = new SimpleIntegerPropertyExt(8);

    private IChess selectedChess = null;
    private ICell targetCell = null;
    private boolean firstSelected = true;

    private LocalUser(int aliveChessCount) {
        this.aliveChessCount.setValue(aliveChessCount);
    }

    @Override
    public void cleanUserState() {
        this.selectedChess = null;
        this.firstSelected = true;
        this.targetCell = null;
    }

    @Override
    public void setActionTarget(IChess chess) {
        if (!ActionPerformer.getInstance().isLocalTurn.get()) return;
        //根据棋子状态去判断
        //如果是hidden状态，那么任何人都可以去翻开这个棋子
        //如果是show状态，那么只有本地玩家可以操作这个棋子
        if (firstSelected) {
            if (chess.getChessState().equals(ChessState.HIDDEN)) {
                this.selectedChess = chess;
                chess.makeSelect();
                firstSelected = false;
            } else {
                if (chess.isLocal()) {
                    //是本地玩家的棋子
                    this.selectedChess = chess;
                    chess.makeSelect();
                    firstSelected = false;
                }
            }
        } else {
            //是第二次选了
            if (chess.getChessState().equals(ChessState.HIDDEN)) {
                if (chess == selectedChess) {
                    targetCell = selectedChess.getCell();
                }
                //玩家想换棋子
                else {
                    this.selectedChess.makeUnselect();
                    this.selectedChess = chess;
                    chess.makeSelect();
                }
            } else {
                if (chess.isLocal()) {
                    //本地玩家的棋子
                    if (chess == selectedChess) {
                        targetCell = selectedChess.getCell();
                    }
                    //玩家想换棋子
                    else {
                        this.selectedChess.makeUnselect();
                        this.selectedChess = chess;
                        chess.makeSelect();
                    }
                } else {
                    //不是玩家的棋子
                    if (chess == selectedChess){
                        //允许玩家翻敌人的棋子
                        targetCell = chess.getCell();
                    }
                    //玩家想要攻击它
                    //但是还没翻开的棋子不能攻击
                    if (!selectedChess.getPreChessState().equals(ChessState.HIDDEN)) {

                        if (!chess.getChessState().equals(ChessState.HIDDEN)) {
                            targetCell = chess.getCell();
                        }
                    }
                }
            }
            action();
        }
    }

    @Override
    public void setActionTarget(ICell cell) {
        if (!ActionPerformer.getInstance().isLocalTurn.get()) return;
        if (!firstSelected) {
            targetCell = cell;
            action();
        }
    }

    //开始行动
    private void action() {
        ActionPerformer.getInstance().action(this);
    }

    @Override
    public void loseOneChess() {
        if (this.aliveChessCount.greaterThan(0).get())
            this.aliveChessCount.subtractSelf(1);
    }
}
