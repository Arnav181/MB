package com.example.hello

import android.os.Bundle
import android.view.Menu
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class HomeActivity : AppCompatActivity() {

    private lateinit var btnTasks: Button
    private lateinit var btnProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnTasks = findViewById(R.id.btnTasks)
        btnProfile = findViewById(R.id.btnProfile)

        // Load the TaskFragment by default
        replaceFragment(TaskFragment())

        btnTasks.setOnClickListener {
            replaceFragment(TaskFragment())
        }

        btnProfile.setOnClickListener {
            // Create ProfileFragment and pass the user data
            val profileFragment = ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString("NAME", intent.getStringExtra("NAME"))
                    putString("GENDER", intent.getStringExtra("GENDER"))
                    putString("DOB", intent.getStringExtra("DOB"))
                    putString("WEIGHT", intent.getStringExtra("WEIGHT"))
                    putString("AGE", intent.getStringExtra("AGE"))
                }
            }
            replaceFragment(profileFragment)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }
}
