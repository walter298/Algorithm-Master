package src;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class Main extends Application {
    private Button makeSelectionSortButton(Group group) {
        var selectionSortButton = new Button("Selection Sort");
        selectionSortButton.setLayoutX(100);
        selectionSortButton.setLayoutY(100);
        selectionSortButton.setOnMouseClicked(event -> {
            SelectionSort.sort(group, new ArrayList<Integer>(Arrays.asList(5, 2, 0, 1, 2, 9, 6, 3)));
            selectionSortButton.setDisable(true);
        });
        return selectionSortButton;
    }

    private Button makePartitionButton(Group group) {
        var partitionButton = new Button("Quicksort");
        partitionButton.setLayoutX(100);
        partitionButton.setLayoutY(200);
        partitionButton.setOnMouseClicked(event -> {
            QuickSort.sort(group, new ArrayList<Integer>(Arrays.asList(12, 11, 13, 17, 29, 18, 95, 81, 22, 35)));
            partitionButton.setDisable(true);
        });
        return partitionButton;
    }

    @Override 
    public void start(Stage stage) {
        var group = new Group();

        var scene = new Scene(group, Color.AZURE);
        stage.setScene(scene);

        group.getChildren().addAll(makeSelectionSortButton(group), makePartitionButton(group));

        stage.setFullScreen(true);
        stage.setTitle("Algorithm Visualizer");
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch();
    }
}