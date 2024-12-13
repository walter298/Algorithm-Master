package src;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.layout.VBox;

public class AlgorithmGenerator {
    private static ArrayList<Integer> parseIntegers(String str) {
        var ret = new ArrayList<Integer>();
        var numStrs = str.split(" ");
        for (var numStr : numStrs) {
            ret.add(Integer.parseInt(numStr));
        }
        return ret;
    }

    protected final int VISUAL_LIST_X = 270;
    protected final int VISUAL_LIST_Y = 700;

    public interface Generator {
        AlgorithmStepList generate(Group group, ArrayList<Integer> integers, List<String> args);
    }

    private Generator gen;

    public AlgorithmGenerator(Generator gen) {
        this.gen = gen;
    }

    AlgorithmStepList generate(Group group, ArrayList<String> args) {
        return gen.generate(group, parseIntegers(args.get(0)), args.subList(1, args.size()));
    }
}