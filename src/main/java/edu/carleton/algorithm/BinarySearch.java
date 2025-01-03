package edu.carleton.algorithm;

import java.util.ArrayList;
// import java.util.Collections;

// import javafx.animation.SequentialTransition;
import javafx.scene.Group;

public class BinarySearch {

    public static void lookUp(Group group, ArrayList<Integer> integers, int neededNumber) {
        // var sortedList = new ListUI(group, integers, 270, 400);

        // var animations = new OnDemandAnimationList();
        
        int low = 0; 
        int high = integers.size()-1;
        int currentMiddle = (low + high)/2;
        int middleValue = integers.get(currentMiddle);
        for (int i = 0; i < integers.size(); i ++) { 
            if (middleValue == neededNumber) {
                String nN = Integer.toString(neededNumber); 
                String indexOfnN = Integer.toString(currentMiddle + 1);
                System.out.print("Found" + nN + "! It was number" + indexOfnN + "in the list.");
            }
            if (middleValue != neededNumber) {
                if (middleValue > neededNumber) {
                    high = currentMiddle - 1; 
                    currentMiddle = (low+high)/2;
                }
                if (middleValue < neededNumber) {
                    low = currentMiddle +1; 
                    currentMiddle = (low+high)/2;
                }
                if (middleValue != neededNumber && low == high){
                    System.out.print("No Such Value Found");
                }
            }
        }
    }
}

    
