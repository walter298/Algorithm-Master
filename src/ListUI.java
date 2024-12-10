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
    private static double NUMBER_GAP = 16;
    
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
        return currInput.size() == 1 ? inputX : inputX + ((textWidth + NUMBER_GAP) * (double)idx);
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
        moveHorizontally.setByX(currInput.get(idxDest).text.getX() - text.getX());
        
        var moveDown = new TranslateTransition(Duration.millis(500), text);
        moveDown.setByY(text.getBoundsInLocal().getHeight());
        
        return new SequentialTransition(moveUp, moveHorizontally, moveDown);
    }

    public ParallelTransition swap(int firstIdx, int secondIdx) {
        var firstText = currInput.get(firstIdx).text;
        var secondText = currInput.get(secondIdx).text;

        var firstTextX = firstText.getX();
        var secondTextX = secondText.getX();

        var firstTextTranslation  = moveUpAndOverToIndex(firstText, secondIdx);
        var secondTextTranslation = moveUpAndOverToIndex(secondText, firstIdx);

        var parallelAnimation = new ParallelTransition(firstTextTranslation, secondTextTranslation);

        parallelAnimation.setOnFinished(event -> {
            firstText.setTranslateX(0);
            firstText.setX(secondTextX);

            secondText.setTranslateX(0);
            secondText.setX(firstTextX);

            Collections.swap(currInput, firstIdx, secondIdx);
        });
        
        return parallelAnimation;
    }

    public void clear(Group group) {
        group.getChildren().clear();
        currInput.clear();
    }
}
