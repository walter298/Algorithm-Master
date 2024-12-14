package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectionSort {
    public static AlgorithmStepList sort(ListUI visualList, ArrayList<Integer> integers, List<String> args) {
        var sortPred = AlgorithmArgParser.parseSortPred(args.get(0));

        var steps = new AlgorithmStepList();

        for (int i = 0; i < integers.size(); i++) {
            int min = i; 
            for (int j = i + 1; j < integers.size(); j++) {
                if (sortPred.comp(integers.get(j), integers.get(min))) {
                    min = j;
                }
            }
            if (min != i) {
                Collections.swap(integers, i, min);
                final int iCopy = i, minCopy = min;
                steps.addStep(() -> { return visualList.swap(iCopy, minCopy); }, null);
            }
        }
        
        return steps;
    }
}
