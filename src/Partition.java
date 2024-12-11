package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

import javafx.scene.Group;

public class Partition {
    private static int findFirstNotMatch(ArrayList<Integer> integers, Function<Integer, Boolean> pred, int first, int last) {
        for (int i = first; i < last; i++) {
            if (!pred.apply(integers.get(i))) {
                return i;
            }
        } 
        return last;
    }

    public static int partition(OnDemandAnimationList animations, ListUI visualList, ArrayList<Integer> integers, Function<Integer, Boolean> pred, int first, int last) {
        var start = findFirstNotMatch(integers, pred, first, last);
        if (start == integers.size()) {
            return start;
        }

        for (int i = start + 1; i < last; i++) {
            if (pred.apply(integers.get(i))) {
                Collections.swap(integers, i, start);
                final int iCopy = i, startCopy = start;
                animations.schedule(() -> { return visualList.swap(iCopy, startCopy); });
                start++;
            }
        }

        return start;
    }

    public static void partition(OnDemandAnimationList animations, ListUI visualList, ArrayList<Integer> integers, Function<Integer, Boolean> pred) {
        partition(animations, visualList, integers, pred, 0, integers.size());
        // var start = findFirstNotMatch(integers, pred, 0, integers.size());
        // if (start == integers.size()) {
        //     return;
        // }

        // for (int i = start + 1; i < integers.size(); i++) {
        //     if (pred.apply(integers.get(i))) {
        //         Collections.swap(integers, i, start);
        //         final int iCopy = i, startCopy = start;
        //         animations.schedule(() -> { return visualList.swap(iCopy, startCopy); });
        //         start++;
        //     }
        // }
    }

    public static void partition(Group group, ArrayList<Integer> integers, Function<Integer, Boolean> pred) {
        //var start = findFirstNotMatch(integers, pred);
        // if (start == integers.size()) {
        //     return;
        // }

        var animations = new OnDemandAnimationList();
        var visualList = new ListUI(group, integers, 270, 470);

        partition(animations, visualList, integers, pred);
    
        animations.run();
    }
}
