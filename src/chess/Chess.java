package chess;

import animation.FlipAni;
import animation.MoveAni;
import animation.ScaleAni;
import game.ActionPerformer;
import game.Referee;
import game.RefereeResult;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import map.ICell;
import user.LocalUser;

public class Chess extends ImageView implements IChess {
    public ChessState chessState = ChessState.HIDDEN;
    private ChessState preChessState = ChessState.HIDDEN;
    private ChessType chessType;
    private ICell cell = null;
    private boolean isLocal = false;
    private Image backImage = null;
    private Image frontImage = null;
    private FlipAni flipAni = null;
    private ScaleAni scaleAni = null;
    private MoveAni moveAni = null;
    private boolean isServer = true;

    public Chess(ChessType chessType, boolean isLocal, boolean isServer) {
        this.chessType = chessType;

        this.isLocal = isLocal;
        this.isServer = isServer;
        this.initImage();
        this.initAction();
        this.initAnima();
    }

    private void initAnima() {
        this.flipAni = new FlipAni(this, Duration.seconds(0.2), ani -> {
            ani.timeline.setRate(-1.0);
            ani.timeline.jumpTo(ani.timeline.totalDurationProperty().get());
            ani.timeline.setOnFinished(null);           //Hint:  因为没有一开始的关键帧，所以导致没法回退播放
            setImage(frontImage);
            ani.timeline.play();
        });

        this.scaleAni = new ScaleAni(this, Duration.seconds(0.4));

    }

    private void initAction() {
        this.setOnMouseClicked(mouseEvent -> {
            LocalUser.getInstance().setActionTarget(this);
        });
    }

    private void initImage() {
        this.backImage = new Image("BACK.png");
        this.frontImage = new Image(this.chessType.name() + "_" + (isServer ? "L" : "R") + ".png");


        this.setImage(backImage);


        this.setPreserveRatio(true);
        this.setFitWidth(140);
        this.setFitHeight(140);

        Rectangle rectangle = new Rectangle(this.prefWidth(-1), this.prefHeight(-1));

        rectangle.setArcWidth(20);
        rectangle.setArcHeight(20);
        this.setClip(rectangle);
    }


    @Override
    public ICell getCell() {
        return cell;
    }

    @Override
    public void setCell(ICell cell) {
        //先离开原来的格子
        if (this.cell != null)
            this.cell.setChessForce(null);
        this.cell = cell;
        if (cell != null && cell.getChess() != this) {
            cell.setChessForce(this);
            GridPane.setConstraints(this, cell.getCol(), cell.getRow());
        }
        if (cell == null) {
            //丢到外面
//            this.cell = null;
        }
    }

    @Override
    public ChessType getChessType() {
        return this.chessType;
    }

    @Override
    public void setChessState(ChessState chessState) {
        this.preChessState = this.chessState;
        this.chessState = chessState;
    }

    @Override
    public ChessState getChessState() {
        return this.chessState;
    }

    @Override
    public ChessState getPreChessState() {
        return this.preChessState;
    }

    @Override
    public void makeShow() {
//        setChessState(ChessState.SHOWED);
        this.chessState = ChessState.SHOWED;
        this.preChessState = ChessState.SHOWED;
        flipAni.run();
    }

    @Override
    public void makeSelect() {
        setChessState(ChessState.SELECTED);
        scaleAni.run();
    }

    @Override
    public void makeUnselect() {
        //直接修改而不是通过 setChessState，不用记录上一次的状态
        this.chessState = this.preChessState;
        scaleAni.stopAtStart();
    }

    @Override
    public void die() {
        this.setVisible(false);
        this.setCell(null);
        if (isLocal) {
            LocalUser.getInstance().loseOneChess();
        }
    }


    @Override
    public boolean canMoveTo(ICell target) {
        return this.cell.getRelatedCellsId().contains(target.getId());
    }


    @Override
    public void moveTo(ICell target) {
        this.makeUnselect();
        this.moveAni = new MoveAni(this, (Node) target, Duration.seconds(0.3), ani -> {
            this.moveAni = null;
            this.setTranslateY(0);
            this.setTranslateX(0);
            //判断情况
            RefereeResult result = Referee.getInstance().judge(this, target.getChess());
            switch (result) {
                case WIN:
                    if (target.getChess() != null)
                        target.getChess().die();
                    this.setCell(target);
                    break;
                case LOSE:
                    this.die();
                    break;
                case FLAT:
                    target.getChess().die();
                    this.die();
                    break;
            }
        });
        moveAni.run();
    }

    @Override
    public boolean isLocal() {
        return isLocal;
    }

    @Override
    public boolean isServer() {
        return isServer;
    }
}
