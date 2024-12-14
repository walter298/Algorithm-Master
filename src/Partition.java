//Walter already made it, simply copy and pasted it since I didn't know how else to get it
package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

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
        
        if (start == integers.size()) {
            return start;
        }

        for (int i = start + 1; i < last; i++) {
            if (pred.apply(integers.get(i))) {
                Collections.swap(integers, i, start);
                final int iCopy = i, startCopy = start;

                var varState = new HashMap<String, String>();
                varState.put("K", Integer.toString(iCopy));
                varState.put("I", Integer.toString(i));

                steps.addStep(() -> { return visualList.swap(iCopy, startCopy); }, varState);

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