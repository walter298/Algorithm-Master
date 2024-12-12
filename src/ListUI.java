package src;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import javafx.util.Duration;
import javafx.animation.*;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ListUI {
    private static double FONT_SIZE = 80;
    private static double NUMBER_GAP = 28;
    
    private final double inputX;
    private final double inputY;

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

    private SequentialTransition moveUpAndOverToIndex(Text text, int idxDest) {
        var moveUp = new TranslateTransition(Duration.millis(500), text);
        moveUp.setByY(-text.getBoundsInLocal().getHeight());
        
        var moveHorizontally = new TranslateTransition(Duration.millis(700), text);
        var dist = (currInput.get(idxDest).text.getX() + currInput.get(idxDest).text.getTranslateX()) - (text.getX() + text.getTranslateX());
        moveHorizontally.setByX(dist);
        
        System.out.println(text.getText() + " moving by " + dist);

        var moveDown = new TranslateTransition(Duration.millis(500), text);
        moveDown.setByY(text.getBoundsInLocal().getHeight());
        
        return new SequentialTransition(moveUp, moveHorizontally, moveDown);
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

        //combine both animations to happen at once
        var parallelAnimation = new ParallelTransition(firstTextTranslation, secondTextTranslation);

        //once the animation finishes, we reset the translate x values so nothing weird later happens later
        parallelAnimation.setOnFinished(event -> {
            //firstText.setTranslateX(0);
            //firstText.setX(secondTextX);

            //secondText.setTranslateX(0);
            //secondText.setX(firstTextX);
            // var firstTextX = firstText.getX();
            // firstText.setX(secondText.getX());
            // secondText.setX(firstTextX);

            for (var v : currInput) {
                System.out.print(v.value + " ");
            }

            System.out.print(" --> ");

            //making sure that the list saved in code is the same as the list order on the screen
            Collections.swap(currInput, firstIdx, secondIdx);
            // for (var i : currInput) {
            //     System.out.print(i.value + " ");
            //     System.out.println();
            // }
        });
        parallelAnimation.setCycleCount(1); // Ensure it runs only once

        return parallelAnimation;
    }

    public void clear(Group group) {
        group.getChildren().clear();
        currInput.clear();
    }
}
