package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

import javafx.scene.Group;

public class Partition {
    private static int findFirstNotMatch(ArrayList<Integer> integers, Function<Integer, Boolean> pred) {
        for (int i = 0; i < integers.size(); i++) {
            if (!pred.apply(integers.get(i))) {
                return i;
            }
        } 
        return integers.size();
    }

    public static void partition(Group group, ArrayList<Integer> integers, Function<Integer, Boolean> pred) {
        var start = findFirstNotMatch(integers, pred);
        if (start == integers.size()) {
            return;
        }

        var ui = new ListUI(group, integers, 270, 470);

        var animations = new OnDemandAnimationList();

        for (int i = start + 1; i < integers.size(); i++) {
            if (pred.apply(integers.get(i))) {
                Collections.swap(integers, i, start);
                final int iCopy = i, startCopy = start;
                animations.schedule(() -> { return ui.swap(iCopy, startCopy); });
                start++;
            }
        }

        animations.run();
    }
}
