package map;

import chess.IChess;

import java.util.List;

public interface ICell {
    public IChess getChess();


    @Deprecated//会导致互相调用死锁
    public void setChess(IChess chess);

    public void setChessForce(IChess chess);

    public Integer getCol();

    public Integer getRow();

    public List<String> getRelatedCellsId();

    public String getId();
}
