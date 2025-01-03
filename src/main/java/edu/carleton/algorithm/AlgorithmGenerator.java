package edu.carleton.algorithm;

import java.util.ArrayList;
import java.util.List;

public interface AlgorithmGenerator {
    AlgorithmStepList generate(ListUI visualList, HighlightableCodeArea codeArea, ArrayList<Integer> integers, List<String> args);
}
