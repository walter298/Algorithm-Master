package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.scene.layout.GridPane;

public class AlgorithmStepList {
    public class AlgorithmStep {
        public Supplier<Animation> scheduledAnimation;
        public VariableWatchWindow variableState;
        
        public AlgorithmStep(Supplier<Animation> scheduledAnimation, VariableWatchWindow variableState) {
            this.scheduledAnimation = scheduledAnimation;
            this.variableState = variableState;
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

    boolean isEmpty() {
        return stepIdx >= steps.size();
    }

    int getStepIndex() {
        return stepIdx;
    }

    GridPane getVariableWindow() {
        return steps.get(stepIdx).variableState.makeWindow();
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