package com.example.hello

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.hello.MainActivity
import com.example.hello.R

class OpeningActivity :AppCompatActivity (){


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.opening_screen)

        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)


        button1.setOnClickListener(){
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener(){
            val intent = Intent(applicationContext,LoginActivity::class.java)
            startActivity(intent)
        }


    }

}