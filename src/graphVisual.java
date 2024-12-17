package src;

import javafx.scene.layout.HBox;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.animation.ParallelTransition;
import javafx.animation.FillTransition;


import javafx.event.ActionEvent;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;


public class graphVisual extends Application{

    //Ignore this, I was trying to see if I could simply make a single rectangle appear 
    //on my screen but for some reason it never worked? I don't know what I did wrong as
    //I would follow the guide of youtube videos but it wasn't working for me, idk :(
    //Also, if you know what imports we need, could you please fix the mess I have above?
    //My computer keeps getting imports it shouldn't
    

    //START OF THE CODE I KINDA WROTE BUT MOST OF IT WILL BE WRONG, IT IS MAINLY PSEUDOCODE
    //SO YOU GUYS KNOW ABOUT HOW I ENVISIONED IT AND WANT IT TO LOOK LIKE
    //I WOULD GREATLY APPRECIATE AS MUCH FEEDBACK AS POSSIBLE AND HELP WITH CORRECTING THE 
    //CODE AS IT IS A MESS

    //choose numbers from 1 to 50, max of 25 numbers in the list
    
    //example of a list of integers the user could input
    ArrayList<Integer> nums = new ArrayList<Integer>(Arrays.asList(9, 3, 1, 4, 2, 5, 8, 6, 7));
    //list where the rectangles that are made will be stored in for future use (to change color and move around)
    ArrayList<Rectangle> listOfRectangles = new ArrayList<>();

    //should it return void? I didn't know what to put
    public void makeVisual(HBox box) {
        if (nums.size() < 1) {
            //let user know that more numbers are needed
            System.out.println("Need more numbers to work with!");
        } 
        else if (nums.size() > 25) {
            //let user know that they shouldn't use over 25 numbers
            System.out.println("List is too long! Give me a smaller list.");
        }
        else {
            //go through the list of numbers and use it to 
            //calculate and create the rectangles for the visual

            //the width of the rectangles depends on how many numbers we received, 
            //and I randomly chose for all of them to take up the space of 100 pixels in total
            //(we could totally make it bigger or smaller, don't care)
            int widthOfRectangles = 100/nums.size();
            //going through the list of numbers, creating it's respective rectangle that will represent it
            for (int index = 0; index < nums.size(); index++) {
                Rectangle rect = new Rectangle();
                //baseOfVisual acts as point (100,100) --> you'll see later how
                // LayoutUtil.setSize(rect, widthOfRectangles, nums.get(index) * 3);
                rect.setWidth(widthOfRectangles);
                rect.setHeight(nums.get(index) * 3);
                rect.setFill(Color.BLACK);
                //by creating a list of rectangles, we are ensuring that we are creating a list of rectangles 
                //we can manipulate later, as right now each rectangle in their respective index matches the 
                //number in the list that was given to us by the user 
                listOfRectangles.add(rect);
                box.getChildren().add(rect);
            }
        }
    }
    
    //swaps rectangles at the same time to their respective new places
    //returns: the parallen transition of the rectangles sliding over to their new locations
    //? ? ? do we need to re-establish the new index places of the rectangles ; if so how do we do that?
    public ParallelTransition swapRectangles (ArrayList<Rectangle> rectangles, int firstIndex, int secondIndex) {
        //does it get the rectangles correctly? I saw in other java files that after the .get() method a .text
        //was added, I didn't know if we also needed that for our rectangles?
        var firstRect = rectangles.get(firstIndex);
        var secondRect = rectangles.get(secondIndex);

        //move both rectangles to their new place (only horzontally)
        //first rectangle:
        var moveHorizontallyFirstR = new TranslateTransition(Duration.millis(1700), firstRect);
        // dist = current location + (location of other rectangle - location of rectangle)
        var distFirstR = (secondRect.getLayoutX() + secondRect.getTranslateX()) - (firstRect.getLayoutX() + firstRect.getTranslateX());
        moveHorizontallyFirstR.setByX(distFirstR);
        //second rectangle:
        var moveHorizontallySecondR = new TranslateTransition(Duration.millis(1700), secondRect);
        // dist = current location + (location of other rectangle - location of rectangle)
        var distSecondR = (firstRect.getLayoutX() + firstRect.getTranslateX()) - (secondRect.getLayoutX() + secondRect.getTranslateX());
        moveHorizontallySecondR.setByX(distSecondR);

        //make movement of rectangles happen at the same time
        var slideOverToAnimation = new ParallelTransition(moveHorizontallyFirstR, moveHorizontallySecondR);
        slideOverToAnimation.setCycleCount(1);
        
        return slideOverToAnimation;
    
    }

    //method to change the color of two rectangles at at the same time to indicate what is going on
    //reason why the color change lasts 850 milliseconds: because the swap of numbers takes a total of 
    //1700 milliseconds, half of that is 850, allowing for the swapping of rectangles and change of color
    //to "match"
    public ParallelTransition compare(ArrayList<Rectangle> rectangles, int firstIndex, int secondIndex, Color oldColor, Color newColor) {
        FillTransition changeFirstIndexColor = new FillTransition(Duration.millis(850), rectangles.get(firstIndex), oldColor, newColor); 
        FillTransition changeSecondIndexColor = new FillTransition(Duration.millis(850), rectangles.get(secondIndex), oldColor, newColor); 
        return new ParallelTransition(changeFirstIndexColor, changeSecondIndexColor);
    }

    //turn both rectangles blue so the user knows which two rectangles are being compared -->
    // ParallelTransition startCompare = 
    //compare(listOfRectangles, firstRectIndex, secondRectIndex, Color.BLACK, Color.BLUE);
    
    //turn both rectangles red (before the swap takes place) so the user knows they are in the incorrect spot
    // ParallelTransition incorrectLocation = 
    //compare(listOfRectangles, firstRectIndex, secondRectIndex, Color.BLUE, Color.RED);

    //turn both rectangles green (after the swap takes place) so the user knows they are in the correct spot now
    // ParallelTransition correctLocation = 
    //compare(listOfRectangles, firstRectIndex, secondRectIndex, Color.RED, Color.GREEN);
    
    //turn both rectangles green so the user knows that the rectangles need no change of location
    // ParallelTransition noChangeNeeded = 
    //compare(listOfRectangles, firstRectIndex, secondRectIndex, Color.BLUE, Color.GREEN);
    
    //turn both rectangles black so the user knows we are done comparing and will move on
    // ParallelTransition stopCompare = 
    //compare(listOfRectangles, firstRectIndex, secondRectIndex, Color.GREEN, Color.BLACK);
    @Override
    public void start(Stage stage) {
        var box = new HBox(25);
        makeVisual(box);
        var scene = new Scene(box);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch();
    }
}
    

    


