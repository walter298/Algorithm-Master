package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.scene.layout.GridPane;

public class AlgorithmStepList {
    public class AlgorithmStep {
        public Optional<Supplier<Animation>> scheduledAnimation = Optional.empty();
        public Optional<VariableWatchWindow> variableState      = Optional.empty();
        public final int firstLine;
        public final int secondLine;
        
        public AlgorithmStep(int firstLine, int secondLine) {
            this.firstLine = firstLine;
            this.secondLine = secondLine;
        }

        public AlgorithmStep(Supplier<Animation> scheduledAnimation, VariableWatchWindow variableState, int firstLine, int secondLine) {
            this(firstLine, secondLine);
            this.scheduledAnimation = Optional.of(scheduledAnimation);
            this.variableState = Optional.of(variableState);
        }
        public AlgorithmStep(Supplier<Animation> scheduledAnimation, int firstLine, int secondLine) {
            this(firstLine, secondLine);
            this.scheduledAnimation = Optional.of(scheduledAnimation);
            this.variableState = Optional.empty();
        }
        public AlgorithmStep(VariableWatchWindow variableState, int firstLine, int secondLine) {
            this(firstLine, secondLine);
            this.scheduledAnimation = Optional.empty();
            this.variableState = Optional.of(variableState);
        }
    }

    private ArrayList<AlgorithmStep> steps;
    private int stepIdx = 0;
    private boolean hasHitbreakPoint = false;
    private boolean isContinuedPastBreakpoint = false;
    private boolean hasStartedCurrentAnimation = false;
    private boolean isNewVariableWindowReady = false;
    private HashMap<Integer, Boolean> activeBreakpoints;
    private Animation currentlyRunningAnimation = null;
    private HighlightableCodeArea sourceCodeViewer;

    public AlgorithmStepList(HighlightableCodeArea sourceCodeViewer) {
        this.sourceCodeViewer = sourceCodeViewer;
        steps = new ArrayList<>();
        activeBreakpoints = new HashMap<>();
    }

    public void addStep(Supplier<Animation> scheduledAnimation, VariableWatchWindow variableState, int firstLine, int secondLine) {
        steps.add(new AlgorithmStep(scheduledAnimation, variableState, firstLine, secondLine));
    }

    public void addStep(VariableWatchWindow variableState, int firstLine, int secondLine) {
        steps.add(new AlgorithmStep(variableState, firstLine, secondLine));
    }

    public void addAnimation(Supplier<Animation> scheduledAnimation, int firstLine, int secondLine) {
        steps.add(new AlgorithmStep(scheduledAnimation, firstLine, secondLine));
    }

    public void addBreakpoint(int firstLine, int secondLine) {
        steps.add(new AlgorithmStep(firstLine, secondLine));
    }

    public void removeBreakpoint(int breakpointID) {
        activeBreakpoints.put(breakpointID, false);
    }

    public boolean isEmpty() {
        return stepIdx >= steps.size();
    }

    public int getStepIndex() {
        return stepIdx;
    }

    public Optional<GridPane> getVariableWindow() {
        if (!(stepIdx < steps.size())) {
            return Optional.empty();
        }
        var currStep = steps.get(stepIdx);
        if (currStep.variableState.isEmpty()) {
            return Optional.empty();
        } 
        return Optional.of(currStep.variableState.get().makeWindow());
    }

    public void movePastBreakpoint() {
        isContinuedPastBreakpoint = true;
    }

    public boolean hasHitBreakpoint() {
        return hasHitbreakPoint;
    }

    private void moveToNextStep() {
        stepIdx++;
        hasHitbreakPoint = false;
        isContinuedPastBreakpoint = false;
        hasStartedCurrentAnimation = false;
    }

    //returns true if a new step was just reached
    public boolean run() {
        //check for out of bounds
        if (!(stepIdx < steps.size())) {
            return false;
        }
        
        var currStep = steps.get(stepIdx);
        
        //if the current step has a breakpoint, then pause until the user manually continues past it
        // if (hasHitbreakPoint && !isContinuedPastBreakpoint) {
        //     return false;
        // } 
        // if (!hasHitbreakPoint && sourceCodeViewer.hasBreakpoint(currStep.firstLine, currStep.secondLine)) {
        //     hasHitbreakPoint = true;
        //     return false;
        // }

        //if there is no animation nor breakpoint, move to the next step
        // if (currStep.scheduledAnimation.isEmpty()) {
        //     moveToNextStep();
        //     return true;
        // } 

        if (currStep.scheduledAnimation.isPresent()) {
            //if we have not even started the animation yet, start it!
            if (currStep.scheduledAnimation.isPresent() && !hasStartedCurrentAnimation) { 
                currentlyRunningAnimation = currStep.scheduledAnimation.get().get();
                currentlyRunningAnimation.play();
                hasStartedCurrentAnimation = true;
                return false;
            } else if (currentlyRunningAnimation.getStatus() == Status.RUNNING) { //if the animation that we are currently running isn't finished, stay on the same step
                return false;
            }
        }

        if (hasHitbreakPoint && !isContinuedPastBreakpoint) {
            return false;
        } 
        if (!hasHitbreakPoint && sourceCodeViewer.hasBreakpoint(currStep.firstLine, currStep.secondLine)) {
            sourceCodeViewer.overlayOverPermanentHighlight(currStep.firstLine, currStep.secondLine);
            hasHitbreakPoint = true;
            return false;
        }
        
        if (hasHitbreakPoint && isContinuedPastBreakpoint) {
            sourceCodeViewer.deoverlayOverPermanentHighlight();
        }

        moveToNextStep();

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