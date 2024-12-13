package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;

public class AlgorithmStepList {
    public class AlgorithmStep {
        public Supplier<Animation> scheduledAnimation;
        public HashMap<String, String> variableState;
        
        public AlgorithmStep(Supplier<Animation> scheduledAnimation, HashMap<String, String> variableState) {
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

    void addStep(Supplier<Animation> scheduledAnimation, HashMap<String, String> variableState) {
        steps.add(new AlgorithmStep(scheduledAnimation, variableState));
    }

    Map<String, String> getVariableState() {
        return Collections.unmodifiableMap(steps.get(stepIdx).variableState);
    }

    boolean isEmpty() {
        return stepIdx >= steps.size();
    }

    public boolean run() {
        if (currentlyRunningAnimation == null) {
            currentlyRunningAnimation = steps.get(0).scheduledAnimation.get();
            currentlyRunningAnimation.play();
        } else if (currentlyRunningAnimation.getStatus() == Status.STOPPED) { 
            stepIdx++; 
            if (stepIdx >= steps.size()) { //if there are no more animations, stop
                return false;
            } else {
                currentlyRunningAnimation = steps.get(stepIdx).scheduledAnimation.get();
                currentlyRunningAnimation.play();
            }
        }
        return true;
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