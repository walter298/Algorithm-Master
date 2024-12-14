package src;

import java.util.ArrayList;
// import java.util.Collections;

// import javafx.animation.SequentialTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.*;

public class BinarySearch {

    public static void lookUp(Group group, ArrayList<Integer> integers, int neededNumber) {
        //unusable??
        // var sortedList = new ListUI(group, integers, 270, 400);
        // var animations = new OnDemandAnimationList();
        
        int low = 0; 
        int high = integers.size()-1;
        int currentMiddle = (low + high)/2;
        int middleValue = integers.get(currentMiddle);
        //set middle number the color purple
        integers.get(currentMiddle).text.setFill(Color.PURPLE);
        for (int i = 0; i < integers.size(); i ++) { 
            if (middleValue == neededNumber) {
                //change color of number to green!
                integers.get(currentMiddle).text.setFill(Color.GREEN);
                String nN = Integer.toString(neededNumber); 
                System.out.print("Found" + nN + "!");
            }
            if (middleValue != neededNumber) {
                if (middleValue > neededNumber) {
                    //make middle number black again
                    integers.get(currentMiddle).text.setFill(Color.BLACK);
                    high = currentMiddle - 1; 
                    currentMiddle = (low+high)/2;
                    //set new middle number to be purple
                    integers.get(currentMiddle).text.setFill(Color.PURPLE);
                }
                if (middleValue < neededNumber) {
                    //make middle number black again
                    integers.get(currentMiddle).text.setFill(Color.BLACK);
                    low = currentMiddle +1; 
                    currentMiddle = (low+high)/2;
                    //set new middle number to be purple
                    integers.get(currentMiddle).text.setFill(Color.PURPLE);
                }
                if (middleValue != neededNumber && low == high){
                    //create method that makes all numbers red
                    System.out.print("No Such Value Found");
                }
            }
        }
    }
}

    
