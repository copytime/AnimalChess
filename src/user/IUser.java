package user;

import chess.Chess;
import chess.IChess;
import map.ICell;

public interface IUser {

    //用来选择
    void setActionTarget(IChess chess);

    void setActionTarget(ICell cell);

    IChess getSelectedChess();

    ICell getTargetCell();

    void cleanUserState();

    void loseOneChess();

}
