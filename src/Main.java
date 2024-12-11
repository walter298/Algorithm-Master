package src;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

// Created for the Carleton College 2024 Software Engineering Externship
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
        var partitionButton = new Button("Partition");
        partitionButton.setLayoutX(100);
        partitionButton.setLayoutY(200);
        partitionButton.setOnMouseClicked(event -> {
            Partition.partition(group, new ArrayList<Integer>(Arrays.asList(12, 14, 17, 20, 94, 61, 43)), (i) -> { return i % 2 != 0; });
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