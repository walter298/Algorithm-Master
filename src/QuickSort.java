//Walter already made it, simply copy and pasted it since I didn't know how else to get it
package src;

import java.util.ArrayList;

import javafx.scene.Group;

public class QuickSort {
    //[first, last)
    private static void sortImpl(OnDemandAnimationList animations, ListUI visualList, ArrayList<Integer> integers, int first, int last) {
        if (first == last) {
            return;
        }
        var pivotIdx = first + ((last - first) / 2);
        final var pivot = integers.get(pivotIdx);
        System.out.println(pivot);
        var mid1 = Partition.partition(animations, visualList, integers, (i) -> { return i < pivot; }, first, last);
        var mid2 = Partition.partition(animations, visualList, integers, (i) -> { return i == pivot; }, mid1, last);
        sortImpl(animations, visualList, integers, first, mid1);
        sortImpl(animations, visualList, integers, mid2, last);
    }

    public static void sort(Group group, ArrayList<Integer> integers) {
        var visualList = new ListUI(group, integers, 50, 670);
        var animations = new OnDemandAnimationList();
        sortImpl(animations, visualList, integers, 0, integers.size());
        animations.run();
    }
}