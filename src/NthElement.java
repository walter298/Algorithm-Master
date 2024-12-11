package src;

import java.util.ArrayList;

import javafx.scene.Group;

public class NthElement {
    public static void find(Group group, ArrayList<Integer> integers, int idx) {
        var animations = new OnDemandAnimationList();
        var visualList = new ListUI(group, integers, 270, 670);

        final var nthElement = integers.get(idx);

        Partition.partition(animations, visualList, integers, (i) -> { return i < nthElement; });
    }
}
