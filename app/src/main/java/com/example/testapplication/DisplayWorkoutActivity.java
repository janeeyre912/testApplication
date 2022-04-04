package com.example.testapplication;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.List;

public class DisplayWorkoutActivity extends AppCompatActivity {
    private TextView infoSplit;
    private TextView infoWorkout;
    private TextView calories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_workout);

        getSupportActionBar().setTitle("Workout Performance");
        String path = getFilesDir() + "/test.gpx";

        Log.i("path","is" + path);

        infoSplit = findViewById((R.id.infoSplit));
        infoWorkout = findViewById((R.id.infoWorkout));
        StringBuilder stringSplit = new StringBuilder();
        StringBuilder stringWorkout = new StringBuilder();

        File splitFile = new File(path);

        Split split = new Split();

        List<Split> splitList = split.decodeSplit(splitFile);

        for(int i = 0; i < splitList.size(); i++){
           stringSplit.append("Split ").append(i+1).append(" details:\n").append(splitList.get(i).getSplitInfo()).append("\n");
        }

        Workout workout = new Workout(splitList);
        Summary summary = new Summary();
        double totalDistance = summary.calculateTotalDistance(workout);
        double avgSpeed = summary.calculateAverageSpeed(workout);

        String totalDistanceS = String.valueOf(totalDistance);
        String avgSpeedS = String.valueOf(avgSpeed);

       stringWorkout.append("Total Distance: ").append(totalDistanceS).append("km").append("\n");
       stringWorkout.append("Average Speed: ").append(avgSpeedS).append("km/h");

        infoSplit.setText(stringSplit);
        infoWorkout.setText(stringWorkout);


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }


}