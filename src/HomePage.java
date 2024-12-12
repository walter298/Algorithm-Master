import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        // Create the main page
        Text title = new Text("Algorithms!");
        title.setFont(Font.font(36));

        Text instruction = new Text("Please choose the algorithm you want to display");
        instruction.setFont(Font.font(24));

        VBox vBox = new VBox(title, instruction);
        vBox.setSpacing(20);

        Button selectionSortButton = new Button("Selection Sort");
        selectionSortButton.setOnAction(event -> showSelectionSortVisualization(stage));

        Button partitionButton = new Button("Partition");
        partitionButton.setOnAction(event -> showPartitionVisualization(stage));

        HBox hBox = new HBox(selectionSortButton, partitionButton);
        hBox.setSpacing(20);

        Group group = new Group(vBox, hBox);
        Scene scene = new Scene(group, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Algorithms!");
        stage.show();
    }

    private void showSelectionSortVisualization(Stage stage) {
        // Create the selection sort visualization scene
        // You can use the code from your SelectionSort.java file here
        Group selectionSortGroup = new Group();
        Scene selectionSortScene = new Scene(selectionSortGroup, 800, 600);
        stage.setScene(selectionSortScene);
    }

    private void showPartitionVisualization(Stage stage) {
        // Create the partition visualization scene
        // You can use the code from your Partition.java file here
        Group partitionGroup = new Group();
        Scene partitionScene = new Scene(partitionGroup, 800, 600);
        stage.setScene(partitionScene);
    }

    public static void main(String[] args) {
        launch();
    }
}