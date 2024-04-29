package com.example.cinefast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cinefast.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_SNACKS = "snacks";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE = "image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_SNACKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_IMAGE + " TEXT)";
        db.execSQL(createTable);
        insertInitialSnacks(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SNACKS);
        onCreate(db);
    }

    private void insertInitialSnacks(SQLiteDatabase db) {
        addSnack(db, "Popcorn", 5.0, "snack_popcorn");
        addSnack(db, "Nachos", 6.5, "snack_nachos");
        addSnack(db, "Soft Drink", 3.0, "snack_softdrink");
        addSnack(db, "Candy", 2.5, "snack_candy");
    }

    private void addSnack(SQLiteDatabase db, String name, double price, String image) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_IMAGE, image);
        db.insert(TABLE_SNACKS, null, values);
    }

    public List<Snack> getAllSnacks(Context context) {
        List<Snack> snacks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SNACKS, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                String imageName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));

                int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
                if (resId == 0) resId = R.drawable.app_logo;

                snacks.add(new Snack(name, price, resId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return snacks;
    }
}
