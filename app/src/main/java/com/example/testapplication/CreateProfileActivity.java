package com.example.testapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;

public class CreateProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private EditText editFullName, editDOB, editWeight;
    final String decimal[] = {".0",".1",".2",".3",".4",".5",".6",".7",".8",".9"};
    final String metric[] ={"kg", "lbs"};
    private RadioGroup radioGroupGender;
    private RadioButton radioGroupGenderSelected;
    private RadioButton radioButtonMale;
    User user;
    private double weight;
    protected static final String PROFILE_FILE_NAME = "profile.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        getSupportActionBar().setTitle("Create Profile");
        Toast.makeText(CreateProfileActivity.this,"Please create a profile now",Toast.LENGTH_LONG).show();

        editFullName = findViewById(R.id.editFullName);
        editDOB = findViewById(R.id.editDOB);
        editWeight = findViewById(R.id.editWeight);

        // Radiobuttons for gender
        radioGroupGender = findViewById(R.id.radioGroupGender);
        // To clear button when started or resumed
        radioGroupGender.clearCheck();
        radioButtonMale  = findViewById(R.id.radioButtonMale);

        // Datepicker for date of birth
        editDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        editWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWeightPickerDialog();
            }
        });

        // Action that happens when save if pressed
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedGenderID = radioGroupGender.getCheckedRadioButtonId();
                radioGroupGenderSelected = findViewById(selectedGenderID);

                // Get input data
                String textFN = editFullName.getText().toString();
                String textDOB = editDOB.getText().toString();
                String textWeight = editWeight.getText().toString();
                String textGender;

                // Check if date fields are empty
                if(textFN == null || textFN.equals("") || textFN.isEmpty()) {
                    Toast.makeText(CreateProfileActivity.this,"Please enter your full name", Toast.LENGTH_LONG).show();
                    editFullName.setError("Full name is required");
                    editFullName.requestFocus();
                } else if(textDOB == null || textDOB.equals("") || textDOB.isEmpty()) {
                    Toast.makeText(CreateProfileActivity.this,"Please enter your birthdate", Toast.LENGTH_LONG).show();
                    editDOB.setError("Birthdate is required");
                    editDOB.requestFocus();
                } else if (radioGroupGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(CreateProfileActivity.this,"Please select your gender", Toast.LENGTH_LONG).show();
                    radioButtonMale.setError("Gender is required");
                    radioButtonMale.requestFocus();
                } else if (textWeight == null || textWeight.equals("") || textWeight.isEmpty() || textWeight.equals("0")) {
                    Toast.makeText(CreateProfileActivity.this,"Please enter your weight", Toast.LENGTH_LONG).show();
                    editWeight.setError("Weight is required");
                    editWeight.requestFocus();
                } else {
                    textGender = radioGroupGenderSelected.getText().toString();
                    weight = Double.parseDouble(textWeight.substring(0, textWeight.length()-3));
                    user = new User(textFN,textDOB,textGender,weight);
                    saveProfile(user);
                    // Redirect to view profile page
                    Intent displayProfileIntent = new Intent(CreateProfileActivity.this , DisplayProfileActivity.class);
                    startActivity(displayProfileIntent);
                }
            }
        });
    }

    // Source code taken from https://github.com/mitchtabian/DatePickerDialog-Example
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    //Source code inspired from https://stackoverflow.com/questions/45812624/how-to-display-number-pickers-in-pop-up-window
    private void showWeightPickerDialog()
    {
        //Read from xml layout file
        //create and link number pickers to layout
        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.view_number_dialog, null);
        NumberPicker picker1 = (NumberPicker) linearLayout.findViewById(R.id.numberPicker1);
        NumberPicker picker2 = (NumberPicker) linearLayout.findViewById(R.id.numberPicker2);
        NumberPicker picker3 = (NumberPicker) linearLayout.findViewById(R.id.numberPicker3);

        //set int constraints
        picker1.setMinValue(40);
        picker1.setMaxValue(136);
        picker1.setValue(40);
        //set decimal constraints from list
        picker2.setMinValue(0);
        picker2.setMaxValue(decimal.length - 1);
        picker2.setDisplayedValues(decimal);
        picker2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        //set metric constraints from list
        picker3.setMinValue(0);
        picker3.setMaxValue(metric.length - 1);
        picker3.setDisplayedValues(metric);
        picker3.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        //Finally building an AlertDialog
        AlertDialog builder = new AlertDialog.Builder(this)
                .setPositiveButton("Submit", null)
                .setNegativeButton("Cancel", null)
                .setView(linearLayout)
                .setCancelable(false)
                .create();
        builder.show();

        picker3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
                                          {
                                              @Override
                                              public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue)
                                              {
                                                  if(newValue == 1)
                                                  {
                                                      double value = picker1.getValue() + picker2.getValue()/10.0;
                                                      double total = value*2.20462262185;
                                                      int full = (int) total;
                                                      double decimal = round(total - full, 1);
                                                      int decimalIndex = (decimal + "").charAt(2) - '0';
                                                      picker1.setMinValue(88);
                                                      picker1.setMaxValue(300);
                                                      picker1.setValue(full);
                                                      picker2.setValue(decimalIndex);

                                                  }
                                                  else
                                                  {
                                                      double value = picker1.getValue() + picker2.getValue()/10.0;
                                                      double total = value/2.20462262185;
                                                      int full = (int) total;
                                                      double decimal = round(total - full, 1);
                                                      int decimalIndex = (decimal + "").charAt(2) - '0';

                                                      picker1.setMinValue(40);
                                                      picker1.setMaxValue(136);
                                                      picker1.setValue(full);
                                                      picker2.setValue(decimalIndex);
                                                  }
                                              }
                                          }
        );

        //Setting up OnClickListener on positive button of AlertDialog
        builder.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //modify input
                String result = String.valueOf(picker1.getValue()) + decimal[picker2.getValue()] + " " + metric[picker3.getValue()];
                editWeight.setText(result);
                //close the popup
                builder.cancel();
            }
        });
    }
    //Code used from: https://stackoverflow.com/questions/22186778/using-math-round-to-round-to-one-decimal-place
    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = month + "/" + dayOfMonth + "/" + year;

        if (getAge(year, month, dayOfMonth) < 13) {
            editDOB.setError("Invalid Age! You are too young to create a profile");
            editDOB.requestFocus();
            Toast.makeText(this, "Invalid Age! You are too young to create a profile", Toast.LENGTH_LONG).show();
        }else {
            editDOB.setText(date);
        }

    }

    public void saveProfile(User u) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new  FileOutputStream(getFilesDir() + "/" + PROFILE_FILE_NAME,false);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(u);
            oos.close();
            fos.close();
            Toast.makeText(this, "File saved to " + getFilesDir() + "/" + PROFILE_FILE_NAME, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Source code taken from https://stackoverflow.com/questions/38967422/calculate-age-from-birthdate
    private int getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        return age;
    }
}