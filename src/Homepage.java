package src;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;

public class Homepage {
    /**
     * Creates and returns the homepage scene, dynamically generating buttons for all algorithms.
     *
     * @param stage The primary stage for managing scene transitions.
     * @return A Scene object representing the homepage.
     */

    private static Button makeButton(String label, Stage stage, AlgorithmPage page) {
        var button = new Button(label);
        button.setPrefWidth(200);
        button.setOnMouseClicked(event -> {
            page.run(stage);
        });
        return button;
    }

    public static Scene createScene(Stage stage) {
        var layout = new VBox();
        var homepage = new Scene(layout);
        
        Label title = new Label("Algorithms!");
        title.setFont(new Font("Arial", 36));

        Label instruction = new Label("Please choose the algorithm you want to display:");
        instruction.setFont(new Font("Arial", 18));

        var selectionSortButton = makeButton("Selection Sort", stage, new AlgorithmPage("selection_sort.json", SelectionSort::sort, stage, homepage));
        var partitionButton = makeButton("Partition", stage, new AlgorithmPage("partition.json", Partition::partition, stage, homepage));
        var quickSortButton = makeButton("Quicksort", stage, new AlgorithmPage("quicksort.json", QuickSort::sort, stage, homepage));
        var nthElementButton = makeButton("Nth Element", stage, new AlgorithmPage("nthelement.json", NthElement::find, stage, homepage));
        
        layout.setAlignment(Pos.CENTER);
        
        // Add title and instruction to the layout
        layout.getChildren().addAll(title, instruction, selectionSortButton, partitionButton, quickSortButton, nthElementButton);
        //VBox.setMargin(layout, new Insets(100, 0, 0, 0));
        
        // Return the homepage scene
        return homepage;
    }
}