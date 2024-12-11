package src;

import java.util.ArrayList;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class AnimationList {
    public interface LazyAnimation {
        Animation makeAnimation();
    }

    public ArrayList<Supplier<Animation>> unstartedAnimations;
    public ArrayList<Animation> startedAnimations;
    
    private boolean startedNextAnimation = false;
    private int animationIdx = 0;

    public AnimationList() {
        unstartedAnimations = new ArrayList<Supplier<Animation>>();
        startedAnimations   = new ArrayList<Animation>();
    }

    void run() {
        System.out.println("Creating background task");
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(50), 
            new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (startedAnimations.isEmpty() && unstartedAnimations.isEmpty()) {
                    return;
                }

                if (startedAnimations.isEmpty() || startedAnimations.get(0).getStatus() == Status.STOPPED) {
                    startedAnimations.clear();
                    startedAnimations.add(unstartedAnimations.get(0).get());
                    startedAnimations.get(0).play();
                    if (!unstartedAnimations.isEmpty()) {
                        unstartedAnimations.remove(0);
                    }
                }

                // if (animationIdx >= animations.size()) {
                //     return;
                // } 
                // if (animations.get(animationIdx).get().getStatus() == Status.STOPPED) {
                //     if (!startedNextAnimation) {
                //         animations.get(animationIdx).get().play();
                //         startedNextAnimation = true;
                //     } else {
                //         animationIdx++;
                //         startedNextAnimation = false;
                //     }
                // }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
