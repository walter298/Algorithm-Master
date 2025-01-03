package edu.carleton.algorithm;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class LayoutUtil {
    public static void setSize(Region r, int width, int height) {
        r.setPrefSize(width, height);
        r.setMinSize(width, height);
        r.setMaxSize(width, height);
    }   

    public static StackPane centerNode(Node n) {
        var centeredPane = new StackPane(n);
        centeredPane.setAlignment(Pos.TOP_CENTER); 
        return centeredPane;
    }
}
