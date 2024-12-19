package src;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

public class NthElement {
    private static final Color FOUND_COLOR = Color.GREEN;

    public static AlgorithmStepList find(ListUI visualList, HighlightableCodeArea codeArea, ArrayList<Integer> integers, List<String> args) {
        var steps = new AlgorithmStepList(codeArea);
        if (integers.isEmpty()) {
            return steps;
        }
        int k = Integer.parseInt(args.get(0));
        findImpl(steps, visualList, integers, 0, integers.size() - 1, k);
        return steps;
    }

    private static int findImpl(AlgorithmStepList steps, ListUI visualList, ArrayList<Integer> integers, int low, int high, int k) {
        final int pivotIdx = low + ((low + high) / 2);
        final int pivot = integers.get(pivotIdx);
        final int shiftedPivotIdx = Partition.partitionWithoutBreakpoints(steps, visualList, integers, (i) -> { return i < pivot; }, low, high);
        
        if (shiftedPivotIdx == k) {
            steps.addAnimation(() -> { return visualList.setColor(shiftedPivotIdx, FOUND_COLOR); });
            return integers.get(shiftedPivotIdx); 
        } else if (shiftedPivotIdx > k) {
            return findImpl(steps, visualList, integers, low, shiftedPivotIdx - 1, k);
        } else {
            return findImpl(steps, visualList, integers, shiftedPivotIdx + 1, high, k); 
        }
    }
}