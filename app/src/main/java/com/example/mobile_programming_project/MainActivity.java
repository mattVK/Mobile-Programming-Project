package com.example.mobile_programming_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {


    List<String[]> dataList;
    int sumOfAll;

    Button moreDetailsButton, viewBudgetPlanButton;
    TextView spentTextView;

    ListView sumCategoriesListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moreDetailsButton = findViewById(R.id.moreDetailsButton);
        spentTextView = findViewById(R.id.spentNumberTextView);
        viewBudgetPlanButton = findViewById(R.id.budgetPlanButton);
        sumCategoriesListView = findViewById(R.id.sumCategoriesListView);

        moreDetailsButton.setOnClickListener(new View.OnClickListener(){
            SQLDatabase transactionsDB = new SQLDatabase(MainActivity.this);
            @Override
            public void onClick(View v) {
                transactionsDB.addSpentTransactions(192000, "2002-11-20", "Shopping");
            }


        });

        viewBudgetPlanButton.setOnClickListener(new View.OnClickListener(){
            SQLDatabase transactionsDB = new SQLDatabase(MainActivity.this);
            @Override
            public void onClick(View v) {
                transactionsDB.addSpentTransactions(100000, "2002-11-20", "Food & Drinks");
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        dataList = new ArrayList<String[]>();
        getSumOfSpent();
        getSumOfSpentDividedIntoCategories();



        dataList.sort(new SumComparator());

        Log.e("DATALIST", Arrays.toString(dataList.get(0)));

        SpentCategoryListAdapter adapter = new SpentCategoryListAdapter(MainActivity.this, dataList, sumOfAll);


        sumCategoriesListView.setAdapter(adapter);


    }

    void getSumOfSpent(){
        SQLDatabase transactionsDB = new SQLDatabase(MainActivity.this);
        Cursor cursor = transactionsDB.getSumOfSpentCategories();

        if (cursor.getCount() == 0){
            spentTextView.setText("0");
        }else{
            while(cursor.moveToNext()){
                sumOfAll = cursor.getInt(0);
                spentTextView.setText(formatInteger(cursor.getInt(0)));
            }
        }

    }

    void getSumOfSpentDividedIntoCategories(){
        SQLDatabase transactionsDB = new SQLDatabase(MainActivity.this);
        Cursor cursor = transactionsDB.getSumOfSpentCategoriesByCategory();
        if (cursor.getCount() == 0){

        }else{
            while(cursor.moveToNext()){
                String[] temp = {cursor.getString(0), String.valueOf(cursor.getInt(1)), String.valueOf(cursor.getInt(2))};
                dataList.add(temp);
            }
        }
    }


    private String formatInteger(int value) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();



        return numberFormat.format(value).replace(",", ".");
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
        else if (Integer.parseInt(o1[1]) > Integer.parseInt(o2[1]) ){
            Log.e("TEST", "OI");
            return -1;
        }
        else{
            return 1;
        }
    }
}