package edu.carleton.algorithm;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.ParallelTransition;
import javafx.scene.paint.Color;

public class QuickSort {
    private static final Color BEGIN_END_COLOR = Color.GREEN;
    private static final Color RANGE_COLOR = Color.BLUE;
    private static final Color PIVOT_COLOR = Color.PINK;
    public static final Color PARTITION_POINT_COLOR = Color.RED;

    //[first, last)
    private static void sortImpl(AlgorithmStepList steps, ListUI visualList, ArrayList<Integer> integers, AlgorithmArgParser.SortPred sortPred, int first, int last) {
        steps.addAnimation(() -> { 
            final int firstF = first;
            final int lastF = last;
            return visualList.setColor(firstF, lastF, RANGE_COLOR); 
        });

        var variableWindow = new VariableWatchWindow();

        final int firstF = first;
        final int lastF = last;
        variableWindow.add("begin", Integer.toString(firstF), BEGIN_END_COLOR);
        variableWindow.add("end", Integer.toString(lastF), BEGIN_END_COLOR);
        
        if (first == last) {
            steps.addStep(new VariableWatchWindow(variableWindow), 7, 8);
            return;
        }
        final var pivotIdx = first + ((last - first) / 2);
        final var pivot = integers.get(pivotIdx);

        variableWindow.add("pivot", VariableWatchWindow.iteratorString(pivotIdx, pivot), PIVOT_COLOR);
        steps.addStep(() -> { return visualList.setColor(pivotIdx, PIVOT_COLOR); }, new VariableWatchWindow(variableWindow), 10, 12);

        final var mid1 = Partition.partitionWithoutBreakpoints(steps, visualList, integers, (i) -> { return sortPred.comp(i, pivot); }, first, last);

        variableWindow.add("mid1", VariableWatchWindow.iteratorString(mid1, integers), PARTITION_POINT_COLOR);
        steps.addStep(new VariableWatchWindow(variableWindow), 13, 16);

        final var mid2 = Partition.partitionWithoutBreakpoints(steps, visualList, integers, (i) -> { return !sortPred.comp(pivot, i); }, mid1, last);
        
        variableWindow.add("mid2", VariableWatchWindow.iteratorString(mid2, integers), PARTITION_POINT_COLOR);
        steps.addStep(new VariableWatchWindow(variableWindow), 16, 19);
        
        steps.addAnimation(() -> {
            var turnBlackTransition = new ParallelTransition();
            for (int i = 0; i < integers.size(); i++) {
                final var iF = i;
                turnBlackTransition.getChildren().add(visualList.setColor(iF, Color.BLACK));
            }
            return turnBlackTransition;
        });
        
        sortImpl(steps, visualList, integers, sortPred, first, mid1);
        sortImpl(steps, visualList, integers, sortPred, mid2, last);
    }

    public static AlgorithmStepList sort(ListUI visualList, HighlightableCodeArea codeArea, ArrayList<Integer> integers, List<String> args) {
        var sortPred = AlgorithmArgParser.parseSortPred(args.get(0));
        var steps = new AlgorithmStepList(codeArea);
        sortImpl(steps, visualList, integers, sortPred, 0, integers.size());
        return steps;
    }
}