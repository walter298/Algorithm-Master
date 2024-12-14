package src;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import javafx.util.Duration;
import javafx.animation.*;
import javafx.scene.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ListUI {
    private static double FONT_SIZE = 80;
    private static double NUMBER_GAP = 28;
    
    public class TextInteger {
        public Text text;
        public Integer value;
        
        public TextInteger(int n) {
            value = n;
            text = new Text(Integer.toString(n));
            text.setFont(new Font(FONT_SIZE));
        }
    }

    private volatile ArrayList<TextInteger> currInput;
    
    public void setColor(int idx, Color color) {
        currInput.get(idx).text.setFill(color);
    }

    public ArrayList<Integer> toIntegers() {
        var nums = new ArrayList<Integer>();
        for (var text : currInput) {
            nums.add(text.value);
        } 
        return nums;
    }

    void printXCoordinates() {
        for (var text : currInput) {
            System.out.print(text.text.getLayoutX() + " ");
            System.out.println();
        }
    }

    public ListUI() {
        this.currInput = new ArrayList<>();
    }

    public ListUI(HBox box, ArrayList<Integer> input) {
        this();
        for (var num : input) {
            var textInt = new TextInteger(num);
            textInt.text.setY(box.getLayoutY());
            currInput.add(textInt);
            box.getChildren().add(textInt.text);
        }
    }

    public List<TextInteger> getData() {
        return Collections.unmodifiableList(currInput);
    }

    private SequentialTransition moveUpAndOverToIndex(Text text, int idxDest) {
        var moveUp = new TranslateTransition(Duration.millis(500), text);
        moveUp.setByY(-text.getBoundsInLocal().getHeight());
        
        var moveHorizontally = new TranslateTransition(Duration.millis(700), text);
        var dist = (currInput.get(idxDest).text.getLayoutX() + currInput.get(idxDest).text.getTranslateX()) - (text.getLayoutX() + text.getTranslateX());
        moveHorizontally.setByX(dist);
        
        var moveDown = new TranslateTransition(Duration.millis(500), text);
        moveDown.setByY(text.getBoundsInLocal().getHeight());
        
        return new SequentialTransition(moveUp, moveHorizontally, moveDown);
    }

    public ParallelTransition swap(int firstIdx, int secondIdx) {
        //referencing the two texts that will be swapped 
        var firstText = currInput.get(firstIdx).text;
        var secondText = currInput.get(secondIdx).text;

        //animation that moves them up and over to their new correct positions
        var firstTextTranslation  = moveUpAndOverToIndex(firstText, secondIdx);
        var secondTextTranslation = moveUpAndOverToIndex(secondText, firstIdx);

        //combine both animations to happen at once
        var parallelAnimation = new ParallelTransition(firstTextTranslation, secondTextTranslation);

        parallelAnimation.setOnFinished(event -> {
            Collections.swap(currInput, firstIdx, secondIdx);
        });
        parallelAnimation.setCycleCount(1); 

        return parallelAnimation;
    }

    public void clear(HBox box) {
        box.getChildren().clear();
        currInput.clear();
    }
}
