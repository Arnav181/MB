package com.example.hello

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "User.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "users"

        // Table Columns
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_GENDER = "gender"
        private const val COLUMN_DOB = "dob"
        private const val COLUMN_WEIGHT = "weight"
        private const val COLUMN_AGE = "age"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createuserstable = ("CREATE TABLE $TABLE_USERS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_EMAIL TEXT,"
                + "$COLUMN_PASSWORD TEXT,"
                + "$COLUMN_GENDER TEXT,"
                + "$COLUMN_DOB TEXT,"
                + "$COLUMN_WEIGHT REAL,"  // Change to REAL for numeric weight
                + "$COLUMN_AGE INTEGER)")  // Change to INTEGER for age
        db.execSQL(createuserstable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // Insert User into Database
    fun insertUser(name: String, email: String, password: String, gender: String, dob: String, weight: Double, age: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_GENDER, gender)
            put(COLUMN_DOB, dob)
            put(COLUMN_WEIGHT, weight)  // Change to Double
            put(COLUMN_AGE, age)        // Change to Int
        }
        Log.d("UserRegistration", "Inserted user ID: $values")
        return db.insert(TABLE_USERS, null, values)

    }

    // Check if user exists (for login)
    fun checkUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(email, password)
        )
        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    // Check if email already exists (for registration)
    fun checkEmailExists(email: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?",
            arrayOf(email)
        )
        val emailExists = cursor.count > 0
        cursor.close()
        return emailExists
    }

    // Get User Data
    fun getUserData(email: String): Cursor? {
        val db = this.readableDatabase

        return db.query(
            TABLE_USERS,
            null,
            "$COLUMN_EMAIL = ?",
            arrayOf(email),
            null,
            null,
            null
        )
    }
}
