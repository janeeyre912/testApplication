package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set title
        getSupportActionBar().setTitle("User Settings");

        // Open create profile page
        Button createButton = findViewById(R.id.createProfileButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateProfileActivity.class);
                startActivity(intent);
            }
        });

        Button viewButton = findViewById(R.id.viewProfileButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayProfileActivity.class);
                startActivity(intent);
            }
        });

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File dir = getFilesDir();
                File myFile = new File(dir, CreateProfileActivity.PROFILE_FILE_NAME);
                if (myFile.delete()) {
                    Toast.makeText(MainActivity.this,"File Deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this,"File does not exist", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button stopButton = findViewById(R.id.workoutButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayWorkoutActivity.class);
                startActivity(intent);
            }
        });

    }
}
