package src;

import javafx.application.*;
import javafx.stage.*;

// This is my new, new,new addition.
public class Main extends Application {
    @Override 
    public void start(Stage stage) {
        //don't go full screen, but show enough so that user can see the algorithm
        stage.setX(0);
        stage.setY(0);
        stage.setWidth(Screen.getPrimary().getBounds().getWidth() / 1.3);
        stage.setHeight(Screen.getPrimary().getBounds().getHeight() / 1.3);

        try {
            var homepageScene = Homepage.createScene(stage);
            stage.setScene(homepageScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch();
    }
}
