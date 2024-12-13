package src;

import javafx.application.*;
import javafx.stage.*;

// This is my new, new,new addition.
public class Main extends Application {
    @Override 
    public void start(Stage stage) {
        stage.setFullScreen(true);
        
        var homepageScene = Homepage.createScene(stage);
        stage.setScene(homepageScene);
        stage.show();
        // var partitionPage = new AlgorithmPage("partition.json", new AlgorithmGenerator(Partition::partition));
        //var quicksortPage = new AlgorithmPage("quicksort.json", new AlgorithmGenerator(QuickSort::sort));
        // var selectionSortPage = new AlgorithmPage("selection_sort.json", new AlgorithmGenerator(SelectionSort::sort));
        
        //quicksortPage.run(stage);
    }

    public static void main(String[] args) {
        Application.launch();
    }
}
