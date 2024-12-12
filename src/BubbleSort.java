package src;

import java.util.ArrayList;
import java.util.Collections;

import javafx.animation.SequentialTransition;
import javafx.scene.Group;

public class BubbleSort {

    public static void bubbles(Group group, ArrayList<Integer> integers) {
        var bubbleList = new ListUI(group, integers, 270, 400);

        var animations = new OnDemandAnimationList();

        for (int i = 1; i < integers.size(); i++) {
            for (int j = 0; j < integers.size()-i; j++) {
                if (integers.get(j) > integers.get(j+1)) {
                    Collections.swap(integers, j, j+1);
                    final int big = j, small = j+1;
                    animations.schedule(() -> { return bubbleList.swap(big, small); });
                    
                }
            }
        }        
        animations.run();
    }
}

    
