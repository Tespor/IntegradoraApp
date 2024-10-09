package com.example.appandroid

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)
         val btnLogOut = findViewById<ImageView>(R.id.user_button)

          btnLogOut.setOnClickListener(){
              val intentNavegar = Intent(this, LoginActivity::class.java)
              startActivity(intentNavegar)

          }
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
        window.statusBarColor = Color.TRANSPARENT

    }
    }

