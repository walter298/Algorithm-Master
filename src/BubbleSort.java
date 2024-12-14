package src;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import javafx.animation.SequentialTransition;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.scene.paint.Color;

public class BubbleSort {
    public static void bubbles(Group group, ArrayList<Integer> integers) {
        var bubbleList = new ListUI(group, integers, 270, 400);

        var animations = new AlgorithmStepList();

        for (int i = 1; i < integers.size(); i++) {
            for (int j = 0; j < integers.size()-i; j++) {
                //compare (turn values blue)
                if (integers.get(j) > integers.get(j+1)) {
                    //hopefully swap does change the color of the values
                    Collections.swap(integers, j, j+1);
                    final int big = j, small = j+1;
                    //fixed this line, hopefully
                    animations.addStep(() -> { return bubbleList.swap(big, small); }, null);                  
                }
            }
            //change color from black to green of value once it is sorted
            integers.get(integers.size()-i).text.setFill(Color.GREEN);
        }        
        animations.run();
    }
}

