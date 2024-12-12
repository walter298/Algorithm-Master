package src;

import java.util.ArrayList;
import java.util.Collections;

import javafx.animation.SequentialTransition;
import javafx.scene.Group;

public class SelectionSort {
    private static class SwapIndices {
        int i = 0;
        int j = 0;

        public SwapIndices(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    public static void sort(Group group, ArrayList<Integer> integers) {
        var ui = new ListUI(group, integers, 270, 100);

        var swapAnimations = new OnDemandAnimationList();
        for (int i = 0; i < integers.size(); i++) {
            int min = i; 
            for (int j = i + 1; j < integers.size(); j++) {
                if (integers.get(j) < integers.get(min)) {
                    min = j;
                }
            }
            if (min != i) {
                Collections.swap(integers, i, min);
                final int iCopy = i, minCopy = min;
                swapAnimations.schedule(() -> { return ui.swap(iCopy, minCopy); });
            }
        }
        
        swapAnimations.run();
    }
}
