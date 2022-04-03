package com.example.userprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class DisplayProfileActivity extends AppCompatActivity {
    private TextView fullName;
    private TextView dob;
    private TextView gender;
    private TextView weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile);

        User u = null;
        try {
            FileInputStream fis = openFileInput(CreateProfileActivity.PROFILE_FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            u = (User)ois.readObject();
            ois.close();
            fis.close();

            fullName = findViewById(R.id.tvName);
            fullName.setText(u.getFullName());

            dob = findViewById(R.id.tvDob);
            dob.setText(u.getDob());

            gender = findViewById(R.id.tvGender);
            gender.setText(u.getGender());

            weight = findViewById(R.id.tvWeight);
            String textWeight = String.valueOf(u.getWeight());
            weight.setText(textWeight);

        } catch(Exception e) {
            Toast.makeText(DisplayProfileActivity.this,"No profile exists", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

//        Button editButton = findViewById(R.id.createProfileButton);
//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(DisplayProfileActivity.this, CreateProfileActivity.class);
//                startActivity(intent);
//            }
//        });

        Button mainButton = findViewById(R.id.mainButton);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}