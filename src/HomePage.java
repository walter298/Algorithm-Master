package src;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.text.Font;

import java.util.LinkedHashMap;
import java.util.Map;

public class HomePage {
    /**
     * Creates and returns the homepage scene, dynamically generating buttons for all algorithms.
     *
     * @param stage The primary stage for managing scene transitions.
     * @return A Scene object representing the homepage.
     */
    public static Scene createScene(Stage stage) {
        // Title Label: "Algorithms!"
        Label title = new Label("Algorithms!");
        title.setFont(new Font("Arial", 36));

        // Instruction Label: "Please choose the algorithm you want to display"
        Label instruction = new Label("Please choose the algorithm you want to display:");
        instruction.setFont(new Font("Arial", 18));

        // Map to store algorithm names and their corresponding actions
        Map<String, Runnable> algorithms = new LinkedHashMap<>();
        algorithms.put("Selection Sort", () -> 
            stage.setScene(AlgorithmPage.createScene(stage, "Selection Sort"))
        );
        algorithms.put("Partition", () -> 
            stage.setScene(AlgorithmPage.createScene(stage, "Partition"))
        );
        algorithms.put("Binary Search", () -> 
            stage.setScene(AlgorithmPage.createScene(stage, "Binary Search"))
        );
        algorithms.put("Bubble Sort", () -> 
            stage.setScene(AlgorithmPage.createScene(stage, "Bubble Sort"))
        );
        algorithms.put("Heap Sort", () -> 
            stage.setScene(AlgorithmPage.createScene(stage, "Heap Sort"))
        );
        algorithms.put("Insertion Sort", () -> 
            stage.setScene(AlgorithmPage.createScene(stage, "Insertion Sort"))
        );
        algorithms.put("Quick Sort", () -> 
            stage.setScene(AlgorithmPage.createScene(stage, "Quick Sort"))
        );
        algorithms.put("Nth Element", () -> 
            stage.setScene(AlgorithmPage.createScene(stage, "Nth Element"))
        );

        // VBox to hold all the UI elements
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: azure;");

        // Add title and instruction to the layout
        layout.getChildren().addAll(title, instruction);

        // Dynamically create buttons for each algorithm
        for (Map.Entry<String, Runnable> entry : algorithms.entrySet()) {
            String algorithmName = entry.getKey();
            Runnable action = entry.getValue();

            // Create a button for each algorithm
            Button algorithmButton = new Button(algorithmName);
            algorithmButton.setPrefWidth(200);
            algorithmButton.setOnAction(event -> action.run());
            
            // Add the button to the layout
            layout.getChildren().add(algorithmButton);
        }

        // Return the homepage scene
        return new Scene(layout, 800, 600);
    }
}
