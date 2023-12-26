package com.example.mobile_programming_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SQLDatabase extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "TransactionsLibrary.db";
    private static final int DATABASE_VERSION = 1;


    private static final String TABLE_NAME_SPENT_CATEGORIES = "spent_categories";
    private static final String COLUMN_CATEGORY_SPENT_CATEGORIES = "_category";

    private static final String TABLE_NAME_BUDGET_CATEGORIES = "budget_categories";
    private static final String COLUMN_CATEGORY_BUDGET_CATEGORIES = "_category";

    private static final String TABLE_NAME_SPENT_TRANSACTIONS = "spent_transactions";
    private static final String COLUMN_ID_SPENT_TRANSACTIONS = "_id";
    private static final String COLUMN_AMOUNT_SPENT_TRANSACTIONS = "amount";
    private static final String COLUMN_DATE_SPENT_TRANSACTIONS = "date";
    private static final String COLUMN_CATEGORY_SPENT_TRANSACTIONS = "category";

    private static final String TABLE_NAME_BUDGET_TRANSACTIONS = "budget_transactions";
    private static final String COLUMN_ID_BUDGET_TRANSACTIONS = "_id";
    private static final String COLUMN_AMOUNT_BUDGET_TRANSACTIONS = "amount";
    private static final String COLUMN_DATE_BUDGET_TRANSACTIONS = "date";
    private static final String COLUMN_CATEGORY_BUDGET_TRANSACTIONS = "category";



    public SQLDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String queryBudgetCategory = "CREATE TABLE " + TABLE_NAME_BUDGET_CATEGORIES + " ( " +
                                     COLUMN_CATEGORY_BUDGET_CATEGORIES + " TEXT PRIMARY KEY);";

        String querySpentCategory = "CREATE TABLE " + TABLE_NAME_BUDGET_CATEGORIES + " ( " +
                COLUMN_CATEGORY_SPENT_CATEGORIES + " TEXT PRIMARY KEY);";

        String queryBudget = "CREATE TABLE " + TABLE_NAME_BUDGET_TRANSACTIONS + " ( " +
                        COLUMN_ID_BUDGET_TRANSACTIONS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_AMOUNT_BUDGET_TRANSACTIONS + " REAL, " +
                        COLUMN_DATE_BUDGET_TRANSACTIONS + " DATE, " +
                        COLUMN_CATEGORY_BUDGET_TRANSACTIONS + " TEXT, " +
                        "FOREIGN KEY ("+ COLUMN_CATEGORY_BUDGET_TRANSACTIONS + ")" + " REFERENCES " + TABLE_NAME_BUDGET_CATEGORIES + "(" + COLUMN_CATEGORY_BUDGET_CATEGORIES + "));";
        String querySpent = " CREATE TABLE " + TABLE_NAME_SPENT_TRANSACTIONS + " ( " +
                        COLUMN_ID_SPENT_TRANSACTIONS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_AMOUNT_SPENT_TRANSACTIONS + " REAL, " +
                        COLUMN_DATE_SPENT_TRANSACTIONS + " DATE, " +
                        COLUMN_CATEGORY_SPENT_TRANSACTIONS + " TEXT, " +
                        "FOREIGN KEY ("+ COLUMN_CATEGORY_SPENT_TRANSACTIONS + ")" + " REFERENCES " + TABLE_NAME_SPENT_CATEGORIES + "(" + COLUMN_CATEGORY_SPENT_CATEGORIES + "));";

        db.execSQL(queryBudgetCategory);
        db.execSQL(querySpentCategory);
        db.execSQL(queryBudget);
        db.execSQL(querySpent);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BUDGET_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SPENT_TRANSACTIONS);

        onCreate(db);
    }


    //add budget transactions by calling this. (date format: YYYY-MM-DD)
    void addBudgetTransactions(double amount, String date, String category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_AMOUNT_BUDGET_TRANSACTIONS, amount);
        cv.put(COLUMN_DATE_BUDGET_TRANSACTIONS, date);
        cv.put(COLUMN_CATEGORY_BUDGET_TRANSACTIONS, category);

        long result = db.insert(TABLE_NAME_BUDGET_TRANSACTIONS, null, cv);
        if (result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }
    }

    //add spent transactions by calling this. (date format: YYYY-MM-DD)
    void addSpentTransactions(double amount, String date, String category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_AMOUNT_SPENT_TRANSACTIONS, amount);
        cv.put(COLUMN_DATE_SPENT_TRANSACTIONS, date);
        cv.put(COLUMN_CATEGORY_SPENT_TRANSACTIONS, category);

        long result = db.insert(TABLE_NAME_SPENT_TRANSACTIONS, null, cv);
        if (result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }
    }


    //sum of categories for main screen.
    Cursor getAllSumOfSpentCategories(){
        String query = "SELECT SUM(" + COLUMN_AMOUNT_SPENT_TRANSACTIONS  + ") FROM " + TABLE_NAME_SPENT_TRANSACTIONS +
                       " GROUP BY " + COLUMN_CATEGORY_SPENT_TRANSACTIONS;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }






}