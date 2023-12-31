package com.example.mobile_programming_project;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SpentBudgetActivity extends AppCompatActivity {

    private Button dateBtn;
    Spinner spn;
    Button saveBtn;
    ImageButton backButton;

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spent_budget);
        initDatePicker();
        dateBtn = findViewById(R.id.dateButton);
        dateBtn.setText(getTodayDate());
        saveBtn = findViewById(R.id.saveButton);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(SpentBudgetActivity.this,
                    android.Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(SpentBudgetActivity.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backintent = new Intent(SpentBudgetActivity.this, FinancialDetailsActivity.class);
                startActivity(backintent);
            }
        });
        spn = findViewById(R.id.spinner);
        editText = findViewById(R.id.editTextPhone);

        ArrayList<String> arrList = new ArrayList<>();
        getAllSpentCategories(arrList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_color_spinner, arrList);
        adapter.setDropDownViewResource(R.layout.dropdown_color_spinner);
        spn.setAdapter(adapter);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpentBudgetActivity.this, FinancialDetailsActivity.class);
                AlertDialog dialog = new AlertDialog.Builder(SpentBudgetActivity.this)
                        .setTitle("Warning!")
                        .setMessage("Are You Sure with your choice?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (spn.getSelectedItem() == "Category" || editText.getText().toString().trim().equals("")){
                                    dialog.dismiss();

                                }else {
//                                Toast.makeText(AddBudgetActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(SpentBudgetActivity.this, dateBtn.getText(), Toast.LENGTH_SHORT).show();
                                    addSpentTransaction();
                                    startActivity(intent);
                                }
                                notification();;


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    private String getTodayDate() {
        Calendar cdr = Calendar.getInstance();
        int day = cdr.get(Calendar.DAY_OF_MONTH);
        int month = cdr.get(Calendar.MONTH);
        month = month+1;
        int year = cdr.get(Calendar.YEAR);
        return MakeDateString(day, month, year);
    }

    private DatePickerDialog initDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                dateBtn.setText(MakeDateString(selectedDay, selectedMonth + 1, selectedYear));
            }
        }, year, month, day);

        return datePickerDialog;


    }

    private String MakeDateString(int day, int month, int year) {
        return String.format(Locale.getDefault(), "%02d/%02d/%d", day, month, year);
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

        initDatePicker().show();
    }

    private void notification() {
        String channel = "Channel_ID_Notification";
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), channel);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentTitle("Success");
        builder.setContentText("Your Spent Budget have been Saved");
        builder.setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel =
                    notificationManager.getNotificationChannel(channel);
            if(notificationChannel== null){
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channel,
                        "some Description", importance);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }

        }
        notificationManager.notify(0,builder.build());
    }
    void getAllSpentCategories(ArrayList<String> populateData){
        SQLDatabase transactionsDB = new SQLDatabase(SpentBudgetActivity.this);
        Cursor cursor = transactionsDB.getAllSpentCategories();
        if (cursor.getCount() == 0){
            spn.setVisibility(View.INVISIBLE);
        }
        else{
            while(cursor.moveToNext()){
                populateData.add(cursor.getString(0));
            }
        }

    }

    void addSpentTransaction(){
        SQLDatabase transactionsDB = new SQLDatabase(SpentBudgetActivity.this);
        transactionsDB.addSpentTransactions(Long.parseLong(editText.getText().toString()), formatDateForDB(dateBtn.getText().toString()), spn.getSelectedItem().toString().trim());
    }
    String formatDateForDB(String date){
        String[] formatDate = date.split("/");
        return formatDate[2] + "-" + formatDate[1] + "-" + formatDate[0];
    }


}

