package chess;

import game.RefereeResult;
import map.ICell;
import user.IUser;

public interface IChess {
    public ICell getCell();

    public void setCell(ICell cell);

    public ChessType getChessType();
    public void setChessState(ChessState chessState);
    public ChessState getChessState();
    public ChessState getPreChessState();

    public void makeShow();

    public void makeSelect();
    public void makeUnselect();

    public void die();



    public boolean canMoveTo(ICell target);



    public void moveTo(ICell target);

    public boolean isLocal();
    public boolean isServer();
}
