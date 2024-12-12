package src;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        stage.setScene(HomePage.createScene(stage));
        stage.setTitle("Algorithm Visualizer");
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch();
    }
}
