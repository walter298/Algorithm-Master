package src;

import java.util.ArrayList;
import java.util.Collections;

import javafx.animation.SequentialTransition;
import javafx.scene.Group;


public class HeapSort {

    public void sortTree(Group group, ArrayList<Integer> integers) {
        var newList = new ListUI(group, integers, 270, 1470);

        var animations = new OnDemandAnimationList();
    
    // source of skeleton for code: https://www.geeksforgeeks.org/java-program-for-heap-sort/
        int n = integers.size();

        //build max heap from the unsorted list provided
        for (int i = n / 2 - 1; i >= 0; i--){
            heapify(integers, n, i);
        }
        //once max heap is sorted, extract root (max value) and place at the very end
        //of the list, shortening the list that needs to be sorted along the way 
        for (int j = n - 1; j >= 0; j--){
            //move current root (max value) to the end of the list by swapping the 
            //root with the last node (last value on the list)
            Collections.swap(integers, 0, j);
            final int rootValue = 0, lastValue = j;
            animations.schedule(() -> { return newList.swap(rootValue, lastValue); });
            //max heapify the shortened heap now that the root (max value) is in it's
            //correct location on the list and is "out of the way"
            heapify(integers, j, 0);
        } 
        animations.run();
    }
 
    // To heapify a subtree rooted with node i which is
    // an index in arr[]. n is size of heap
    void heapify(ArrayList<Integer> numbers, int n, int i)
    {
        int largest = i; // Initialize largest as root
        int l = 2 * i + 1; // left = 2*i + 1
        int r = 2 * i + 2; // right = 2*i + 2
 
        // If left child is larger than root
        if (l < n && numbers.get(l) > numbers.get(largest)){
            largest = l;
        }
 
        // If right child is larger than largest so far
        if (r < n && numbers.get(r) > numbers.get(largest)){
            largest = r;
        }
 
        // If largest is not root
        if (largest != i) {
            //swap largest number with root to make max heap more accurate
            //ISSUE: BECAUSE ANIMATIONS AND NEWlIST ARE CREATED IN SORTTREE, 
            //HOW DO I TRANSFER THAT INFORMATION SUCCESSFULLY INTO HEAPIFY?
            Collections.swap(numbers, i, largest);
            final int rootValue = i, largerValue = largest;
            //animations.schedule(() -> { return newList.swap(rootValue, largerValue); });

            // Recursively heapify the affected sub-tree
            heapify(numbers, n, largest);
        }
    }

}


