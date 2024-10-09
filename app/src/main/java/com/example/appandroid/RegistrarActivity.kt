package com.example.appandroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appandroid.Utils

class RegistrarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Inicio del codigo
        //
        //
        //variable para guardar Id del boton para ir a Inicio de sesion
        val btn_regresar: TextView = findViewById(R.id.textRegresarALogin)
        //Ids de los botones de ojo para ver contraseña
        val btnVer1: ImageButton = findViewById(R.id.imgBtnOjo1)
        val btnVer2: ImageButton = findViewById(R.id.imgBtnOjo2)
        //Ids de los textPassword
        val txtPass1: EditText = findViewById(R.id.textPass)
        val txtPass2: EditText = findViewById(R.id.textConfirmPass)
        //Variables Boleanas para ver u ocultar la contraseña
        var passVisible1: Boolean = false
        var passVisible2: Boolean = false

        //Evento para redirigirnos a la pantalla de login
        btn_regresar.setOnClickListener(){
            val intentNavegar = Intent(this, LoginActivity::class.java)
            startActivity(intentNavegar)
            finish()
        }

        //Eventos para ver y ocultar contraseña del campo de texto
        btnVer1.setOnClickListener(){
            passVisible1 = VerOcultarPass(btnVer1, txtPass1, passVisible1)
        }
        btnVer2.setOnClickListener(){
            passVisible2 = VerOcultarPass(btnVer2, txtPass2, passVisible2)
        }
    }
}