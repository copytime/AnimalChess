package animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class ScaleAni extends Animation{
    public ScaleAni(Node node, Duration duration){
        this.duration = duration;
        KeyValue x = new KeyValue(node.scaleXProperty(),1.2);
        KeyValue y = new KeyValue(node.scaleYProperty(),1.2);
        KeyFrame keyFrame = new KeyFrame(this.duration, "end",x,y);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().add(keyFrame);
    }
}
