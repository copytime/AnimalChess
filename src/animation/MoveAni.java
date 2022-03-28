package animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;
import net.ICallBack;

public class MoveAni extends Animation{
    private ICallBack<Animation> callBack = null;
    private double oldViewOrder = 0;
    public MoveAni(Node node, Node targetNode, Duration duration, ICallBack<Animation> callBack){
        this.duration = duration;
        this.callBack = callBack;
        this.oldViewOrder = node.getViewOrder();
        node.setViewOrder(-2);
        var offsetX = targetNode.getBoundsInParent().getCenterX() - node.getBoundsInParent().getCenterX();
        var offsetY = targetNode.getBoundsInParent().getCenterY() - node.getBoundsInParent().getCenterY();
//        System.out.println(node.getBoundsInParent().getCenterX() + "   " + targetNode.getBoundsInParent().getCenterX());
//        System.out.println(node.getBoundsInParent().getCenterY() + "   " + targetNode.getBoundsInParent().getCenterY());
        KeyValue x0 = new KeyValue(node.translateXProperty(), 0);
        KeyValue y0 = new KeyValue(node.translateYProperty(), 0);
        KeyFrame keyFrame0 = new KeyFrame(Duration.ZERO, "moveStart",x0,y0);

        KeyValue x = new KeyValue(node.translateXProperty(), offsetX);
        KeyValue y = new KeyValue(node.translateYProperty(), offsetY);
        KeyFrame keyFrame = new KeyFrame(this.duration, "moveEnd",x,y);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame0,keyFrame);
        timeline.setOnFinished(actionEvent -> {
            node.setViewOrder(oldViewOrder);
            callBack.invoke(this);
        });
    }
}
