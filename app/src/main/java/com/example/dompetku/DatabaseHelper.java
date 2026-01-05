package com.example.dompetku;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "FinanceApp.db";
    public static final int DATABASE_VERSION = 2;

    public static final String TABLE_USERS = "users";
    public static final String COL_ID = "ID";
    public static final String COL_NAME = "NAME";
    public static final String COL_EMAIL = "EMAIL";
    public static final String COL_PASSWORD = "PASSWORD";

    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String COL_T_ID = "ID";
    public static final String COL_T_EMAIL = "USER_EMAIL";
    public static final String COL_T_AMOUNT = "AMOUNT";
    public static final String COL_T_TYPE = "TYPE";
    public static final String COL_T_DATE = "DATE";
    public static final String COL_T_DESC = "DESCRIPTION";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_PASSWORD + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                COL_T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_T_EMAIL + " TEXT, " +
                COL_T_AMOUNT + " DOUBLE, " +
                COL_T_TYPE + " TEXT, " +
                COL_T_DATE + " TEXT, " +
                COL_T_DESC + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db);
    }

    public boolean insertUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                " WHERE " + COL_EMAIL + "=? AND " + COL_PASSWORD + "=?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Cursor getUserData(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + "=?", new String[]{email});
    }

    public boolean insertTransaction(String email, double amount, String type, String date, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_T_EMAIL, email);
        values.put(COL_T_AMOUNT, amount);
        values.put(COL_T_TYPE, type);
        values.put(COL_T_DATE, date);
        values.put(COL_T_DESC, desc);
        return db.insert(TABLE_TRANSACTIONS, null, values) != -1;
    }

    public double getBalance(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalIn = 0, totalOut = 0;

        Cursor cIn = db.rawQuery("SELECT SUM(" + COL_T_AMOUNT + ") FROM " + TABLE_TRANSACTIONS +
                " WHERE " + COL_T_EMAIL + "=? AND " + COL_T_TYPE + "='IN'", new String[]{email});
        if(cIn.moveToFirst()) totalIn = cIn.getDouble(0);

        Cursor cOut = db.rawQuery("SELECT SUM(" + COL_T_AMOUNT + ") FROM " + TABLE_TRANSACTIONS +
                " WHERE " + COL_T_EMAIL + "=? AND " + COL_T_TYPE + "='OUT'", new String[]{email});
        if(cOut.moveToFirst()) totalOut = cOut.getDouble(0);

        cIn.close(); cOut.close();
        return totalIn - totalOut;
    }

    public List<Transaksi> getTransactionsFiltered(String email, String startDate) {
        List<Transaksi> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TRANSACTIONS +
                " WHERE " + COL_T_EMAIL + " = ? AND " + COL_T_DATE + " >= ? ORDER BY " + COL_T_DATE + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{email, startDate});

        if (cursor.moveToFirst()) {
            do {
                list.add(new Transaksi(
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Cursor getTotalPengeluaranPerKategori(String email, String startDate) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COL_T_DESC + " AS category, SUM(" + COL_T_AMOUNT + ") AS total " +
                "FROM " + TABLE_TRANSACTIONS +
                " WHERE " + COL_T_EMAIL + " = ? AND " + COL_T_DATE + " >= ? AND " + COL_T_TYPE + " = 'OUT' " +
                "GROUP BY " + COL_T_DESC;

        return db.rawQuery(query, new String[]{email, startDate});
    }
}