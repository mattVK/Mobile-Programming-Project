package com.example.mobile_programming_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.NumberFormat;

public class FinancialDetailsActivity extends AppCompatActivity {


    int sumOfSpent, sumOfBudget;
    Button addSpentButton, addEarnedButton;
    ImageButton backButton;

    TextView totalBalanceTextView, earnedTextView, spentTextView;
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

        backButton.setOnClickListener(new View.OnClickListener(){
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
    }

    void getSumOfBudget(){
        SQLDatabase transactionsDB = new SQLDatabase(FinancialDetailsActivity.this);
        Cursor cursor = transactionsDB.getSumOfBudgetCategories();
        if(cursor.getCount() == 0){
            earnedTextView.setText("0");
            sumOfBudget = 0;
        }
        else{
            while(cursor.moveToNext()){
                earnedTextView.setText(formatInteger(cursor.getInt(0)));
                sumOfBudget = cursor.getInt(0);
            }
        }



    }
    void getSumOfSpent(){
        SQLDatabase transactionsDB = new SQLDatabase(FinancialDetailsActivity.this);
        Cursor cursor = transactionsDB.getSumOfSpentCategories();
        if(cursor.getCount() == 0){
            spentTextView.setText("0");
            sumOfSpent = 0;
        }
        else{
            while(cursor.moveToNext()){
                spentTextView.setText(formatInteger(cursor.getInt(0)));
                sumOfSpent = cursor.getInt(0);
            }
        }

    }


    private String formatInteger(int value) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();



        return numberFormat.format(value).replace(",", ".");
    }

}