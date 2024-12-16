package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.paint.Color;

public class SelectionSort {
    public static AlgorithmStepList sort(ListUI visualList, ArrayList<Integer> integers, List<String> args) {
        var sortPred = AlgorithmArgParser.parseSortPred(args.get(0));

        var steps = new AlgorithmStepList();

        final var IT_COLOR  = Color.GREEN;
        final var MIN_COLOR = Color.BLUE;

        for (int i = 0; i < integers.size(); i++) {
            int I = i;
            steps.addAnimation(() -> { return visualList.setColor(I, IT_COLOR); });

            int min = i; 
            
            for (int j = i + 1; j < integers.size(); j++) {
                if (sortPred.comp(integers.get(j), integers.get(min))) {
                    int OLD_MIN = min;
                    min = j;
                    final int NEW_MIN = min;

                    final var OLD_MIN_COLOR = OLD_MIN == i ? Color.GREEN : Color.BLACK;
                    steps.addAnimation(() -> { return visualList.setColor(OLD_MIN, OLD_MIN_COLOR); });
                    
                    steps.addAnimation(() -> { return visualList.setColor(NEW_MIN, MIN_COLOR); });
                } 
            }

            final int MIN = min;

            if (min != i) {
                Collections.swap(integers, i, min);
                
                var watchWindow = new VariableWatchWindow();
                watchWindow.add("it", VariableWatchWindow.iteratorString(i, integers.get(i)), IT_COLOR);
                watchWindow.add("min", VariableWatchWindow.iteratorString(min, integers.get(min)), MIN_COLOR);

                steps.addStep(() -> { return visualList.swap(I, MIN); }, watchWindow);
            }

            //reset min color and i color
            steps.addAnimation(() -> { return visualList.setColor(I, Color.BLACK); });
            steps.addAnimation(() -> { return visualList.setColor(MIN, Color.BLACK); });
        }
        
        return steps;
    }
}
