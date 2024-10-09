package com.example.appandroid

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appandroid.Utils

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setContentView(R.layout.activity_login)
        val btnIngresar: Button = findViewById(R.id.btn_ingresar)
        val btnRegistrar: TextView = findViewById(R.id.txtregistrar)
        val btnVer: ImageButton = findViewById(R.id.imgBtnOjo1)
        val txtPass: EditText = findViewById(R.id.editTextTextPassword)
        var passVisible: Boolean = false

        //codigo que se ejecuta al hacer clic en el boton ingresar
        btnIngresar.setOnClickListener(){
            val intentNavegar = Intent(this, MainActivity::class.java)
            startActivity(intentNavegar)
            finish()
        }
        btnRegistrar.setOnClickListener(){
            val intentNavegar = Intent(this, RegistrarActivity::class.java)
            startActivity(intentNavegar)
            finish()
        }
        btnVer.setOnClickListener(){
            passVisible = VerOcultarPass(btnVer, txtPass, passVisible)
        }

    }
}