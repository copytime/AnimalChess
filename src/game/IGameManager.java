package game;

import chess.IChess;
import map.CellBase;
import user.IUser;

import java.util.List;

public interface IGameManager {
    public List<CellBase> getCells();

    public List<IChess> getChessList();

    public List<Integer> getChessInCellOrder();

    void sendOutAction(IUser user);

    void sendLose();

    void sendCancel();

    void localUserLose();
}
