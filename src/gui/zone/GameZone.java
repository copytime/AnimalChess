package gui.zone;

import chess.Chess;
import chess.ChessType;
import chess.IChess;
import game.GameFactory;
import gui.IComponent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import map.CellBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameZone implements IComponent {

    @FXML
    private GridPane zone;

    private List<CellBase> cells = null;
    private List<IChess> chessList = null;
    private List<Integer> chessInCellOrder = null;


    @Override
    public void init() throws IOException {

        cells = GameFactory.getInstance().getGameManager().getCells();
        chessList = GameFactory.getInstance().getGameManager().getChessList();
        chessInCellOrder = GameFactory.getInstance().getGameManager().getChessInCellOrder();

        //初始化格子
        int id = 0;
        for (int i = 0; i < zone.getRowCount(); i++) {
            for (int j = 0; j < zone.getColumnCount(); j++) {
                CellBase cellBase = new CellBase(id, zone, 149, 149, Color.GHOSTWHITE);
                id++;
                cells.add(cellBase);
                zone.add(cellBase, j, i);
                GridPane.setHalignment(cellBase, HPos.CENTER);
                GridPane.setValignment(cellBase, VPos.CENTER);
            }
        }


        //填充棋子
        GameFactory.getInstance().getGameManager().getChessList().forEach(iChess -> {
            zone.getChildren().add((Node) iChess);
            GridPane.setHalignment((Node) iChess, HPos.CENTER);
            GridPane.setValignment((Node) iChess, VPos.CENTER);
        });
        for (int i = 0; i < chessInCellOrder.size(); i++) {
            chessList.get(i).setCell(cells.get(chessInCellOrder.get(i)));
        }
    }
}
