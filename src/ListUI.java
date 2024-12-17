package src;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    private static double NUMBER_GAP = 0;
    
    public class TextInteger {
        public Text text;
        public Integer value;
        public Color color = Color.BLACK;

        public TextInteger(int n) {
            value = n;
            text = new Text(Integer.toString(n));
            text.setFont(new Font(FONT_SIZE));
        }
    }

    private volatile ArrayList<TextInteger> currInput;
    
    public FillTransition setColor(int idx, Color color) {
        var text = currInput.get(idx);
        return new FillTransition(Duration.millis(300), text.text, text.color, color);
    }

    public ArrayList<Integer> toIntegers() {
        var nums = new ArrayList<Integer>();
        for (var text : currInput) {
            nums.add(text.value);
        } 
        return nums;
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

    //must be called after the numbers have been swapped
    private Optional<ParallelTransition> shiftRighthandNumbersToCreateCorrectSpacing(int originalTextIdx, int newTextIdx) {
        var originalText = currInput.get(originalTextIdx).text;
        var newText = currInput.get(newTextIdx).text;

        var widthDiff = newText.getBoundsInLocal().getWidth() - originalText.getBoundsInLocal().getWidth();

        //if the swapped numbers have the same width, we don't need to shift anything
        if (widthDiff == 0) {
            return Optional.empty();
        }

        var shiftAnimations = new ParallelTransition();
        for (int i = originalTextIdx + 1; i < currInput.size(); i++) {
            if (i == newTextIdx) {
                continue;
            }
            var shift = new TranslateTransition(Duration.millis(500), currInput.get(i).text);
            shift.setByX(widthDiff);
            shiftAnimations.getChildren().add(shift);
        }

        if (originalTextIdx < newTextIdx) {
            var shift = new TranslateTransition(Duration.millis(500), currInput.get(originalTextIdx).text);
            shift.setByX(widthDiff);
            shiftAnimations.getChildren().add(shift);
        }

        return Optional.of(shiftAnimations);
    }

    private SequentialTransition moveUpAndOverToIndex(Text text, int idxDest) {
        var moveUp = new TranslateTransition(Duration.millis(500), text);
        moveUp.setByY(-text.getBoundsInLocal().getHeight());
        
        var moveHorizontally = new TranslateTransition(Duration.millis(700), text);
        var dist = (currInput.get(idxDest).text.getLayoutX() + currInput.get(idxDest).text.getTranslateX()) - (text.getLayoutX() + text.getTranslateX());
        moveHorizontally.setByX(dist);
        
        // var moveDown = new TranslateTransition(Duration.millis(500), text);
        // moveDown.setByY(text.getBoundsInLocal().getHeight());
        
        return new SequentialTransition(moveUp, moveHorizontally);
    }

    private ParallelTransition moveBothNumbersUpAndOver(int firstIdx, int secondIdx) {
        var firstText = currInput.get(firstIdx).text;
        var secondText = currInput.get(secondIdx).text;

        //move both Text objects up and then horizontally to the other's x coordinate
        var firstTextTranslation  = moveUpAndOverToIndex(firstText, secondIdx);
        var secondTextTranslation = moveUpAndOverToIndex(secondText, firstIdx);
        var upAndOverToAnimation = new ParallelTransition(firstTextTranslation, secondTextTranslation);
        upAndOverToAnimation.setCycleCount(1);
        
        return upAndOverToAnimation;
    }

    TranslateTransition moveDown(Text text) {
        var moveDown = new TranslateTransition(Duration.millis(500), text);
        moveDown.setByY(text.getBoundsInLocal().getHeight());
        return moveDown;
    }

    public SequentialTransition swap(int firstIdx, int secondIdx) {
        var animations = new SequentialTransition();

        // //first and secondIdx must be in order
        if (firstIdx > secondIdx) {
            var temp = firstIdx;
            firstIdx = secondIdx;
            secondIdx = temp;
        }
        animations.getChildren().add(moveBothNumbersUpAndOver(firstIdx, secondIdx));

        var firstShift = shiftRighthandNumbersToCreateCorrectSpacing(firstIdx, secondIdx);
        if (firstShift.isPresent()) {
            animations.getChildren().add(firstShift.get());
        }
        var secondShift = shiftRighthandNumbersToCreateCorrectSpacing(secondIdx, firstIdx);
        if (secondShift.isPresent()) {
            animations.getChildren().add(secondShift.get());
        }

        animations.getChildren().add(new ParallelTransition(moveDown(currInput.get(firstIdx).text), moveDown(currInput.get(secondIdx).text)));

        final var firstIdxF = firstIdx;
        final var secondIdxF = secondIdx;
        animations.setOnFinished(event -> {
            Collections.swap(currInput, firstIdxF, secondIdxF);
        });

        return animations;
    }

    public void clear(HBox box) {
        box.getChildren().clear();
        currInput.clear();
    }
}
