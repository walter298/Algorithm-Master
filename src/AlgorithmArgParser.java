package src;

public class AlgorithmArgParser {
    public interface SortPred {
        boolean comp(int x, int y);
    }

    public static SortPred parseSortPred(String angleBracketStr) {
        if (angleBracketStr.equals("descending")) {
            return (x, y) -> { return x > y; };
        } else {
            return (x, y) -> { return x < y; };
        }
    }
}