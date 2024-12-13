package src;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;

public class QuickSort {
    // public interface SortPred {
    //     boolean 
    // }

    //[first, last)
    private static void sortImpl(AlgorithmStepList steps, ListUI visualList, ArrayList<Integer> integers, AlgorithmArgParser.SortPred sortPred, int first, int last) {
        if (first == last) {
            return;
        }
        var pivotIdx = first + ((last - first) / 2);
        final var pivot = integers.get(pivotIdx);
        var mid1 = Partition.partition(steps, visualList, integers, (i) -> { return sortPred.comp(i, pivot); }, first, last);
        var mid2 = Partition.partition(steps, visualList, integers, (i) -> { return !sortPred.comp(pivot, i); }, mid1, last);
        sortImpl(steps, visualList, integers, sortPred, first, mid1);
        sortImpl(steps, visualList, integers, sortPred, mid2, last);
    }

    // public static void sort(Group group, ArrayList<Integer> integers) {
    //     var visualList = new ListUI(group, integers, 50, 670);
    //     var steps = new AlgorithmStepList();
    //     sortImpl(steps, visualList, integers, 0, integers.size());
        
    // }
    
    // private Function<Integer, Boolean> parsePred(String sortOrder) {
    //     if (sortOrder.equals(""))
    // }

    public static AlgorithmStepList sort(Group group, ArrayList<Integer> integers, List<String> args) {
        var visualList = new ListUI(group, integers, 270, 670);
        var sortPred = AlgorithmArgParser.parseSortPred(args.get(0));
        var steps = new AlgorithmStepList();
        sortImpl(steps, visualList, integers, sortPred, 0, integers.size());
        return steps;
    }
}