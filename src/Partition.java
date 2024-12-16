//Walter already made it, simply copy and pasted it since I didn't know how else to get it
package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import javafx.animation.ParallelTransition;
import javafx.scene.paint.Color;

public class Partition {
    private static int findFirstNotMatch(ArrayList<Integer> integers, Function<Integer, Boolean> pred, int first, int last) {
        for (int i = first; i < last; i++) {
            if (!pred.apply(integers.get(i))) {
                return i;
            }
        } 
        return last;
    }

    public static int partition(AlgorithmStepList steps, ListUI visualList, ArrayList<Integer> integers, Function<Integer, Boolean> pred, int first, int last) {
        var start = findFirstNotMatch(integers, pred, first, last);
        
        final var PARTITION_POINT_COLOR = Color.GREEN;
        
        if (start == integers.size()) {
            return start;
        }

        // final int initialStart = start;
        // steps.addAnimation(() -> { return visualList.setColor(initialStart, PARTITION_POINT_COLOR); });

        for (int i = start + 1; i < last; i++) {
            final var startF = start;
            steps.addAnimation(() -> { return visualList.setColor(startF, PARTITION_POINT_COLOR); });

            if (pred.apply(integers.get(i))) {
                //steps.addAnimation(() -> { return visualList.setColor(startF, Color.BLACK); });

                Collections.swap(integers, i, start);
                final int iCopy = i;

                var watchWindow = new VariableWatchWindow();

                watchWindow.add("start:", Integer.toString(startF), Color.GREEN);
                watchWindow.add("it:", Integer.toString(iCopy), Color.BLUE);

                steps.addStep(() -> { return visualList.swap(iCopy, startF); }, watchWindow);
                //steps.addAnimation(() -> { return visualList.setColor(iCopy, Color.BLACK); });
                
                start++;
            }
        }

        return start;
    }

    public static void partition(AlgorithmStepList steps, ListUI visualList, ArrayList<Integer> integers, Function<Integer, Boolean> pred) {
        partition(steps, visualList, integers, pred, 0, integers.size());
    }

    private static Function<Integer, Boolean> parsePred(String operator, String strValue) {
        System.out.println("Parsing " + operator + " " + strValue);
        final int value = Integer.parseInt(strValue);
    
        if (operator.equals("<")) {
            return (i) -> { return i < value; }; 
        } else if (operator.equals(">")) {
            return (i) -> { return i > value; }; 
        } else {
            return (i) -> { return i == value; }; 
        }
    }

    public static AlgorithmStepList partition(ListUI visualList, ArrayList<Integer> integers, List<String> args) {
        var steps = new AlgorithmStepList();
        var pred = parsePred(args.get(0), args.get(1));
       
        partition(steps, visualList, integers, pred);
        
        return steps;
    }
}