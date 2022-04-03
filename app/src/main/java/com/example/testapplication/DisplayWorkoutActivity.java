package com.example.testapplication;

import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.List;

public class DisplayWorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_workout);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/test.gpx";

        TextView textInfo = findViewById((R.id.info));
        StringBuilder info = new StringBuilder();

        File splitFile = new File(path);
//        info.append(splitFile.getPath()).append("\n\n");

        Split split = new Split();

        List<Split> splitList = split.decodeSplit(splitFile);

        info.append("This workout includes ").append(splitList.size()).append(" splits: \n");

        for(int i = 0; i < splitList.size(); i++){
           info.append("Split ").append(i+1).append(" details:\n").append(splitList.get(i).getSplitInfo()).append("\n");
        }

        Workout workout = new Workout(splitList);
        Summary summary = new Summary();
        double totalDistance = summary.calculateTotalDistance(workout);
        double avgSpeed = summary.calculateAverageSpeed(workout);

        String totalDistanceS = String.valueOf(totalDistance);
        String avgSpeedS = String.valueOf(avgSpeed);

        info.append("\n");
       info.append("Total Distance: ").append(totalDistanceS).append("km").append("\n");
       info.append("Average Speed: ").append(avgSpeedS).append("km/h");

        textInfo.setText(info);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


}