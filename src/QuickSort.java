package src;
import java.util.ArrayList;
import java.util.List;

public class QuickSort {
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

    public static AlgorithmStepList sort(ListUI visualList, ArrayList<Integer> integers, List<String> args) {
        var sortPred = AlgorithmArgParser.parseSortPred(args.get(0));
        var steps = new AlgorithmStepList();
        sortImpl(steps, visualList, integers, sortPred, 0, integers.size());
        return steps;
    }
}