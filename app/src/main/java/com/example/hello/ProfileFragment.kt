package com.example.hello

import android.annotation.SuppressLint
import android.app.Fragment

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    private lateinit var tvName: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvDOB: TextView
    private lateinit var tvWeight: TextView
    private lateinit var tvAge: TextView

    private lateinit var databaseHelper: DatabaseHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Health Tracker"

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        tvName = view.findViewById(R.id.tvName)
        tvGender = view.findViewById(R.id.tvGender)
        tvDOB = view.findViewById(R.id.tvDOB)
        tvWeight = view.findViewById(R.id.tvWeight)
        tvAge = view.findViewById(R.id.tvAge)

        databaseHelper = DatabaseHelper(requireContext())

        // Get the email from the arguments instead of SharedPreferences
        val userEmail = arguments?.getString("user_email") ?: "default@example.com" // Use a default email if null
        fetchUserData(userEmail)

        return view
    }

    private fun fetchUserData(email: String) {
        val cursor: Cursor? = databaseHelper.getUserData(email)
        cursor?.let {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex("name")
                val genderIndex = it.getColumnIndex("gender")
                val dobIndex = it.getColumnIndex("dob")
                val weightIndex = it.getColumnIndex("weight")
                val ageIndex = it.getColumnIndex("age")

                Log.d("ProfileFragment", "Indices - Name: $nameIndex, Gender: $genderIndex, DOB: $dobIndex, Weight: $weightIndex, Age: $ageIndex")

                // Check if indices are valid
                if (nameIndex != -1 && genderIndex != -1 && dobIndex != -1 && weightIndex != -1 && ageIndex != -1) {
                    val name = it.getString(nameIndex)
                    val gender = it.getString(genderIndex)
                    val dob = it.getString(dobIndex)
                    val weight = it.getFloat(weightIndex)
                    val age = it.getInt(ageIndex)

                    // Set user data to the TextViews
                    tvName.text = "Name: $name"
                    tvGender.text = "Gender: $gender"
                    tvDOB.text = "Date of Birth: $dob"
                    tvWeight.text = if (weight > 0) "Weight: $weight kg" else "Weight: Not available"
                    tvAge.text = if (age > 0) "Age: $age years" else "Age: Not available"
                } else {
                    Log.e("ProfileFragment", "One or more column indices are -1. Check your query and column names.")
                }
            }
            it.close()
        }
    }
}

