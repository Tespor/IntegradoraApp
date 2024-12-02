package com.example.appandroid

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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

        //=======================================================================//
        //                Sacar las cuentas y llenar el spinner                  //
        //=======================================================================//
        //................SPINNER................//
        val SpinCuentas: Spinner = findViewById(R.id.ddlCuenta)

        obtenerCuentas(this) { cuentasList ->

            if (cuentasList.isNotEmpty()) {
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cuentasList)
                //Diseño
                adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
                SpinCuentas.adapter = adapter
            } else {
                val arrayDenull = arrayOf("Aun no existen cuentas")

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cuentasList)
                //Diseño
                adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
                SpinCuentas.adapter = adapter
            }
        }


        //=======================================================================//
        //              Llenar los datos de la cuenta en el menu                 //
        //=======================================================================//
        SpinCuentas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Aquí manejas la selección de un nuevo elemento
                val seleccion = parent.getItemAtPosition(position).toString()

                llenarDatosCuenta(this@MainActivity, seleccion.toInt(), { CuentaDatos ->
                    if (CuentaDatos != null) {
                        txtDireccion.text = CuentaDatos.direccion
                        txtEstadoServicio.text = CuentaDatos.estadoServicio
                        txtMesesAdeudados.text = CuentaDatos.mesesAdeudo.toString()
                        txtAdudoMes.text = CuentaDatos.adeudoMes
                        txtConsumoMes.text = CuentaDatos.consumoMesReciente
                        txtConsumoPromedio.text = CuentaDatos.consumoPromedio
                        txtProximoVencimiento.text = CuentaDatos.proximoVencimiento
                        txtTipoContrato.text = CuentaDatos.tipoContrato
                        txtAdeudoTotal.text = CuentaDatos.adeudoTotal
                        txtNombre.text = CuentaDatos.nombreCompleto
                    } else {
                        println("No se obtuvieron datos de la cuenta.")
                    }
                })

                Toast.makeText(this@MainActivity, "Seleccionaste: $seleccion", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d("Spinner", "Nada seleccionado")
            }
        }

        //=======================================================================//
        //           Detectar cambios en la selección y llenar datos             //
        //=======================================================================//
        //................SPINNER................//
        menuUser.translationX = screenWidth.toFloat();
        menuUser.visibility = View.VISIBLE

        // Cierra el menú al iniciar la app
        menuServ.translationX = -screenWidth.toFloat()
        menuServ.visibility = View.VISIBLE

        // Configuracion del clic para abrir y cerrar el menú con animación
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

        val btnAddCuenta = findViewById<Button>(R.id.btnAddCuenta);
        btnAddCuenta.setOnClickListener(){
            irACrearCuenta()
        }

    }

    private fun irACrearCuenta(){
        val intentAddCuenta = Intent(this, AgregarCuentaActivity::class.java)
        startActivity(intentAddCuenta)
    }

    private fun CerrarSesion(){
        val intentNavegar = Intent(this, LoginActivity::class.java)
        intentNavegar.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK;
        startActivity(intentNavegar)
        finish()
    }

}
