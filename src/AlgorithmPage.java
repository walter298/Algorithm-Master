package src;

import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Arrays;

public class AlgorithmPage {
    public static Scene createScene(Stage stage, String algorithm) {
        Group group = new Group();

        // Back Button
        Button backButton = new Button("Back to Home");
        backButton.setLayoutX(10);
        backButton.setLayoutY(10);
        backButton.setOnAction(event -> stage.setScene(HomePage.createScene(stage)));
        group.getChildren().add(backButton);

        // Run the appropriate algorithm
        if (algorithm.equals("Selection Sort")) {
            SelectionSort.sort(group, new ArrayList<>(Arrays.asList(5, 2, 0, 1, 2, 9, 6, 3)));
        } else if (algorithm.equals("Partition")) {
            Partition.partition(group, new ArrayList<>(Arrays.asList(12, 14, 17, 20, 94, 61, 43)),
                    (i) -> i % 2 != 0);
        }

        return new Scene(group, 800, 600);
    }
}
