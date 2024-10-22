package com.example.hello

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var tvSelectedDOB: TextView
    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var editTextWeight: EditText
    private lateinit var editTextAge: EditText
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        supportActionBar?.title = "Health Tracker"
        dbHelper = DatabaseHelper(this)

        tvSelectedDOB = findViewById(R.id.tvSelectedDOB)
        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        radioGroup = findViewById(R.id.radioGroup)
        editTextWeight = findViewById(R.id.editTextWeight)
        editTextAge = findViewById(R.id.editTextAge)

        val btnSubmit: Button = findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            if (validateInputs()) {
                if (dbHelper.checkEmailExists(editTextEmail.text.toString())) {
                    Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show()
                } else {
                    val weight = editTextWeight.text.toString().toDoubleOrNull() // Convert to Double
                    val age = editTextAge.text.toString().toIntOrNull() // Convert to Int

                    if (weight == null) {
                        editTextWeight.error = "Invalid weight"
                        return@setOnClickListener
                    }

                    if (age == null) {
                        editTextAge.error = "Invalid age"
                        return@setOnClickListener
                    }

                    val id = dbHelper.insertUser(
                        editTextName.text.toString(),
                        editTextEmail.text.toString(),
                        editTextPassword.text.toString(),
                        getSelectedGender(),
                        tvSelectedDOB.text.toString(),
                        weight, // Pass Double weight
                        age // Pass Int age
                    )
                    if (id > 0) {
                        Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                        goToLoginActivity()
                    } else {
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        tvSelectedDOB.setOnClickListener { showDatePickerDialog() }
    }

    private fun validateInputs(): Boolean {
        if (editTextName.text.isEmpty()) {
            editTextName.error = "Name is required"
            return false
        }
        if (editTextEmail.text.isEmpty()) {
            editTextEmail.error = "Email is required"
            return false
        }
        if (editTextPassword.text.isEmpty()) {
            editTextPassword.error = "Password is required"
            return false
        }
        if (tvSelectedDOB.text == "Select Date of Birth") {
            tvSelectedDOB.error = "Please select a date of birth"
            return false
        }
        if (editTextWeight.text.isEmpty()) {
            editTextWeight.error = "Weight is required"
            return false
        }
        if (editTextAge.text.isEmpty()) {
            editTextAge.error = "Age is required"
            return false
        }
        return true
    }

    private fun getSelectedGender(): String {
        val selectedId = radioGroup.checkedRadioButtonId
        return if (selectedId != -1) {
            val radioButton: RadioButton = findViewById(selectedId)
            radioButton.text.toString()
        } else {
            "Not specified"
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update the TextView with the selected date
                tvSelectedDOB.text = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            }, year, month, day)
        datePickerDialog.show()
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
