//Walter already made it, simply copy and pasted it since I didn't know how else to get it
package edu.carleton.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import javafx.animation.SequentialTransition;
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

    public static int partitionWithoutBreakpoints(AlgorithmStepList steps, ListUI visualList, ArrayList<Integer> integers, Function<Integer, Boolean> pred, int first, int last) {
        var start = findFirstNotMatch(integers, pred, first, last);

        if (start == integers.size()) {
            return start;
        }

        for (int i = start + 1; i < last; i++) {
            if (pred.apply(integers.get(i))) {
                Collections.swap(integers, i, start);
                final var iF = i;
                final var startF = start;
                steps.addAnimation(() -> { return visualList.swap(iF, startF); });
                start++;
            }
        }

        return start;
    }

    private static final Color PARTITION_POINT_COLOR = Color.GREEN;
    private static final Color IT_COLOR = Color.BLUE;

    private static void updateStart(AlgorithmStepList steps, ListUI visualList, ArrayList<Integer> integers, int start, int firstLine, int secondLine) {
        final var startF = start;
        var line8VarState = new VariableWatchWindow();
        line8VarState.add("first", VariableWatchWindow.iteratorString(start, integers.get(start)), PARTITION_POINT_COLOR);
        steps.addStep(() -> { return visualList.setColor(startF, PARTITION_POINT_COLOR); }, line8VarState, firstLine, secondLine);
    }

    private static void updateStartAndIt(AlgorithmStepList steps, ListUI visualList, ArrayList<Integer> integers, int start, int it, int firstLine, int secondLine) {
        final var startF = start;
        final var itF = it;
        final var firstLineF = firstLine;
        final var secondLineF = secondLine;

        var watchWindow = new VariableWatchWindow();
        watchWindow.add("start:", VariableWatchWindow.iteratorString(startF, integers.get(startF)), PARTITION_POINT_COLOR);
        watchWindow.add("it:", VariableWatchWindow.iteratorString(itF, integers.get(itF)), IT_COLOR);
        steps.addStep(() -> { 
            return new SequentialTransition(
                visualList.setColor(startF, PARTITION_POINT_COLOR),
                visualList.setColor(itF, IT_COLOR)
            );
        }, watchWindow, firstLineF, secondLineF);
    }

    private static void makeSwapItStartAnimation(AlgorithmStepList steps, ListUI visualList, ArrayList<Integer> integers, int start, int it) {
        final var startF = start;
        final var itF = it;
        steps.addAnimation(() -> { 
            return new SequentialTransition(
                visualList.swap(itF, startF),
                visualList.setColor(itF, PARTITION_POINT_COLOR),
                visualList.setColor(startF, PARTITION_POINT_COLOR),
                visualList.setColor(itF, IT_COLOR)
            );
        });   
    }

    private static void incrementStartColor(AlgorithmStepList steps, ListUI visualList, ArrayList<Integer> integers, int start) {
        final var startF = start;
        final var inputLen = integers.size();
        steps.addAnimation(() -> {
            var transition = new SequentialTransition();
            transition.getChildren().add(visualList.setColor(startF, Color.BLACK));
            if (startF + 1 < inputLen) {
                transition.getChildren().add(visualList.setColor(startF + 1, PARTITION_POINT_COLOR));
            }
            return transition;
        }, 0, 0);
    }

    private static void blackenOldItValue(AlgorithmStepList steps, ListUI visualList, ArrayList<Integer> integers, int start, int it) {
        if (it != start + 1 && it > 0) {
            final var itF = it;
            steps.addAnimation(() -> { 
                return visualList.setColor(itF - 1, Color.BLACK);
            }, 1000, 1000);
        }
    }

    private static void colorPartitionedRange(AlgorithmStepList steps, ListUI visualList, ArrayList<Integer> integers, int start) {
        final var startF = start;
        final var itF = integers.size() - 1;
        steps.addAnimation(() -> { return visualList.setColor(startF, Color.BLACK); });
        steps.addAnimation(() -> { return visualList.setColor(itF, Color.BLACK); });
        
        for (int i = 0; i < start; i++) {
            final var iF = i;
            steps.addAnimation(() -> { return visualList.setColor(iF, Color.GREEN); });
        }
    }

    public static int partition(AlgorithmStepList steps, ListUI visualList, ArrayList<Integer> integers, Function<Integer, Boolean> pred, int first, int last) {
        var start = findFirstNotMatch(integers, pred, first, last);

        //add line variable state at std::find_if_not around line 8
        updateStart(steps, visualList, integers, start, 7, 8);
        
        if (start == integers.size()) {
            return start;
        }

        for (int i = start + 1; i < last; i++) {
            blackenOldItValue(steps, visualList, integers, start, i);
            updateStartAndIt(steps, visualList, integers, start, i, 14, 15);

            if (pred.apply(integers.get(i))) {
                steps.addBreakpoint(15, 16); 
                Collections.swap(integers, i, start);

                makeSwapItStartAnimation(steps, visualList, integers, start, i);
                updateStartAndIt(steps, visualList, integers, start, i, 16, 17);

                incrementStartColor(steps, visualList, integers, start);
                start++;

                updateStartAndIt(steps, visualList, integers, start, i, 17, 18);
            }
        }

        colorPartitionedRange(steps, visualList, integers, start);

        return start;
    }

    public static void partition(AlgorithmStepList steps, ListUI visualList, ArrayList<Integer> integers, Function<Integer, Boolean> pred) {
        partition(steps, visualList, integers, pred, 0, integers.size());
    }

    private static Function<Integer, Boolean> parsePred(String operator, String strValue) {
        final int value = Integer.parseInt(strValue);
    
        if (operator.equals("<")) {
            return (i) -> { return i < value; }; 
        } else if (operator.equals(">")) {
            return (i) -> { return i > value; }; 
        } else {
            return (i) -> { return i == value; }; 
        }
    }

    public static AlgorithmStepList partition(ListUI visualList, HighlightableCodeArea codeArea, ArrayList<Integer> integers, List<String> args) {
        var steps = new AlgorithmStepList(codeArea);
        var pred = parsePred(args.get(0), args.get(1));
       
        partition(steps, visualList, integers, pred);
        
        return steps;
    }
}