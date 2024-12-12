package src;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.text.Font;

public class HomePage {
    public static Scene createScene(Stage stage) {
        // Title Label
        Label title = new Label("Algorithms!");
        title.setFont(new Font("Arial", 36));

        // Instruction Label
        Label instruction = new Label("Please choose the algorithm you want to display:");
        instruction.setFont(new Font("Arial", 18));

        // Buttons for algorithms
        Button selectionSortButton = new Button("Selection Sort");
        selectionSortButton.setPrefWidth(200);
        selectionSortButton.setOnAction(event -> 
            stage.setScene(AlgorithmPage.createScene(stage, "Selection Sort"))
        );

        Button partitionButton = new Button("Partition");
        partitionButton.setPrefWidth(200);
        partitionButton.setOnAction(event -> 
            stage.setScene(AlgorithmPage.createScene(stage, "Partition"))
        );

        // Layout for homepage
        VBox layout = new VBox(20, title, instruction, selectionSortButton, partitionButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: azure;");

        return new Scene(layout, 800, 600);
    }
}
