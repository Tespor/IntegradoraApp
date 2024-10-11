package com.example.appandroid

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.appandroid.Utils
import com.google.zxing.integration.android.IntentIntegrator

class MainActivity : AppCompatActivity() {

    private lateinit var btnScan: Button
    private lateinit var tvData1: TextView
    private lateinit var tvData2: TextView
    private lateinit var tvData3: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnScan = findViewById(R.id.btnScan)
        tvData1 = findViewById(R.id.tvData1)
        tvData2 = findViewById(R.id.tvData2)
        tvData3 = findViewById(R.id.tvData3)

        btnScan.setOnClickListener {
            iniciarEscaneoQR()
        }
        val btnLogOut = findViewById<ImageView>(R.id.user_button)

        btnLogOut.setOnClickListener(){
            val intentNavegar = Intent(this, LoginActivity::class.java)
            startActivity(intentNavegar)

        }
    }

    private fun iniciarEscaneoQR() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE) // Solo QR
        integrator.setPrompt("Escanea un código QR \n \n")
        integrator.setCameraId(0) // Cámara trasera
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(false)
        integrator.initiateScan() //Iniciar la cámara
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
            } else {
                mostrarDatos(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun mostrarDatos(datos: String) {
        //remplazamos las llaves
        val datosLimpios = datos
            .replace("{", "")
            .replace("}", "")
            .replace("\n", "")
            .replace("\r", "")
            .replace("\"", "")

        // Divide los datos para mostrarlos en 3 TextViews
        val partes = datosLimpios.split(",")
        tvData1.text = partes.getOrNull(0) ?: "No disponible"
        tvData2.text = partes.getOrNull(1) ?: "No disponible"
        tvData3.text = partes.getOrNull(2) ?: "No disponible"
    }

}
