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
}
