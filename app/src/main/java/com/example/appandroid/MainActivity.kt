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
import android.webkit.WebView
import android.widget.*
import com.example.appandroid.Utils
import com.google.zxing.integration.android.IntentIntegrator

class MainActivity : AppCompatActivity() {

    //menus activos e inactivos
    private var menuUserAbierto :Boolean = false
    private var menuAbierto: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Datos del usuario
        val txtDireccion: TextView = findViewById(R.id.txtDireccion)
        val txtEstadoServicio: TextView = findViewById(R.id.txtEstadoServicio)
        val txtMesesAdeudados: TextView = findViewById(R.id.txtMesesAdeudados)
        val txtAdudoMes: TextView = findViewById(R.id.txtAdudoMes)
        val txtConsumoMes: TextView = findViewById(R.id.txtConsumoMes)
        val txtConsumoPromedio: TextView = findViewById(R.id.txtConsumoPromedio)
        val txtProximoVencimiento: TextView = findViewById(R.id.txtProximoVencimiento)
        val txtTipoContrato: TextView = findViewById(R.id.txtTipoContrato)
        val txtAdeudoTotal: TextView = findViewById(R.id.txtAdeudoTotal)
        val txtNombre: TextView = findViewById(R.id.txtNombre)


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

        //Menu lateral
        menuServ.visibility = View.VISIBLE

        //................SPINNER................//
        val SpinCuentas: Spinner = findViewById(R.id.ddlCuenta)

        HacerPostCuentas(this) { cuentasList ->
            // Aquí se recibe la lista de cuentas
            println("Cuentas recibidas: $cuentasList")

            val adapter = ArrayAdapter(
                this, // Contexto (Activity o Fragment)
                android.R.layout.simple_spinner_item, // Layout para los elementos del Spinner
                cuentasList // La lista de datos
            )
            //Diseño
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            SpinCuentas.adapter = adapter //Adaptador
        }


        //................SPINNER................//


        menuUser.translationX = screenWidth.toFloat();
        menuUser.visibility = View.VISIBLE

        // Cierra el menú al iniciar la app
        menuServ.translationX = -screenWidth.toFloat()
        menuServ.visibility = View.VISIBLE

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
        intentNavegar.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK;
        startActivity(intentNavegar)
        finish()
    }

}
