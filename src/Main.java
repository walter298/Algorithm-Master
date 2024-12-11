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

    @Override 
    public void start(Stage stage) {
        var group = new Group();

        var scene = new Scene(group, Color.AZURE);
        stage.setScene(scene);

        group.getChildren().addAll(makeSelectionSortButton(group), makeQuicksortButton(group));

        stage.setFullScreen(true);
        stage.setTitle("Algorithm Visualizer");
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch();
    }
}
