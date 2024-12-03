package com.example.appandroid

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.animation.TranslateAnimation
import android.webkit.WebView
import android.widget.*
import com.example.appandroid.Utils
import androidx.core.content.ContextCompat

class AgregarCuentaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_cuenta)


        //Direccion txtview
        val etDireccion = findViewById<TextView>(R.id.etDireccion)

        //Spinner Tipo de contrato
        val spinner: Spinner = findViewById(R.id.spTipoContrato)
        val tipo_contrato = arrayOf("Comercial", "Domestico")

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipo_contrato)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter


        //===============================================//
        // Colores radioBtn y definir cual se selecciono //
        //===============================================//

        // Obtén los colores desde el archivo colors.xml
        val activoColor = ContextCompat.getColor(this, R.color.white)
        val inactivoColor = ContextCompat.getColor(this, R.color.blue1)

        // Crea un ColorStateList con los colores obtenidos
        val activoColorStateList = ColorStateList.valueOf(activoColor)
        val inactivoColorStateList = ColorStateList.valueOf(inactivoColor)

        // Establece el color en los RadioButtons
        val rbActivo: RadioButton = findViewById(R.id.rbActivo)
        val rbInactivo: RadioButton = findViewById(R.id.rbInactivo)

        //Lo activamos por primera vezx con su color
        rbActivo.buttonTintList = activoColorStateList


        rbActivo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rbActivo.buttonTintList = activoColorStateList
                rbInactivo.buttonTintList = inactivoColorStateList
            }
        }

        rbInactivo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rbInactivo.buttonTintList = activoColorStateList
                rbActivo.buttonTintList = inactivoColorStateList
            }
        }

        //===============================================//
        //     Crear cuenta al hacer clic al boton       //
        //===============================================//
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)

        btnCrearCuenta.setOnClickListener(){
            val Estado = rbEstadoCuenta()
            val tipoContrato = spinner.selectedItem.toString()

            crearServicio(this, Estado, tipoContrato, etDireccion.text.toString()) { respuesta ->
                if (respuesta == "0"){
                    showPopup(this, "No deben existir campos vacíos")
                } else if (respuesta == "1") {
                    showPopupSuccess(this, "Se ha creado la cuenta")
                    RegresarInicio()
                } else {
                    showPopup(this, respuesta)
                }

            }

        }
        val btnRegresar = findViewById<ImageView>(R.id.btnRegresar)
        btnRegresar.setOnClickListener(){
            RegresarInicio()
        }

    }
    fun rbEstadoCuenta():String{
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        // Obtener el ID del RadioButton seleccionado
        val selectedId = radioGroup.checkedRadioButtonId

        // Verifica cuál RadioButton está seleccionado y obtén su texto
        val selectedRadioButton = findViewById<RadioButton>(selectedId)
        val selectedText = selectedRadioButton.text.toString()
        return selectedText
    }

    private fun RegresarInicio(){
        val intentNavegar = Intent(this, MainActivity::class.java)
        intentNavegar.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK;
        startActivity(intentNavegar)
        finish()
    }
}