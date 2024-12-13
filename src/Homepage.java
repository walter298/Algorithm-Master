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
        System.out.println("Creating homepage...");
        // Title Label
        // Title Label: "Algorithms!"
        Label title = new Label("Algorithms!");
        title.setFont(new Font("Arial", 36));

        // Instruction Label
        // Instruction Label: "Please choose the algorithm you want to display"
        Label instruction = new Label("Please choose the algorithm you want to display:");
        instruction.setFont(new Font("Arial", 18));

        // Buttons for algorithms
        // Button selectionSortButton = new Button("Selection Sort");
        // selectionSortButton.setPrefWidth(200);

        var selectionSortButton = makeButton("Selection Sort", stage, new AlgorithmPage("selection_sort.json", new AlgorithmGenerator(SelectionSort::sort)));
        
        System.out.println("Made selection sort button!");
        //selectionSortButton.setOnAction(event -> {

        // Map to store algorithm names and their corresponding actions
        //var algorithms = new LinkedHashMap<String, Runnable>();
        // selectionSortButton.setOnMouseClicked(event -> {
        //     var selectionSortPage = new AlgorithmPage("selection_sort.json", new AlgorithmGenerator(SelectionSort::sort));
        //     selectionSortPage.run(stage);
        // });

        var partitionButton = makeButton("Partition", stage, new AlgorithmPage("partition.json", new AlgorithmGenerator(Partition::partition)));
        var quickSortButton = makeButton("Quicksort", stage, new AlgorithmPage("quicksort.json", new AlgorithmGenerator(QuickSort::sort)));
        var nthElementButton = makeButton("Nth Element", stage, new AlgorithmPage("nthelement.json", new AlgorithmGenerator(NthElement::find)));
        //partitionButton.setPrefWidth(200);

        System.out.println("Created buttons!");
        // partitionButton.setOnAction(event -> 
        // algorithms.put("Partition", () -> 
        //     stage.setScene(AlgorithmPage.createScene(stage, "Partition"))
        // );
        // algorithms.put("Binary Search", () -> 
        //     stage.setScene(AlgorithmPage.createScene(stage, "Binary Search"))
        // );
        // algorithms.put("Bubble Sort", () -> 
        //     stage.setScene(AlgorithmPage.createScene(stage, "Bubble Sort"))
        // );
        // algorithms.put("Heap Sort", () -> 
        //     stage.setScene(AlgorithmPage.createScene(stage, "Heap Sort"))
        // );
        // algorithms.put("Insertion Sort", () -> 
        //     stage.setScene(AlgorithmPage.createScene(stage, "Insertion Sort"))
        // );
        // algorithms.put("Quick Sort", () -> 
        //     stage.setScene(AlgorithmPage.createScene(stage, "Quick Sort"))
        // );
        // algorithms.put("Nth Element", () -> 
        //     stage.setScene(AlgorithmPage.createScene(stage, "Nth Element"))
        // );

        // Layout for homepage
        var layout = new VBox();
        // VBox to hold all the UI elements
        //VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        //layout.setStyle("-fx-background-color: azure;");

        // Add title and instruction to the layout
        layout.getChildren().addAll(title, instruction, selectionSortButton, partitionButton, quickSortButton, nthElementButton);
        VBox.setMargin(layout, new Insets(100, 0, 0, 0));
        
        // Dynamically create buttons for each algorithm
        // for (Map.Entry<String, Runnable> entry : algorithms.entrySet()) {
        //     String algorithmName = entry.getKey();
        //     Runnable action = entry.getValue();

        //     // Create a button for each algorithm
        //     Button algorithmButton = new Button(algorithmName);
        //     algorithmButton.setPrefWidth(200);
        //     algorithmButton.setOnAction(event -> action.run());

        //     // Add the button to the layout
        //     layout.getChildren().add(algorithmButton);
        // }

        // Return the homepage scene
        return new Scene(layout, 800, 600);
    }
}