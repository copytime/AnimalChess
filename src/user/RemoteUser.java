package user;

import chess.ChessState;
import chess.IChess;
import game.ActionPerformer;
import map.ICell;
import numberPropertyExtension.SimpleIntegerPropertyExt;

public class RemoteUser implements IUser{
    public IChess getSelectedChess() {
        return selectedChess;
    }

    public ICell getTargetCell() {
        return targetCell;
    }



    private static RemoteUser remoteUser = null;
    public static RemoteUser getInstance() {
        if (remoteUser == null){
            remoteUser = new RemoteUser();
        }
        return remoteUser;
    }


    private IChess selectedChess = null;
    private ICell targetCell = null;

    @Override
    public void cleanUserState() {
        this.selectedChess = null;
        this.targetCell = null;
    }

    @Override
    public void setActionTarget(IChess chess) {
        if (chess != null) {
            selectedChess = chess;
        }
    }

    @Override
    public void setActionTarget(ICell cell) {
        if (cell!=null) {
            targetCell = cell;
        }
    }


    @Override
    public void loseOneChess() {
    }
}
