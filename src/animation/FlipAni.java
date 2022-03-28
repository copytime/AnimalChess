package animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.util.Duration;
import net.ICallBack;


public class FlipAni extends animation.Animation {
    private ICallBack<FlipAni> callBack;

    public FlipAni(Node node, Duration duration, ICallBack<FlipAni> callBack) {
        this.duration = duration;
        this.callBack = callBack;

        KeyValue x0 = new KeyValue(node.translateXProperty(), 0);
        KeyValue y0 = new KeyValue(node.translateYProperty(), 0);
        KeyValue r0 = new KeyValue(node.rotateProperty(), 0);
        KeyValue sx0 = new KeyValue(node.scaleXProperty(), 1);

        KeyValue x = new KeyValue(node.translateXProperty(), 50);
        KeyValue y = new KeyValue(node.translateYProperty(), 50);
        node.setRotationAxis(new Point3D(0, 0, 1));
        KeyValue r = new KeyValue(node.rotateProperty(), 20);
        KeyValue sx = new KeyValue(node.scaleXProperty(), 0);


        KeyFrame keyFrame0 = new KeyFrame(Duration.ZERO, "end", x0, y0,r0,sx0);
        KeyFrame keyFrame = new KeyFrame(this.duration, "end", x, y,r,sx);
        timeline.getKeyFrames().add(keyFrame0);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(actionEvent -> {
//            timeline.setRate(-1.0);
//            timeline.jumpTo(timeline.totalDurationProperty().get());
//            timeline.setOnFinished(null);           //Hint:  因为没有一开始的关键帧，所以导致没法回退播放
//            timeline.play();
            callBack.invoke(this);
        });
        timeline.setRate(1.0);
//        timeline.jumpTo(duration);

        timeline.currentTimeProperty().addListener((observableValue, duration1, t1) -> {
//            if (flag){
//                if (t1.lessThanOrEqualTo(duration)) {
//                    System.out.println("mid");
//                    flag = false;
//                    timeline.pause();
//                }
//            }
        });
    }

    public Duration getDur() {
        return this.duration;
    }

}
