package com.example.mobile_programming_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    PendingIntent pending_intent;
    AlarmManager alarm_manager;

    List<String[]> dataList;
    Long sumOfAllSpent;

    Long sumOfAllBudget;

    Button moreDetailsButton, viewBudgetPlanButton,btn;
    TextView spentTextView, budgetTextView, availableBalanceNumberTextView, topSpentTextView;

    ListView sumCategoriesListView;

    PieChart pieChartMain;


    int[] colorArray = new int[]{0xFF2DAAFF, 0xFFFFBA01, 0x2FF9E45D, 0xFF57B0E1};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moreDetailsButton = findViewById(R.id.moreDetailsButton);
        spentTextView = findViewById(R.id.spentNumberTextView);
        viewBudgetPlanButton = findViewById(R.id.budgetPlanButton);
        sumCategoriesListView = findViewById(R.id.sumCategoriesListView);
        budgetTextView = findViewById(R.id.earnedNumberTextView);
        availableBalanceNumberTextView = findViewById(R.id.availableBalanceNumberTextView);
        topSpentTextView = findViewById(R.id.topSpentCategoryTextView);
        pieChartMain = findViewById(R.id.pieChartMainScreen);


        notification_channel();
        pending_intent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(this, BroadCastReminderAlarm.class), PendingIntent.FLAG_IMMUTABLE);
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        moreDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FinancialDetailsActivity.class));

            }
        });

        viewBudgetPlanButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FinancialDetailsActivity.class));
            }
        });

    }

    private void notification_channel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Learn Turkish";
            String description = "Learn Turkish Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Notification", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void set_notification_alarm(long interval) {
        long triggerAtMillis = System.currentTimeMillis() + interval;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        if (Build.VERSION.SDK_INT >= 23) {
            alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pending_intent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarm_manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pending_intent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarm_manager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pending_intent);
        } else {
            alarm_manager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pending_intent);
        }
    }

    public void cancel_notification_alarm() {
        alarm_manager.cancel(pending_intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        dataList = new ArrayList<String[]>();
        getSumOfSpent();
        getSumOfBudget();
        int databaseFlag = getSumOfSpentDividedIntoCategories();

        availableBalanceNumberTextView.setText(formatInteger(sumOfAllBudget - sumOfAllSpent));

        if (databaseFlag != -1) {
            ArrayList<PieEntry> entries = new ArrayList<>();
            pieChartMain.setVisibility(View.VISIBLE);

            dataList.sort(new SumComparator());

            for (int i = 0; i < dataList.size(); i++){
                entries.add(new PieEntry(Long.parseLong(dataList.get(i)[1]), dataList.get(i)[0]));
            }

            PieDataSet pieDataSet = new PieDataSet(entries, " ");
            pieDataSet.setColors(colorArray);
            pieDataSet.setDrawValues(false);

            PieData pieData = new PieData(pieDataSet);

            pieChartMain.setData(pieData);
            pieChartMain.invalidate();
            pieChartMain.getDescription().setEnabled(false);
            pieChartMain.getLegend().setEnabled(false);
            pieChartMain.setCenterTextColor(R.color.white);
            pieChartMain.setHoleColor(0xFF2D2D2D);
            pieChartMain.setTransparentCircleAlpha(0);




            topSpentTextView.setText(String.format("You spent %s on %s this month!", formatInteger(Long.parseLong(dataList.get(0)[1])), dataList.get(0)[0]));
            Log.e("DATALIST", Arrays.toString(dataList.get(0)));

            SpentCategoryListAdapter adapter = new SpentCategoryListAdapter(MainActivity.this, dataList, sumOfAllSpent);


            sumCategoriesListView.setAdapter(adapter);
        }
        else{
            pieChartMain.setVisibility(View.INVISIBLE);
        }



    }


    void getSumOfSpent(){
        SQLDatabase transactionsDB = new SQLDatabase(MainActivity.this);
        Cursor cursor = transactionsDB.getSumOfSpentCategoriesMonthly();

        if (cursor.getCount() == 0){
            spentTextView.setText("0");
            sumOfAllSpent = 0L;
        }else{
            while(cursor.moveToNext()){
                sumOfAllSpent = cursor.getLong(0);
                spentTextView.setText(formatInteger(cursor.getLong(0)));
            }
        }

    }

    void getSumOfBudget(){
        SQLDatabase transactionsDB = new SQLDatabase(MainActivity.this);
        Cursor cursor = transactionsDB.getSumOfBudgetCategoriesMonthly();

        if (cursor.getCount() == 0){
            budgetTextView.setText("0");
            sumOfAllBudget = 0L;
        }else{
            while(cursor.moveToNext()){
                sumOfAllBudget = cursor.getLong(0);
                budgetTextView.setText(formatInteger(cursor.getLong(0)));
            }
        }

    }


    int getSumOfSpentDividedIntoCategories(){
        SQLDatabase transactionsDB = new SQLDatabase(MainActivity.this);
        Cursor cursor = transactionsDB.getSumOfSpentCategoriesByCategoryMonthly();
        if (cursor.getCount() == 0){
            sumCategoriesListView.setVisibility(View.INVISIBLE);
            return -1;
        }else{
            while(cursor.moveToNext()){
                sumCategoriesListView.setVisibility(View.VISIBLE);
                String[] temp = {cursor.getString(0), String.valueOf(cursor.getLong(1)), String.valueOf(cursor.getLong(2))};
                dataList.add(temp);
            }
        }
        return 1;
    }


    private String formatInteger(Long value) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();



        return numberFormat.format(value).replace(",", ".");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        set_notification_alarm(1*60*1000);

    }

}

class SumComparator implements Comparator<String[]> {

    // Function to compare
    @Override
    public int compare(String[] o1, String[] o2) {
        Log.e("TEST", Arrays.toString(o1) + Arrays.toString(o2));
        if (Objects.equals(o1[1], o2[1])){
            return 0;
        }
        else if (Long.parseLong(o1[1]) > Long.parseLong(o2[1]) ){
            return -1;
        }
        else{
            return 1;
        }
    }
}
