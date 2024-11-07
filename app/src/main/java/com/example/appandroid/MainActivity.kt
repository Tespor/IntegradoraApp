package com.example.appandroid

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.webkit.WebView
import com.example.appandroid.Utils
import com.google.zxing.integration.android.IntentIntegrator

class MainActivity : AppCompatActivity() {

    private lateinit var btnScan: Button
    private lateinit var tvData1: TextView
    private lateinit var tvData2: TextView
    private lateinit var tvData3: TextView

    //menus activos e inactivos
    private var menuUserAbierto :Boolean = false
    private var menuAbierto: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnScan = findViewById(R.id.btnScan)
        tvData1 = findViewById(R.id.txDataTitular)
        tvData2 = findViewById(R.id.txDataCuenta)
        tvData3 = findViewById(R.id.txDataMensualidad)

        btnScan.setOnClickListener {
            iniciarEscaneoQR()
        }

        //Abrir y cerrar menu de Servicios
        val logoCompany = findViewById<ImageView>(R.id.logoCompany)
        val menuServ = findViewById<View>(R.id.layoutContenServ)
        //Obtengo las variables para el ancho de la pantalla
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        //variables para modificar el encabezado
        val boxLogoText = findViewById<View>(R.id.boxLogoText)
        val paddingFinal = (170 * resources.displayMetrics.density).toInt()

        //Menu de usuario
        val btnUserIcon = findViewById<ImageView>(R.id.btnUserIcon);
        // Cierra el menuUser al iniciar la app
        val menuUser = findViewById<View>(R.id.menuUser);

        menuUser.translationX = screenWidth.toFloat();

        // Cierra el menú al iniciar la app
        menuServ.translationX = -screenWidth.toFloat()

        // Configura el clic para abrir y cerrar el menú con animación
        logoCompany.setOnClickListener {
            //evita que se abra el menu usuario
            btnUserIcon.isEnabled = menuAbierto

            val targetX = if (menuAbierto) -screenWidth.toFloat() else 0f //else 0f es para desacer lo que se hizo en el if
            val paddingEnd = if (menuAbierto) 0 else paddingFinal

            //animacion del menu
            val animator = ObjectAnimator.ofFloat(menuServ, "translationX", targetX)
            animator.duration = 300
            animator.start()
            //animar header dando un padding
            val paddingAnimator = ValueAnimator.ofInt(boxLogoText.paddingEnd, paddingEnd)
            paddingAnimator.duration = 300
            paddingAnimator.addUpdateListener { animation ->
                val animatedPaddingEnd = animation.animatedValue as Int
                boxLogoText.setPadding(
                    boxLogoText.paddingLeft,
                    boxLogoText.paddingTop,
                    animatedPaddingEnd,
                    boxLogoText.paddingBottom
                )
            }
            paddingAnimator.start()

            if (menuUserAbierto){
                val moverX = screenWidth.toFloat();
                //animacion del menuUser moviendolo en X en un cierto tiempo
                val animatoMove = ObjectAnimator.ofFloat(menuUser, "translationX", moverX)
                animatoMove.duration = 300
                animatoMove.start()
                menuUserAbierto = !menuUserAbierto
            }

            menuAbierto = !menuAbierto
        }




        btnUserIcon.setOnClickListener() {
            val moverX = if (menuUserAbierto)
                                screenWidth.toFloat()
                         else 0f
            //animacion del menuUser moviendolo en X en un cierto tiempo
            val animatoMove = ObjectAnimator.ofFloat(menuUser, "translationX", moverX)
            animatoMove.duration = 300
            animatoMove.start()

            menuUserAbierto = !menuUserAbierto
        }

        //cerrar sesion
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion);

        btnCerrarSesion.setOnClickListener(){
            CerrarSesion();
        }
    }
    private fun CerrarSesion(){
        val intentNavegar = Intent(this, LoginActivity::class.java)
        startActivity(intentNavegar)
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

        // Division de los datos
        val partes = datosLimpios.split(",")
        tvData1.text = partes.getOrNull(0) ?: "No disponible"
        tvData2.text = partes.getOrNull(1) ?: "No disponible"
        tvData3.text = partes.getOrNull(2) ?: "No disponible"
    }

}
