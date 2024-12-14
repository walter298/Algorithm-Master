package src;

import java.util.ArrayList;
import java.util.Collections;

import javafx.animation.SequentialTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.*;


public class HeapSort {

    public static void sortTree(Group group, ArrayList<Integer> integers) {
        ListUI newTreeList = new ListUI(group, integers, 270, 500);

        AlgorithmStepList animations = new AlgorithmStepList();
    
    // source of skeleton for code: https://www.geeksforgeeks.org/java-program-for-heap-sort/
        int n = integers.size();

        //build max heap from the unsorted list provided
        for (int i = n / 2 - 1; i >= 0; i--){
            heapify(integers, n, i, animations, newTreeList);
        }
        //once max heap is sorted, extract root (max value) and place at the very end
        //of the list, shortening the list that needs to be sorted along the way 
        for (int j = n - 1; j >= 0; j--){
            //move current root (max value) to the end of the list by swapping the 
            //root with the last node (last value on the list)
            //COLOR CHANGE
            Collections.swap(integers, 0, j);
            final int rootValue = 0, lastValue = j;
            animations.addStep(() -> { return newTreeList.swap(rootValue, lastValue); }, null);
            //change color from black to green of value once it is sorted
            integers.get(j).text.setFill(Color.GREEN);
            //max heapify the shortened heap now that the root (max value) is in it's
            //correct location on the list and is "out of the way"
            heapify(integers, j, 0, animations, newTreeList);
        } 
        animations.run();
    }
 
    // To heapify a subtree
    private static void heapify(ArrayList<Integer> numbers, int n, int i, AlgorithmStepList animations, ListUI treeList) {
        int largest = i; // largest is root/parent
        int l = 2 * i + 1; // left
        int r = 2 * i + 2; // right
 
        // If left child is larger than root
        if (l < n && numbers.get(l) > numbers.get(largest)){
            //compare (turn values blue)
            largest = l;
        }
 
        // If right child is larger than largest so far
        if (r < n && numbers.get(r) > numbers.get(largest)){
            //compare (turn values blue)
            largest = r;
        } 
 
        // If largest is not root
        if (largest != i) {
            //swap largest number with root to make max heap more accurate
            //COLOR CHANGE
            Collections.swap(numbers, i, largest);
            final int rootValue = i, largerValue = largest;
            animations.addStep(() -> { return treeList.swap(rootValue, largerValue); }, null);

            // Recursively heapify the affected sub-tree 
            heapify(numbers, n, largest, animations, treeList);
        }
    }

}


