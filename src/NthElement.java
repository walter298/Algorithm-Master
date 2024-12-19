package src;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.ParallelTransition;
import javafx.scene.paint.Color;

public class NthElement {
    private static final Color RANGE_COLOR = Color.BLUE;
    private static final Color PIVOT_COLOR = Color.PINK;
    private static final Color FOUND_PIVOT_COLOR = Color.GREEN;
    private static final Color PARTITION_POINT_COLOR = Color.ORANGE;

    public static AlgorithmStepList find(ListUI visualList, HighlightableCodeArea codeArea, ArrayList<Integer> integers, List<String> args) {
        var steps = new AlgorithmStepList(codeArea);

        int k = Integer.parseInt(args.get(0));
        final int pivotIdx = findImpl(steps, visualList, integers, 0, integers.size(), k);
        steps.addAnimation(() -> { return visualList.setColor(pivotIdx, FOUND_PIVOT_COLOR); });

        return steps;
    }

    private static int findImpl(AlgorithmStepList steps, ListUI visualList, ArrayList<Integer> integers, int first, int last, int k) {
        final int firstF = first;
        final int lastF = last;

        steps.addAnimation(() -> { 
            return visualList.setColor(firstF, lastF, RANGE_COLOR); 
        });

        var variableWindow = new VariableWatchWindow();

        variableWindow.add("begin", Integer.toString(firstF), RANGE_COLOR);
        variableWindow.add("end", Integer.toString(lastF), RANGE_COLOR);
        
        final var pivotIdx = first + ((last - first) / 2);
        final var pivot = integers.get(pivotIdx);

        variableWindow.add("pivot", VariableWatchWindow.iteratorString(pivotIdx, pivot), PIVOT_COLOR);
        steps.addStep(() -> { return visualList.setColor(pivotIdx, PIVOT_COLOR); }, new VariableWatchWindow(variableWindow), 10, 12);

        final var mid1 = Partition.partitionWithoutBreakpoints(steps, visualList, integers, (i) -> { return i < pivot; }, first, last);

        variableWindow.add("mid1", VariableWatchWindow.iteratorString(mid1, integers), PARTITION_POINT_COLOR);
        steps.addStep(new VariableWatchWindow(variableWindow), 8, 11);

        final var mid2 = Partition.partitionWithoutBreakpoints(steps, visualList, integers, (i) -> { return i == pivot; }, mid1, last);
        
        variableWindow.add("mid2", VariableWatchWindow.iteratorString(mid2, integers), PARTITION_POINT_COLOR);
        steps.addStep(new VariableWatchWindow(variableWindow), 11, 14);
        
        steps.addAnimation(() -> {
            return visualList.setColor(firstF, lastF, Color.BLACK);
        });

        if (mid1 == k) {
            return mid1;
        } else if (mid1 < k) {
            return findImpl(steps, visualList, integers, mid1 + 1, lastF, k); 
        } else {
            return findImpl(steps, visualList, integers, firstF, mid1, k); 
        }
    }
}
