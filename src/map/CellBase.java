package map;

import chess.IChess;
import game.ActionPerformer;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import user.LocalUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CellBase extends Rectangle implements ICell {
    private GridPane grid = null;
    private List<String> relatedCellsId = null;
    private IChess chessInThisCell;

    public CellBase(String id, GridPane grid) {
        new CellBase(id, grid, 149, 149);
    }

    public CellBase(int id, GridPane grid, Paint fill) {
        new CellBase(id, grid, 149, 149, fill);
    }

    public CellBase(String id, GridPane grid, double width, double height) {
        new CellBase(Integer.parseInt(id), grid, 149, 149, Color.GHOSTWHITE);
    }

    public CellBase(int id, GridPane grid, double width, double height, Paint fill) {
        super(width, height, fill);
        this.setId(String.valueOf(id));
        this.grid = grid;

        this.initAction();
    }

    private void initAction() {
        this.setOnMouseClicked(mouseEvent -> {
            LocalUser.getInstance().setActionTarget(this);
        });
    }

    @Override
    public IChess getChess() {
        return chessInThisCell;
    }

    @Override
    public void setChess(IChess chess) {
        // 先拿走现有的棋子
        if (this.chessInThisCell != null)
            this.chessInThisCell.setCell(null);
        this.chessInThisCell = chess;
        if (chess != null && chess.getCell() != this) {
            chess.setCell(this);
            GridPane.setConstraints((Node) chess, getCol(), getRow());
            System.out.println("chess设置在" + getCol() + " " + getRow());
        }
    }

    @Override
    public void setChessForce(IChess chess) {
        this.chessInThisCell = chess;
    }

    @Override
    public Integer getCol() {
        return Optional.ofNullable(GridPane.getColumnIndex(this)).orElse(0);
    }

    @Override
    public Integer getRow() {
        return Optional.ofNullable(GridPane.getRowIndex(this)).orElse(0);
    }


    public List<String> getRelatedCellsId() {

        if (this.relatedCellsId == null) {
            this.relatedCellsId = new ArrayList<>();
            int me = Integer.parseInt(this.getId());
            if (me % 4 != 0) {
                //有左端
                this.relatedCellsId.add(String.valueOf(me - 1));
            }
            if ((me + 1) % 4 != 0) {
                //有右端
                this.relatedCellsId.add(String.valueOf(me + 1));
            }
            if (me - grid.getColumnCount() >= 0) {
                this.relatedCellsId.add(String.valueOf(me - grid.getColumnCount()));
            }
            if (me + grid.getColumnCount() < (grid.getRowCount() * grid.getRowCount())) {
                this.relatedCellsId.add(String.valueOf(me + grid.getColumnCount()));
            }
        }
        return relatedCellsId;
    }
}
