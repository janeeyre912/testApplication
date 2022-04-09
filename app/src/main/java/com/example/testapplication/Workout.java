package com.example.testapplication;

import java.util.List;

public class Workout{
    private List<Split> splitList;

    public Workout() {
       splitList = null;
    }

    public Workout(List<Split> splitList){
        this.splitList = splitList;
    }

    public List<Split> getSplitList() {
        return splitList;
    }

    public void setSplitList(List<Split> splitList) {
        this.splitList = splitList;
    }
     /*Calculate the total burnt calories per minute
    * reference: https://codereview.stackexchange.com/questions/62371/calculating-total-number-of-calories-burned
    */
	 public static double caloriesPerMinute( double weight, double time) {
	       
			return value * getMet(weight) * poundToKilogram(weight) * time;
	    }

}
