package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.Group;

public class SelectionSort {
    public static AlgorithmStepList sort(Group group, ArrayList<Integer> integers, List<String> args) {
        var ui = new ListUI(group, integers, 270, 670);
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
                steps.addStep(() -> { return ui.swap(iCopy, minCopy); }, null);
            }
        }
        
        return steps;
    }
}
