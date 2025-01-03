package edu.carleton.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.animation.SequentialTransition;
import javafx.scene.paint.Color;

public class SelectionSort {
    private static final Color IT_COLOR  = Color.GREEN;
    private static final Color MIN_COLOR = Color.BLUE;

    private static VariableWatchWindow updateLoopWatchWindow(ArrayList<Integer> integers, int i, int min) {
        final int iF = i;
        final int minF = min;
        var watchWindow = new VariableWatchWindow();
        watchWindow.add("it", VariableWatchWindow.iteratorString(iF, integers.get(iF)), IT_COLOR);
        watchWindow.add("min", VariableWatchWindow.iteratorString(minF, integers.get(minF)), MIN_COLOR);
        return watchWindow;
    }

    public static AlgorithmStepList sort(ListUI visualList, HighlightableCodeArea codeArea, ArrayList<Integer> integers, List<String> args) {
        var sortPred = AlgorithmArgParser.parseSortPred(args.get(0));

        var steps = new AlgorithmStepList(codeArea);

        for (int i = 0; i < integers.size(); i++) {
            int I = i;
            
            int min = i; 
            
            steps.addStep(() -> { return visualList.setColor(I, IT_COLOR); }, updateLoopWatchWindow(integers, i, min), 8, 9);

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

            steps.addStep(updateLoopWatchWindow(integers, i, min), 9, 10);

            final int MIN = min;

            if (min != i) {
                Collections.swap(integers, i, min);
                steps.addStep(() -> { 
                    return new SequentialTransition(
                        visualList.swap(I, MIN),
                        visualList.setColor(I, MIN_COLOR),
                        visualList.setColor(MIN, IT_COLOR)
                    );
                    //return visualList.swap(I, MIN); 
                }, updateLoopWatchWindow(integers, i, min), 11, 12);
            }

            //reset min color and i color
            steps.addAnimation(() -> { return visualList.setColor(I, Color.BLACK); }, 500, 500);
            steps.addAnimation(() -> { return visualList.setColor(MIN, Color.BLACK); }, 500, 500);
        }
        
        return steps;
    }
}
