package src;

import java.util.TreeMap;

import javafx.scene.control.Label;
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

    public VariableWatchWindow() {
        varMap = new TreeMap<>();
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
            
            //set labels to appropriate colors
            nameLabel.setTextFill(entry.getValue().color);
            valueLabel.setTextFill(entry.getValue().color);

            //add them to the grid
            grid.add(nameLabel, 0, rowC);
            grid.add(valueLabel, 1, rowC);
            
            rowC++;
        }

        return grid;
    }
}
