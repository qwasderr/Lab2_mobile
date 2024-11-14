package com.example.lab2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "production.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "wheat_production";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_PRODUCTION = "production";
    public static final String COLUMN_VALUE = "value";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_YEAR + " INTEGER," +
                COLUMN_PRODUCTION + " INTEGER," +
                COLUMN_VALUE + " REAL)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getHighProductionYears() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_YEAR + ", " + COLUMN_PRODUCTION +
                " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_PRODUCTION + " > 25000000";
        return db.rawQuery(query, null);
    }
    public double getAverageValue() {
        SQLiteDatabase db = this.getReadableDatabase();
        double averageValue = 0.0;

        String query = "SELECT AVG(" + COLUMN_VALUE + ") FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            averageValue = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return averageValue;
    }
    public void importDataFromCSV(Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_NAME);

        try (InputStream is = context.getAssets().open("data.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            db.beginTransaction();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && !parts[0].equals("year")) {
                    int year = Integer.parseInt(parts[0]);
                    int production = Integer.parseInt(parts[1]);
                    double value = Double.parseDouble(parts[2]);
                    db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?)",
                            new Object[]{year, production, value});
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
