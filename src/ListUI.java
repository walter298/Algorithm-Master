package src;


import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import javafx.util.Duration;
import javafx.animation.*;
import javafx.scene.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class ListUI{
    private static double FONT_SIZE = 80;
    private static double NUMBER_GAP = 28;
    
    private final double inputX;
    private final double inputY;

    public class TextInteger {
        public Text text;
        // public Rectangle square;
        public Integer value;
        
        public TextInteger(int n) {
            value = n;
            text = new Text(Integer.toString(n));
            text.setFont(new Font(FONT_SIZE));
            // square = new Rectangle();
            // square.setWidth(text.getBoundsInLocal().getWidth());
            // square.setHeight(text.getBoundsInLocal().getHeight());
        }
    }

    private volatile ArrayList<TextInteger> currInput;
    
    public ListUI(Group group, ArrayList<Integer> input, int x, int y) {
        inputX = x;
        inputY = y;

        currInput = new ArrayList<>();
        
        for (var i : input) {
            append(i, group);
        }
    }

    private boolean inBounds(int idx) {
        return idx >= 0 && idx < currInput.size();
    }

    private double getXCoordOfNum(int idx, double textWidth) {
        if (!inBounds(idx)) {
            throw new IndexOutOfBoundsException();
        }
        var x = inputX;
        for (int i = 0; i < currInput.size(); i++) {
            x += currInput.get(i).text.getBoundsInLocal().getWidth() + NUMBER_GAP;
        }
        return x;
    }  

    public void append(Integer num, Group group) {
        var textInt = new TextInteger(num);

        currInput.add(textInt);

        textInt.text.setX(getXCoordOfNum(currInput.size() - 1, textInt.text.getLayoutBounds().getWidth()));
        textInt.text.setY(inputY);

        group.getChildren().add(textInt.text);
    }

    public List<TextInteger> getData() {
        return Collections.unmodifiableList(currInput);
    }

    private SequentialTransition moveUpAndOverToIndex (Text text, int idxDest) {
        var moveUp = new TranslateTransition(Duration.millis(500), text);
        moveUp.setByY(-text.getBoundsInLocal().getHeight());
        
        var moveHorizontally = new TranslateTransition(Duration.millis(700), text);
        var dist = (currInput.get(idxDest).text.getX() + currInput.get(idxDest).text.getTranslateX()) - (text.getX() + text.getTranslateX());
        moveHorizontally.setByX(dist);
        
        System.out.println(text.getText() + " moving by " + dist);

        var moveDown = new TranslateTransition(Duration.millis(500), text);
        moveDown.setByY(text.getBoundsInLocal().getHeight());
        text.setFill(Color.PINK);
        return new SequentialTransition(moveUp, moveHorizontally, moveDown);
    }

    private SequentialTransition incorrectToCorrectColors (Text text) {
        FillTransition correcting = new FillTransition(Duration.millis(850), text, Color.BLACK, Color.RED); 
        correcting.setCycleCount(2); 
        correcting.setAutoReverse(true); 
        
        FillTransition corrected = new FillTransition(Duration.millis(850), text, Color.RED, Color.BLUE); 
        corrected.setCycleCount(2); 
        corrected.setAutoReverse(true);

        return new SequentialTransition(correcting, corrected);
    }

    public ParallelTransition swap(int firstIdx, int secondIdx) {
        //referencing the two texts that will be swapped 
        var firstText = currInput.get(firstIdx).text;
        var secondText = currInput.get(secondIdx).text;

        System.out.println("Integers swapped " + currInput.get(firstIdx).value + " and " + currInput.get(secondIdx).value);
        System.out.println("Text swapped " + currInput.get(firstIdx).text.getText() + " and " + currInput.get(secondIdx).text.getText());
        //save their x-values
        // var firstTextX = firstText.getX();
        // var secondTextX = secondText.getX();

        //animation that moves them up and over to their new correct positions
        var firstTextTranslation  = moveUpAndOverToIndex(firstText, secondIdx);
        var secondTextTranslation = moveUpAndOverToIndex(secondText, firstIdx);

        var firstTextColorChange = incorrectToCorrectColors(firstText);
        var secondTextColorChange = incorrectToCorrectColors(secondText);

        //combine both animations to happen at once
        var parallelAnimation = new ParallelTransition(firstTextTranslation, secondTextTranslation);
        
        //combine both color changes so they happen at the same time
        var parallelColor = new ParallelTransition(firstTextColorChange, secondTextColorChange); 

        //combine actual swap with color changes to highlight the numbers that are being swapped
        var parallelAnimationWithColor = new ParallelTransition(parallelAnimation, parallelColor);

        //once the animation finishes, we reset the translate x values so nothing weird later happens later
        parallelAnimationWithColor.setOnFinished(event -> {
            for (var v : currInput) {
                System.out.print(v.value + " ");
            }

            Collections.swap(currInput, firstIdx, secondIdx);
        });
        parallelAnimationWithColor.setCycleCount(1); // Ensure it runs only once

        
        // return parallelAnimation;
        return parallelAnimationWithColor;
    }

    public void clear(Group group) {
        group.getChildren().clear();
        currInput.clear();
    }

    //helps illustrate to the user what numbers are being looked at in the moment
    
    // public FillTransition compare(int idx1, int idx2, Color oldColor, Color newColor){
    //     FillTransition newColor = new FillTransition(Duration.millis(850), text, oldColor, newColor); 
    //     newColor.setCycleCount(1); 
    //     newColor.setAutoReverse(true); 
    //     return newColor;
    // }

    //haven't tested this method out yet
    public ParallelTransition compare(int firstIdx, int secondIdx) {
        //referencing the two texts that will be swapped 
        var firstText = currInput.get(firstIdx).text;
        var secondText = currInput.get(secondIdx).text;

        //change their color from black to blue
        FillTransition firstTextColorChange = new FillTransition(Duration.millis(850), firstText, Color.BLACK, Color.BLUE); 
        firstTextColorChange.setCycleCount(1); 
        firstTextColorChange.setAutoReverse(true);
        FillTransition secondTextColorChange = new FillTransition(Duration.millis(850), secondText, Color.BLACK, Color.BLUE); 
        secondTextColorChange.setCycleCount(1); 
        secondTextColorChange.setAutoReverse(true);
        
        //combine both color changes so they happen at the same time
        var parallelColor = new ParallelTransition(firstTextColorChange, secondTextColorChange); 

        return parallelColor;
    }

}
