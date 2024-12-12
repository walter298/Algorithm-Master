package src;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

// This is my new, new,new addition.
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

    private Button makeQuicksortButton(Group group) {
        var quicksortButton = new Button("Quicksort");
        quicksortButton.setLayoutX(100);
        quicksortButton.setLayoutY(200);
        quicksortButton.setOnMouseClicked(event -> {
            QuickSort.sort(group, new ArrayList<Integer>(Arrays.asList(12, 11, 13, 17, 29, 18, 95, 81, 22, 35)));
            quicksortButton.setDisable(true);
        });
        return quicksortButton;
    }

    private Button makeBubbleSortButton(Group group) {
        var bubblesButton = new Button("Bubble Sort");
        bubblesButton.setLayoutX(100);
        bubblesButton.setLayoutY(300);
        bubblesButton.setOnMouseClicked(event -> {
            BubbleSort.bubbles(group, new ArrayList<Integer>(Arrays.asList(7, 4, 2, 8, 0, 1, 3)));
            bubblesButton.setDisable(true);
        });
        return bubblesButton;
    }

    private Button makeInsertionSortButton(Group group) {
        var InsertionButton = new Button("Insertion Sort");
        InsertionButton.setLayoutX(100);
        InsertionButton.setLayoutY(400);
        InsertionButton.setOnMouseClicked(event -> {
            InsertionSort.insert(group, new ArrayList<Integer>(Arrays.asList(5, 2, 9, 1, 0, 7, 6)));
            InsertionButton.setDisable(true);
        });
        return InsertionButton;
    }

    @Override 
    public void start(Stage stage) {
        var group = new Group();

        var scene = new Scene(group, Color.AZURE);
        stage.setScene(scene);

        group.getChildren().addAll(makeSelectionSortButton(group), makePartitionButton(group), makeBubbleSortButton(group), makeInsertionSortButton(group));

        stage.setFullScreen(true);
        stage.setTitle("Algorithm Visualizer");
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch();
    }
}
