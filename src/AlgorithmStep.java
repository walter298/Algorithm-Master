package src;

import java.util.HashMap;
import java.util.function.Supplier;

import javafx.animation.Animation;

public class AlgorithmStep {
    private Supplier<Animation> scheduledAnimation;
    private HashMap<String, Object> variables;
    
    public AlgorithmStep(Supplier<Animation> scheduledAnimation, HashMap<String, Object> variables) {
        this.scheduledAnimation = scheduledAnimation;
        this.variables = variables;
    }

    public Object getVariable(String name) {
        return variables.get(name);
    }

    
}
