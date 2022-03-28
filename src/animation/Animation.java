package animation;

import javafx.animation.Timeline;
import javafx.util.Duration;

public abstract class Animation {
    protected Duration duration = Duration.seconds(2);
    public Timeline timeline = new Timeline();

    public void run() {
        this.timeline.play();
    }

    public void pause() {
        this.timeline.pause();
    }

    public  void stopAtHere() {
        this.timeline.stop();
    }

    public  void stopAtStart() {
        this.timeline.jumpTo(Duration.ZERO);
        this.timeline.stop();
    }
}
