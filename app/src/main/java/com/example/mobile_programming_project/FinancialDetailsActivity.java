package com.example.mobile_programming_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.NumberFormat;
import java.util.ArrayList;

public class FinancialDetailsActivity extends AppCompatActivity {


    int sumOfSpent, sumOfBudget;
    Button addSpentButton, addEarnedButton;
    ImageButton backButton;

    LineChart earnedLineChart, spentLineChart;

    TextView totalBalanceTextView, earnedTextView, spentTextView;

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_details);
        addSpentButton = findViewById(R.id.addSpentButton);
        addEarnedButton = findViewById(R.id.addEarnedButton);
        totalBalanceTextView = findViewById(R.id.totalBalanceFinancialDetailsTextView);
        earnedTextView = findViewById(R.id.earnedNumberTextView);
        spentTextView = findViewById(R.id.spentNumberTextView);
        backButton = findViewById(R.id.backButton);
        earnedLineChart = findViewById(R.id.earningLineChart);

        earnedLineChart.getDescription().setEnabled(false);
        earnedLineChart.setDrawGridBackground(false);
        earnedLineChart.getXAxis().setEnabled(false);
        earnedLineChart.getAxisLeft().setEnabled(false);
        earnedLineChart.getAxisRight().setEnabled(false);
        earnedLineChart.getLegend().setEnabled(false);
        earnedLineChart.setScaleEnabled(false);
        earnedLineChart.setTouchEnabled(false);

        spentLineChart = findViewById(R.id.spentLineChart);

        spentLineChart.getDescription().setEnabled(false);
        spentLineChart.setDrawGridBackground(false);
        spentLineChart.getXAxis().setEnabled(false);
        spentLineChart.getAxisLeft().setEnabled(false);
        spentLineChart.getAxisRight().setEnabled(false);
        spentLineChart.getLegend().setEnabled(false);
        spentLineChart.setScaleEnabled(false);
        spentLineChart.setTouchEnabled(false);

        viewPager = findViewById(R.id.fragmentViewPager);

        setupViewPager(viewPager);



        addSpentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FinancialDetailsActivity.this, SpentBudgetActivity.class));
            }
        });
        addEarnedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FinancialDetailsActivity.this, AddBudgetActivity.class));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FinancialDetailsActivity.this, MainActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        getSumOfSpent();
        getSumOfBudget();
        totalBalanceTextView.setText(String.format("Your balance is Rp%s", formatInteger(sumOfBudget - sumOfSpent)));
        setDataEarned();
        setDataSpent();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new TransactionHistoryFragment());
        adapter.addFragment(new DetailedCategoriesFragment());
        viewPager.setAdapter(adapter);
    }

    void getSumOfBudget() {
        SQLDatabase transactionsDB = new SQLDatabase(FinancialDetailsActivity.this);
        Cursor cursor = transactionsDB.getSumOfBudgetCategories();
        if (cursor.getCount() == 0) {
            earnedTextView.setText("0");
            sumOfBudget = 0;
        } else {
            while (cursor.moveToNext()) {
                earnedTextView.setText(formatInteger(cursor.getInt(0)));
                sumOfBudget = cursor.getInt(0);
            }
        }


    }

    void getSumOfSpent() {
        SQLDatabase transactionsDB = new SQLDatabase(FinancialDetailsActivity.this);
        Cursor cursor = transactionsDB.getSumOfSpentCategories();
        if (cursor.getCount() == 0) {
            spentTextView.setText("0");
            sumOfSpent = 0;
        } else {
            while (cursor.moveToNext()) {
                spentTextView.setText(formatInteger(cursor.getInt(0)));
                sumOfSpent = cursor.getInt(0);
            }
        }

    }


    private String formatInteger(int value) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();


        return numberFormat.format(value).replace(",", ".");
    }

    void setDataEarned() {
        ArrayList<Entry> entries = new ArrayList<>();
        SQLDatabase transactionsDB = new SQLDatabase(FinancialDetailsActivity.this);
        Cursor cursor = transactionsDB.getAllBudgetCategoriesSortedByDate();

        if (cursor.getCount() == 0) {
            entries.add(new Entry(0, 0));
        } else {
            while (cursor.moveToNext()) {
                Log.e("CURSOR ELEMENT", String.valueOf(cursor.getInt(1)));
                entries.add(new Entry(cursor.getPosition(), cursor.getInt(1)));
            }
        }

        LineDataSet earnedDataSet = new LineDataSet(entries, "");

        earnedDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        earnedDataSet.setCubicIntensity(0.2f);
        earnedDataSet.setDrawCircles(false);
        earnedDataSet.setDrawValues(false);
        earnedDataSet.setDrawFilled(true);
        earnedDataSet.setLineWidth(1.8f);
        earnedDataSet.setFillColor(0xFF127FFF);
        earnedDataSet.setFillDrawable(getDrawable(R.drawable.gradient_earned_financial_details));
        earnedDataSet.setFillAlpha(200);
        earnedDataSet.setColor(0xC8127FFF);



        LineData earnedData = new LineData(earnedDataSet);
        earnedData.setDrawValues(false);


        earnedLineChart.setData(earnedData);
        earnedLineChart.invalidate();

    }

    void setDataSpent() {
        ArrayList<Entry> entries = new ArrayList<>();
        SQLDatabase transactionsDB = new SQLDatabase(FinancialDetailsActivity.this);
        Cursor cursor = transactionsDB.getAllSpentCategoriesSortedByDate();

        if (cursor.getCount() == 0) {
            entries.add(new Entry(0, 0));
        } else {
            while (cursor.moveToNext()) {
                Log.e("CURSOR ELEMENT", String.valueOf(cursor.getInt(1)));
                entries.add(new Entry(cursor.getPosition(), cursor.getInt(1)));
            }
        }

        LineDataSet earnedDataSet = new LineDataSet(entries, "");

        earnedDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        earnedDataSet.setCubicIntensity(0.2f);
        earnedDataSet.setDrawCircles(false);
        earnedDataSet.setDrawValues(false);
        earnedDataSet.setDrawFilled(true);
        earnedDataSet.setLineWidth(1.8f);
        earnedDataSet.setFillColor(0xFF127FFF);
        earnedDataSet.setFillDrawable(getDrawable(R.drawable.gradient_spent_financial_details));
        earnedDataSet.setFillAlpha(200);
        earnedDataSet.setColor(0xFF697596);



        LineData earnedData = new LineData(earnedDataSet);
        earnedData.setDrawValues(false);


        spentLineChart.setData(earnedData);
        spentLineChart.invalidate();

    }

    public void updateFinancialDetailsUI(){
        getSumOfSpent();
        getSumOfBudget();
        totalBalanceTextView.setText(String.format("Your balance is Rp%s", formatInteger(sumOfBudget - sumOfSpent)));
        setDataEarned();
        setDataSpent();
    }
}