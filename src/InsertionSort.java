package src;

import java.util.ArrayList;
import java.util.Collections;

import javafx.animation.SequentialTransition;
import javafx.scene.Group;
import javafx.scene.layout.VBox;

public class InsertionSort {

    public static void insert(Group group, ArrayList<Integer> integers) {
        var newList = new ListUI(group, integers, 270, 550);

        var animations = new AlgorithmStepList();
        
        for (int i = 0; i <= integers.size()-1; i++) {
            int j = i;
            while (j>0 && integers.get(j-1) > integers.get(j)) { 
                //swap integer[j] and integer[j-1]
                Collections.swap(integers, j, j-1);
                final int small = j, big = j-1;
                //animations.schedule(() -> { return newList.swap(small, big); });
                j = j-1; 
            }
        }        
        animations.run();
    }
}

