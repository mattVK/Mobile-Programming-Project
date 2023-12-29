package com.example.mobile_programming_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


class Transaction{

    Transaction(String transactionType, int id, int amount, String date, String category){
        this.transactionType = transactionType;
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }
    String transactionType;
    int id;
    int amount;
    String date;
    String category;
    

    
}

public class SQLDatabase extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "TransactionsLibrary.db";
    private static final int DATABASE_VERSION = 1;


    private static final String TABLE_NAME_SPENT_CATEGORIES = "spent_categories";
    private static final String COLUMN_CATEGORY_SPENT_CATEGORIES = "_category";

    private static final String COLUMN_LIMIT_SPENT_CATEGORIES = "limit_category";

    private static final String TABLE_NAME_BUDGET_CATEGORIES = "budget_categories";
    private static final String COLUMN_CATEGORY_BUDGET_CATEGORIES = "_category";

    private static final String COLUMN_LIMIT_BUDGET_CATEGORIES = "limit_category";

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
                COLUMN_CATEGORY_BUDGET_CATEGORIES + " TEXT PRIMARY KEY, " +
                COLUMN_LIMIT_BUDGET_CATEGORIES + " INTEGER );";

        String querySpentCategory = "CREATE TABLE " + TABLE_NAME_SPENT_CATEGORIES + " ( " +
                COLUMN_CATEGORY_SPENT_CATEGORIES + " TEXT PRIMARY KEY, " +
                COLUMN_LIMIT_SPENT_CATEGORIES + " INTEGER );";

        String queryBudget = "CREATE TABLE " + TABLE_NAME_BUDGET_TRANSACTIONS + " ( " +
                        COLUMN_ID_BUDGET_TRANSACTIONS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_AMOUNT_BUDGET_TRANSACTIONS + " INTEGER, " +
                        COLUMN_DATE_BUDGET_TRANSACTIONS + " DATE, " +
                        COLUMN_CATEGORY_BUDGET_TRANSACTIONS + " TEXT, " +
                        "FOREIGN KEY ("+ COLUMN_CATEGORY_BUDGET_TRANSACTIONS + ")" + " REFERENCES " + TABLE_NAME_BUDGET_CATEGORIES + "(" + COLUMN_CATEGORY_BUDGET_CATEGORIES + "));";
        String querySpent = " CREATE TABLE " + TABLE_NAME_SPENT_TRANSACTIONS + " ( " +
                        COLUMN_ID_SPENT_TRANSACTIONS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_AMOUNT_SPENT_TRANSACTIONS + " INTEGER, " +
                        COLUMN_DATE_SPENT_TRANSACTIONS + " DATE, " +
                        COLUMN_CATEGORY_SPENT_TRANSACTIONS + " TEXT, " +
                        "FOREIGN KEY ("+ COLUMN_CATEGORY_SPENT_TRANSACTIONS + ")" + " REFERENCES " + TABLE_NAME_SPENT_CATEGORIES + "(" + COLUMN_CATEGORY_SPENT_CATEGORIES + "));";

        db.execSQL(queryBudgetCategory);
        db.execSQL(querySpentCategory);
        db.execSQL(queryBudget);
        db.execSQL(querySpent);

        //set initial values for categories
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CATEGORY_SPENT_CATEGORIES, "Groceries");
        cv.put(COLUMN_LIMIT_SPENT_CATEGORIES, 1000000);
        db.insert(TABLE_NAME_SPENT_CATEGORIES, null, cv);
        cv.clear();


        cv.put(COLUMN_CATEGORY_SPENT_CATEGORIES, "Shopping");
        cv.put(COLUMN_LIMIT_SPENT_CATEGORIES, 1000000);
        db.insert(TABLE_NAME_SPENT_CATEGORIES, null, cv);
        cv.clear();

        cv.put(COLUMN_CATEGORY_SPENT_CATEGORIES, "Food & Drinks");
        cv.put(COLUMN_LIMIT_SPENT_CATEGORIES, 1000000);
        db.insert(TABLE_NAME_SPENT_CATEGORIES, null, cv);
        cv.clear();

        cv.put(COLUMN_CATEGORY_SPENT_CATEGORIES, "Transportation");
        cv.put(COLUMN_LIMIT_SPENT_CATEGORIES, 1000000);
        db.insert(TABLE_NAME_SPENT_CATEGORIES, null, cv);
        cv.clear();


        cv.put(COLUMN_CATEGORY_BUDGET_CATEGORIES, "M-Banking");
        db.insert(TABLE_NAME_BUDGET_CATEGORIES, null, cv);
        cv.clear();

        cv.put(COLUMN_CATEGORY_BUDGET_CATEGORIES, "Dana");
        db.insert(TABLE_NAME_BUDGET_CATEGORIES, null, cv);
        cv.clear();

        cv.put(COLUMN_CATEGORY_BUDGET_CATEGORIES, "OVO");
        db.insert(TABLE_NAME_BUDGET_CATEGORIES, null, cv);
        cv.clear();

        cv.put(COLUMN_CATEGORY_BUDGET_CATEGORIES, "ShopeePay");
        db.insert(TABLE_NAME_BUDGET_CATEGORIES, null, cv);
        cv.clear();


    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BUDGET_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SPENT_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BUDGET_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SPENT_TRANSACTIONS);


        onCreate(db);
    }


    //add budget transactions by calling this. (date format: YYYY-MM-DD)
    void addBudgetTransactions(int amount, String date, String category){
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
    void addSpentTransactions(int amount, String date, String category){
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
    Cursor getSumOfSpentCategories(){
        String query = "SELECT SUM(" + COLUMN_AMOUNT_SPENT_TRANSACTIONS  + ") FROM " + TABLE_NAME_SPENT_TRANSACTIONS;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor getSumOfBudgetCategories(){
        String query = "SELECT SUM(" + COLUMN_AMOUNT_BUDGET_TRANSACTIONS  + ") FROM " + TABLE_NAME_BUDGET_TRANSACTIONS;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    //sum of categories grouped for main screen.

    Cursor getSumOfSpentCategoriesByCategory(){
        String query = "SELECT "+ COLUMN_CATEGORY_SPENT_TRANSACTIONS+"," +"SUM(" + COLUMN_AMOUNT_SPENT_TRANSACTIONS  + ")" + ", COUNT(*) " + "FROM " + TABLE_NAME_SPENT_TRANSACTIONS +
                       " GROUP BY " + COLUMN_CATEGORY_SPENT_TRANSACTIONS;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor getAllSpentCategories(){
        String query = "SELECT " + COLUMN_CATEGORY_SPENT_CATEGORIES + " FROM " + TABLE_NAME_SPENT_CATEGORIES;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }

    Cursor getAllBudgetCategories(){
        String query = "SELECT " + COLUMN_CATEGORY_BUDGET_CATEGORIES + " FROM " + TABLE_NAME_BUDGET_CATEGORIES;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor getAllBudgetCategoriesSortedByDate(){
        String query = "SELECT " + "* " + "FROM " + TABLE_NAME_BUDGET_TRANSACTIONS + " ORDER BY " + COLUMN_DATE_BUDGET_TRANSACTIONS + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    Cursor getAllSpentCategoriesSortedByDate(){
        String query = "SELECT " + "* " + "FROM " + TABLE_NAME_SPENT_TRANSACTIONS + " ORDER BY " + COLUMN_DATE_SPENT_TRANSACTIONS + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor getAllTransactionsSortedByDate(){
        String query = "SELECT * FROM " + TABLE_NAME_BUDGET_TRANSACTIONS +
                       " UNION ALL " +
                       "SELECT * FROM " + TABLE_NAME_SPENT_TRANSACTIONS +
                       " ORDER BY " + COLUMN_DATE_BUDGET_TRANSACTIONS + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }

    Cursor getAllBudgetTransactionsSortedByDate(){
        String query = "SELECT * FROM " + TABLE_NAME_BUDGET_TRANSACTIONS +
                " ORDER BY " + COLUMN_DATE_BUDGET_TRANSACTIONS + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }
    Cursor getAllSpentTransactionsSortedByDate(){
        String query = "SELECT * FROM " + TABLE_NAME_SPENT_TRANSACTIONS +
                " ORDER BY " + COLUMN_DATE_SPENT_TRANSACTIONS + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }

    Cursor getSumOfBudgetCategoriesMonthly(){
        Calendar calendar = Calendar.getInstance();


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());



        String query = "SELECT SUM("+ COLUMN_AMOUNT_BUDGET_TRANSACTIONS+") FROM " + TABLE_NAME_BUDGET_TRANSACTIONS +
                       "  WHERE strftime('%Y-%m',"+COLUMN_DATE_BUDGET_TRANSACTIONS+  " ) =" + "'"+formattedDate+"'" +";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor getSumOfSpentCategoriesMonthly(){
        Calendar calendar = Calendar.getInstance();


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());



        String query = "SELECT SUM("+COLUMN_AMOUNT_SPENT_TRANSACTIONS+") FROM " + TABLE_NAME_SPENT_TRANSACTIONS +
                " WHERE strftime('%Y-%m'," + COLUMN_DATE_SPENT_TRANSACTIONS +")" + " =" + "'"+ formattedDate + "'";

        Log.d("DATE", query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor getSumOfSpentCategoriesByCategoryMonthly(){
        Calendar calendar = Calendar.getInstance();


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());
        String query = "SELECT "+ COLUMN_CATEGORY_SPENT_TRANSACTIONS+"," +" SUM(" + COLUMN_AMOUNT_SPENT_TRANSACTIONS  + ")" + ", COUNT(*) " + "FROM " + TABLE_NAME_SPENT_TRANSACTIONS +
                " WHERE strftime('%Y-%m',"+COLUMN_DATE_SPENT_TRANSACTIONS+  ")= " + "'" + formattedDate + "'" +
                " GROUP BY " + COLUMN_CATEGORY_SPENT_TRANSACTIONS;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void deleteRow(long id, String transactionType){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = null;
        if (transactionType.equals("spent")){
            query = "DELETE FROM " + TABLE_NAME_SPENT_TRANSACTIONS + " WHERE " + COLUMN_ID_SPENT_TRANSACTIONS + " = " + id;
        }
        else{
            query = "DELETE FROM " + TABLE_NAME_BUDGET_TRANSACTIONS + " WHERE " + COLUMN_ID_BUDGET_TRANSACTIONS + " = " + id;
        }
        if (query != null){
            db.execSQL(query);
        }


        db.close();


    }

    Cursor getCategoriesAndLimits(){

        String query = "SElECT " + COLUMN_CATEGORY_SPENT_TRANSACTIONS + ", " +
                "SUM(" + COLUMN_AMOUNT_SPENT_TRANSACTIONS +"), " + COLUMN_LIMIT_SPENT_CATEGORIES + " FROM " + TABLE_NAME_SPENT_TRANSACTIONS +
                " JOIN " + TABLE_NAME_SPENT_CATEGORIES + " ON " + COLUMN_CATEGORY_SPENT_TRANSACTIONS + " = " + COLUMN_CATEGORY_SPENT_CATEGORIES +
                " GROUP BY " + COLUMN_CATEGORY_SPENT_TRANSACTIONS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }
}
