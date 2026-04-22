package com.example.lab07_sqlite.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "student_management.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_STUDENT = "student";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_STUDENT_CODE = "student_code";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
    public static final String COLUMN_MAJOR = "major";
    public static final String COLUMN_GPA = "gpa";

    private static final String CREATE_TABLE_STUDENT =
            "CREATE TABLE " + TABLE_STUDENT + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_STUDENT_CODE + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_FULL_NAME + " TEXT NOT NULL, " +
                    COLUMN_DATE_OF_BIRTH + " TEXT NOT NULL, " +
                    COLUMN_MAJOR + " TEXT NOT NULL, " +
                    COLUMN_GPA + " REAL NOT NULL" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STUDENT);
        seedData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        onCreate(db);
    }

    private void seedData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_STUDENT + " (" +
                COLUMN_STUDENT_CODE + ", " + COLUMN_FULL_NAME + ", " +
                COLUMN_DATE_OF_BIRTH + ", " + COLUMN_MAJOR + ", " + COLUMN_GPA + ") VALUES " +
                "('SV001', 'Nguyen Van An', '15/03/2003', 'Cong nghe thong tin', 3.5)");

        db.execSQL("INSERT INTO " + TABLE_STUDENT + " (" +
                COLUMN_STUDENT_CODE + ", " + COLUMN_FULL_NAME + ", " +
                COLUMN_DATE_OF_BIRTH + ", " + COLUMN_MAJOR + ", " + COLUMN_GPA + ") VALUES " +
                "('SV002', 'Tran Thi Binh', '22/07/2002', 'Ke toan', 3.8)");

        db.execSQL("INSERT INTO " + TABLE_STUDENT + " (" +
                COLUMN_STUDENT_CODE + ", " + COLUMN_FULL_NAME + ", " +
                COLUMN_DATE_OF_BIRTH + ", " + COLUMN_MAJOR + ", " + COLUMN_GPA + ") VALUES " +
                "('SV003', 'Pham Van Cuong', '10/12/2003', 'Quan tri kinh doanh', 3.2)");
    }
}
