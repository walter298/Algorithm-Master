package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Supplier;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.scene.layout.GridPane;

public class AlgorithmStepList {
    public class AlgorithmStep {
        public Supplier<Animation> scheduledAnimation;
        public Optional<VariableWatchWindow> variableState;
        
        public AlgorithmStep(Supplier<Animation> scheduledAnimation, VariableWatchWindow variableState) {
            this.scheduledAnimation = scheduledAnimation;
            this.variableState = Optional.of(variableState);
        }
        public AlgorithmStep(Supplier<Animation> scheduledAnimation) {
            this.scheduledAnimation = scheduledAnimation;
            this.variableState = Optional.empty();
        }
    }

    private ArrayList<AlgorithmStep> steps;
    private int stepIdx = 0;
    private Animation currentlyRunningAnimation = null;
    
    public AlgorithmStepList() {
        steps = new ArrayList<>();
    }

    void addStep(Supplier<Animation> scheduledAnimation, VariableWatchWindow variableState) {
        steps.add(new AlgorithmStep(scheduledAnimation, variableState));
    }

    void addAnimation(Supplier<Animation> scheduledAnimation) {
        steps.add(new AlgorithmStep(scheduledAnimation));
    }

    boolean isEmpty() {
        return stepIdx >= steps.size();
    }

    int getStepIndex() {
        return stepIdx;
    }

    Optional<GridPane> getVariableWindow() {
        if (steps.get(stepIdx).variableState.isEmpty()) {
            return Optional.empty();
        } 
        return Optional.of(steps.get(stepIdx).variableState.get().makeWindow());
    }

    //returns true if a new step was just reached
    public boolean run() {
        if (currentlyRunningAnimation == null) {
            currentlyRunningAnimation = steps.get(0).scheduledAnimation.get();
            currentlyRunningAnimation.play();
            return true;
        } else if (currentlyRunningAnimation.getStatus() == Status.STOPPED) { 
            stepIdx++; 
            if (stepIdx < steps.size()) { //if there are no more animations, stop
                currentlyRunningAnimation = steps.get(stepIdx).scheduledAnimation.get();
                currentlyRunningAnimation.play();
                return true;
            } 
        } 
        return false;
    }

    void togglePause() {
        var status = currentlyRunningAnimation.getStatus();
        if (status == Status.PAUSED) {
            currentlyRunningAnimation.play();
        } else if (status == Status.PAUSED) {
            currentlyRunningAnimation.pause();
        }
    }
}