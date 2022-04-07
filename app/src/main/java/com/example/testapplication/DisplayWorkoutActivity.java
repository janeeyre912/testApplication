package com.example.testapplication;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

public class DisplayWorkoutActivity extends AppCompatActivity {
    private TextView infoSplit;
    private TextView infoWorkout;
    protected static final String WORKOUT_FILE_NAME = "Data.xml";
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_workout);

        //read gpx file and generate split information(Team 16)
        ProcessGPX processGPX = new ProcessGPX(this);
        XMLGenerator xmlGenerator = new XMLGenerator(this, 30);


        getSupportActionBar().setTitle("Workout Performance");
        String path = getFilesDir() + "/" + WORKOUT_FILE_NAME;

//        Log.i("path","is" + path);

        infoSplit = findViewById((R.id.infoSplit));
        infoWorkout = findViewById((R.id.infoWorkout));
        StringBuilder stringSplit = new StringBuilder();
        StringBuilder stringWorkout = new StringBuilder();

        File splitFile = new File(path);

        Split split = new Split();

        List<Split> splitList = split.decodeSplit(splitFile);

        //gets split data and combines to display
        for(int i = 0; i < splitList.size(); i++){
           stringSplit.append("Split ").append(i+1).append(" details:\n").append(splitList.get(i).getSplitInfo()).append("\n");
        }

        //gets user information (Team 13)
        User u = null;
        try {
            FileInputStream fis = openFileInput(CreateProfileActivity.PROFILE_FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            u = (User)ois.readObject();
            ois.close();
            fis.close();

        } catch(Exception e) {
            Toast.makeText(DisplayWorkoutActivity.this,"No profile exists", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


        //gets workout data and combines to display
        Workout workout = new Workout(splitList);
        Summary summary = new Summary();
        double totalDistance = summary.calculateTotalDistance(workout);
        double avgSpeed = summary.calculateAverageSpeed(workout);

        try {
            double totalCalories = summary.calculateCalories(u,workout);
            String totalCaloriesS = df.format(totalCalories);
            stringWorkout.append("Total burnt Calories: ").append(totalCaloriesS).append("kcal").append("\n");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // gets workout burnt calories
        String totalDistanceS = df.format(totalDistance);
        String avgSpeedS = df.format(avgSpeed);


        stringWorkout.append("Total Distance: ").append(totalDistanceS).append("km").append("\n");
        stringWorkout.append("Average Speed: ").append(avgSpeedS).append("km/h");


        infoSplit.setText(stringSplit);
        infoWorkout.setText(stringWorkout);

    }
}