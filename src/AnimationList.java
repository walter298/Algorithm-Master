package src;

import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class AnimationList {
    public ArrayList<Animation> animations;
    private boolean startedNextAnimation = false;
    private int animationIdx = 0;

    public AnimationList() {
        animations = new ArrayList<Animation>();
    }

    void run() {
        animations.get(0).play();
        
        System.out.println("Creating background task");
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(50), 
            new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (animationIdx >= animations.size()) {
                    return;
                } 
                if (animations.get(animationIdx).getStatus() == Status.STOPPED) {
                    if (!startedNextAnimation) {
                        animations.get(animationIdx).play();
                        startedNextAnimation = true;
                    } else {
                        animationIdx++;
                        startedNextAnimation = false;
                    }
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
