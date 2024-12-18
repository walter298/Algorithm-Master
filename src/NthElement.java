package src;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

public class NthElement {

    public static AlgorithmStepList find(ListUI visualList, ArrayList<Integer> integers, List<String> args) {
        var steps = new AlgorithmStepList();

        int k = Integer.parseInt(args.get(0)) - 1; // Convert to 0-indexed
        int value = kthSmallest(steps, visualList, integers, 0, integers.size() - 1, k);

        // Highlight the final Nth smallest element in the list
        for (int i = 0; i < integers.size(); i++) {
            int finalI = i;
            if (integers.get(i) == value) {
                steps.addAnimation(() -> visualList.setColor(finalI, Color.GREEN));
            }
        }
        return steps;
    }

    /**
     * Quickselect-based approach to find the k-th smallest element in the array.
     */
    private static int kthSmallest(AlgorithmStepList steps, ListUI visualList, ArrayList<Integer> integers, int low, int high, int k) {
        if (low <= high) {
            int pivotIndex = partition(steps, visualList, integers, low, high);
            if (pivotIndex == k) {
                return integers.get(pivotIndex); // Found the k-th smallest element
            } else if (pivotIndex > k) {
                return kthSmallest(steps, visualList, integers, low, pivotIndex - 1, k); // Search left
            } else {
                return kthSmallest(steps, visualList, integers, pivotIndex + 1, high, k); // Search right
            }
        }
        return -1; 
    }

    /**
     * Partition part. Elements less than the pivot
     * are moved to the left, and elements greater are moved to the right.
     */
    private static int partition(AlgorithmStepList steps, ListUI visualList, ArrayList<Integer> integers, int low, int high) {
        int pivot = integers.get(high);
        int i = low;

        for (int j = low; j < high; j++) {
            int finalJ = j;
            int finalI = i;

            // Highlight comparison
            steps.addAnimation(() -> visualList.setColor(finalJ, Color.ORANGE));

            if (integers.get(j) <= pivot) {
                steps.addAnimation(() -> visualList.swap(finalI, finalJ));
                swap(integers, i, j);
                i++;
            }
        }

        // Final swap to place pivot in its correct position
        int finalI = i;
        steps.addAnimation(() -> visualList.swap(finalI, high));
        swap(integers, i, high);

        return i; // Return the index of the pivot
    }

    private static void swap(ArrayList<Integer> list, int i, int j) {
        int temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
