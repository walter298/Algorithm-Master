package src;

import java.util.ArrayList;
import java.util.List;

public interface AlgorithmGenerator {
    AlgorithmStepList generate(ListUI visualList, ArrayList<Integer> integers, List<String> args);
}
