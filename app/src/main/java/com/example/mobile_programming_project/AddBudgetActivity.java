package com.example.mobile_programming_project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class AddBudgetActivity extends AppCompatActivity {
    private DatePickerDialog pickerDialog;
    private Button dateBtn;
    Button saveBtn;
    Spinner spn;

    //Kurang :
    // Icon Calender gx tau cara gesernya kalo di taro luar Button, icon ada dibelakang buttonnya iconya
    // validasi pop up pas neken save, cuma punya gw muncul di awal:v
    // sama tombol back gx tau gmna?
    // warna awal spinner blm bisa, tpi yg dropdownnya udah
    // button savenya msh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addbudget);
        initDatePicker();
        dateBtn = findViewById(R.id.dateButton);
        dateBtn.setText(getTodayDate());
        saveBtn = findViewById(R.id.saveButton);
        spn = findViewById(R.id.spinner);


        //Spinner
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> AdapterView, View view, int position, long id) {
                String item = AdapterView.getItemAtPosition(position).toString();
                Toast.makeText(AddBudgetActivity.this, "" + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> arrList = new ArrayList<>();
        arrList.add("");
        arrList.add("M-Banking");
        arrList.add("Dana");
        arrList.add("OVO");
        arrList.add("ShoppePay");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrList);
        adapter.setDropDownViewResource(R.layout.dropdown_color_spinner);
        spn.setAdapter(adapter);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBudgetActivity.this, SpentBudgetActivity.class);
                startActivity(intent);
            }
        });

        //Pop up  Validation
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Warning!")
                .setMessage("Are You Sure with your choice?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(AddBudgetActivity.this,"Success",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
//    private void OpenDialog() {
//        ExampleDialog exampleDialog = new ExampleDialog();
//        exampleDialog.show(getSupportFragmentManager(),"example dialog");
//    }
//    public class ExampleDialog extends AppCompatDialogFragment{
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//               builder.setTitle("Warning!")
//                .setMessage("Are You Sure with your choice?")
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//            return builder.create();
//        }


    //Time Picker
    private String getTodayDate() {
        Calendar cdr = Calendar.getInstance();
        int day = cdr.get(Calendar.DAY_OF_MONTH);
        int month = cdr.get(Calendar.MONTH);
        month = month+1;
        int year = cdr.get(Calendar.YEAR);
        return MakeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener DateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = MakeDateString(day, month, year);
                dateBtn.setText(date);
            }
        };
        Calendar cdr = Calendar.getInstance();
        int day = cdr.get(Calendar.DAY_OF_MONTH);
        int month = cdr.get(Calendar.MONTH);
        int year = cdr.get(Calendar.YEAR);

        int style = AlertDialog.THEME_HOLO_DARK;
        pickerDialog = new DatePickerDialog(this, style, DateListener, day, month, year);
    }

    private String MakeDateString(int day, int month, int year) {
        return getMonthFormat(month)+"/"+day+"/"+year;
    }

    private String getMonthFormat(int month) {
        if(month==1){
            return "01";
        }
        if(month==2){
            return "02";
        }
        if(month==3){
            return "03";
        }
        if(month==4){
            return "04";
        }
        if(month==5){
            return "05";
        }
        if(month==6){
            return "06";
        }
        if(month==7){
            return "07";
        }
        if(month==8){
            return "08";
        }
        if(month==9){
            return "09";
        }
        if(month==10){
            return "10";
        }
        if(month==11){
            return "11";
        }
        if(month==12){
            return "12";
        }
        return "01";
    }
    public void openDatePicker(View view) {

        pickerDialog.show();
    }
}
