package src;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;

public class NthElement {
    public static AlgorithmStepList find(ListUI visualList, ArrayList<Integer> integers, List<String> args){
        var steps = new AlgorithmStepList();
        
        int value = kthSmallest(steps, visualList, integers, 0, integers.size()-1, Integer.parseInt(args.get(0)));
        
        //color or show the number we want to see
        //????? --> create a way to highlight a specific number in a list
        return steps;
    }

    // source of skeleton for code: https://www.geeksforgeeks.org/quickselect-algorithm/ 
    
    // can be used to find both kth largest and kth smallest element in the 
    //array; assume all elements in arr[] are distinct
    public static int kthSmallest(AlgorithmStepList steps, ListUI visualList, ArrayList<Integer> integers, int low, int high, int k)
    {
        // find the partition
        var pivotIdx = low + ((high - low) / 2);
        final int pivot = integers.get(pivotIdx);
        var mid1 = Partition.partition(steps, visualList, integers, (i) -> { return i < pivot; }, low, high);
        Partition.partition(steps, visualList, integers, (i) -> { return i.equals(pivot); }, mid1, high);

        // if p is equal to the kth position, return the index of the value we desire to know
        if (pivotIdx == k - 1){
            return pivot;
        }

        // if p is less than k, search right side of the array.
        else if (pivotIdx < k - 1){
            return kthSmallest(steps, visualList, integers, pivotIdx + 1, high, k);

        }
            
        // if p is more than k, search left side
        else{
            return kthSmallest(steps, visualList, integers, low, pivotIdx - 1, k);
        }
    }



}
