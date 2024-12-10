package src;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class Main extends Application {
    @Override 
    public void start(Stage stage) {
        var group = new Group();

        // var ui = new ListUI(group, new ArrayList<>(Arrays.asList(6, 5, 4, 3, 2, 1)), 270, 270);
        
        var scene = new Scene(group, Color.AZURE);
        stage.setScene(scene);

        var button = new Button("Raise");
        button.setOnMouseClicked(event -> {
            SelectionSort.sort(group, new ArrayList<Integer>(Arrays.asList(5, 2, 5, 1, 6, 2, 0, -4, 2)));
        });

        group.getChildren().add(button);

        stage.setFullScreen(true);
        stage.setTitle("Algorithm Visualizer");
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch();
    }
}