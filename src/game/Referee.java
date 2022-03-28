package game;

import chess.Chess;
import chess.ChessType;
import chess.IChess;

public class Referee {
    private static Referee instance;

    public static Referee getInstance() {
        if (instance == null) {
            instance = new Referee();
        }
        return instance;
    }

    public RefereeResult judge(IChess mine, IChess enemy) {
        if (enemy == null){
            return RefereeResult.WIN;
        }
        if (mine.getChessType().equals(ChessType.MOUSE) && enemy.getChessType().equals(ChessType.ELEPHANT)) {
            return RefereeResult.WIN;
        } else {
            int r = mine.getChessType().ordinal() - enemy.getChessType().ordinal();
            if (r == 0) {
                return RefereeResult.FLAT;
            }
            if (r < 0) {
                return RefereeResult.WIN;
            }
            else {
                return RefereeResult.LOSE;
            }
        }
    }
}
