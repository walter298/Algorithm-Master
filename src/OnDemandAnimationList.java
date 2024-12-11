package src;

import java.util.ArrayList;
import java.util.function.Supplier;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class OnDemandAnimationList {
    private ArrayList<Supplier<Animation>> unstartedAnimations;
    private ArrayList<Animation> startedAnimations;
    
    public OnDemandAnimationList() {
        unstartedAnimations = new ArrayList<Supplier<Animation>>();
        startedAnimations   = new ArrayList<Animation>();
    }

    public void schedule(Supplier<Animation> animation) {
        unstartedAnimations.add(animation);
    }

    public void run() {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(50), 
            new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (startedAnimations.isEmpty() || startedAnimations.get(0).getStatus() == Status.STOPPED) {
                    startedAnimations.clear();
                    if (!unstartedAnimations.isEmpty()) {
                        startedAnimations.add(unstartedAnimations.get(0).get());
                        unstartedAnimations.remove(0);
                        startedAnimations.get(0).play();
                    } 
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
