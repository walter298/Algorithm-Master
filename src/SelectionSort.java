package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.paint.Color;

public class SelectionSort {
    public static AlgorithmStepList sort(ListUI visualList, ArrayList<Integer> integers, List<String> args) {
        var sortPred = AlgorithmArgParser.parseSortPred(args.get(0));

        var steps = new AlgorithmStepList();

        for (int i = 0; i < integers.size(); i++) {
            int min = i; 
            for (int j = i + 1; j < integers.size(); j++) {
                final var minF = min;
                steps.addAnimation(() -> { return visualList.setColor(minF, Color.PINK);});
                final var jF = j;
                steps.addAnimation(() -> { return visualList.setColor(jF, Color.PINK);});
                if (sortPred.comp(integers.get(j), integers.get(min))) {
                    min = j;
                    final var minFinal = min;
                    steps.addAnimation(() -> { return visualList.setColor(minFinal, Color.BLUE);});
                } else {
                    final var minFF = min;
                    steps.addAnimation(() -> { return visualList.setColor(minFF, Color.BLACK);});
                    final var jFF = j;
                    steps.addAnimation(() -> { return visualList.setColor(jFF, Color.BLACK);});
                }
                
            }
            if (min != i) {
                Collections.swap(integers, i, min);
                final int iCopy = i, minCopy = min;

                var watchWindow = new VariableWatchWindow();
                watchWindow.add("i", Integer.toString(i), Color.BLUE);
                watchWindow.add("min", Integer.toString(min), Color.GREEN);

                steps.addStep(() -> { return visualList.swap(iCopy, minCopy); }, watchWindow);
            }
        }
        
        return steps;
    }
}
