package src;

import java.util.ArrayList;
import java.util.TreeMap;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class VariableWatchWindow {
    private class Value {
        String value;
        Color color;

        public Value(String value, Color color) {
            this.value = value;
            this.color = color;
        }
    }

    private TreeMap<String, Value> varMap;

    public static String iteratorString(int idx, int value) {
        return "input[" + Integer.toString(idx) + "] | (" + Integer.toString(value) + ")";
    }

    public static String iteratorString(int idx, ArrayList<Integer> values) {
        if (idx < values.size()) {
            return iteratorString(idx, values.get(idx));
        } else {
            return "input[" + Integer.toString(idx) + "] | (end)";
        }
    }

    public VariableWatchWindow() {
        varMap = new TreeMap<>();
    }

    //ctor
    public VariableWatchWindow(VariableWatchWindow other) {
        varMap = new TreeMap<>(other.varMap);
    }

    public void add(String name, String value, Color color) {
        varMap.put(name, new Value(value, color));
    }
    
    private static Label makeLabel(String str) {
        var label = new Label(str);
        label.setFont(new Font(24));
        return label;
    }

    public GridPane makeWindow() {
        var grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);

        int rowC = 0;
        for (var entry : varMap.entrySet()) {
            //create name and value labels
            var nameLabel = makeLabel(entry.getKey());
            var valueLabel = makeLabel(entry.getValue().value);
            
            //set variable name to appropriate color
            nameLabel.setTextFill(entry.getValue().color);
            
            //add them to the grid
            grid.add(nameLabel, 0, rowC);
            grid.add(valueLabel, 1, rowC);

            grid.setBorder(new Border(new BorderStroke(
                Color.BLACK, 
                BorderStrokeStyle.SOLID, 
                CornerRadii.EMPTY, 
                new BorderWidths(2)
            )));
            grid.setBackground(new Background(
                new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
            ));

            rowC++;
        }

        return grid;
    }
}
