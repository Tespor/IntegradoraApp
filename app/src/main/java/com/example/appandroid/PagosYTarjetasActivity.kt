package com.example.appandroid

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import android.widget.LinearLayout

class PagosYTarjetasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pagos_ytarjetas)

        val spinnerTarjetas = findViewById<Spinner>(R.id.spinnerTarjetas)

        val arrayDenull = arrayOf("Aun no existen cuentas")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayDenull)
        //Diseño
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        spinnerTarjetas.adapter = adapter



        //============================================================//
        //              Estilo Gradients y Borde redondeado           //
        //============================================================//
        val layout = findViewById<LinearLayout>(R.id.layoutTarjeta)

        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TL_BR, // Dirección del gradiente
            intArrayOf(
                ContextCompat.getColor(this, R.color.colorStart), // Color inicial
                ContextCompat.getColor(this, R.color.colorEnd)    // Color final
            )
        )
        gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT // Tipo de gradiente
        gradientDrawable.cornerRadius = 25f

        // Aplicando el gradiente al fondo del layout
        layout.background = gradientDrawable
    }
}