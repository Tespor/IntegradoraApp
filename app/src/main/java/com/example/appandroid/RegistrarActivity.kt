package com.example.appandroid

import android.annotation.SuppressLint
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


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Para registrar
        val btnRegistrar: Button = findViewById(R.id.btnRegistrar)
        val txtUsuario: EditText = findViewById(R.id.txtUsuario)
        val txtNombre: EditText = findViewById(R.id.txtNombre)
        val txtCorreo: EditText = findViewById(R.id.txtCorreo)

        //variable para guardar Id del boton para ir a Inicio de sesion
        val btn_regresar: TextView = findViewById(R.id.textRegresarALogin)
        //Ids de los botones de ojo para ver contraseña
        val btnVer1: ImageButton = findViewById(R.id.imgBtnOjo1)
        val btnVer2: ImageButton = findViewById(R.id.imgBtnOjo2)
        //Ids de los textPassword
        val txtPass1: EditText = findViewById(R.id.txtContrasena)
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
        btnRegistrar.setOnClickListener(){
            //sacar los textos de los editText
            var tx1 = txtUsuario.text.toString()
            var tx2 = txtNombre.text.toString()
            var tx3 = txtCorreo.text.toString()
            var tx4 = txtPass1.text.toString()

            registrarUsuario(this, tx1, tx2, tx3, tx4){ respuesta ->
                if (respuesta == "1"){
                    showPopup(this, "Usuario registrado correctamente")
                    val intentNavLogin = Intent(this, LoginActivity::class.java)
                    startActivity(intentNavLogin)
                    finish()
                }else{
                    showPopup(this, respuesta)
                }
            }
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