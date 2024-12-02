package com.example.appandroid

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.zxing.integration.android.IntentIntegrator

class LoginActivity : AppCompatActivity() {

    private lateinit var btnScan: ImageButton
    private lateinit var tvData1: String
    private lateinit var tvData2: String
    private lateinit var tvData3: String
    private lateinit var tvData4: String
    private lateinit var tvData5: String

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
        val txtPass: EditText = findViewById(R.id.txtPassword)
        val txtUser: EditText = findViewById(R.id.txtUser)
        var passVisible: Boolean = false

        btnScan = findViewById(R.id.btnQR)
        btnScan.setOnClickListener(){
            iniciarEscaneoQR()
        }

        //codigo que se ejecuta al hacer clic en el boton ingresar
        btnIngresar.setOnClickListener(){

            val username: String = txtUser.text.toString()
            val password: String = txtPass.text.toString()

            enviarLogin(this, username, password) { mensaje ->
                //Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                showPopup(this, mensaje)
            }

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
                val intent = Intent(this, RaciboPagoActivity::class.java)
                //Poner datos extras y mandarlos por el activity
                intent.putExtra("Data1", tvData1)
                intent.putExtra("Data2", tvData2)
                intent.putExtra("Data3", tvData3)
                intent.putExtra("Data4", tvData4)
                intent.putExtra("Data5", tvData5)
                startActivity(intent)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun mostrarDatos(datos: String) {
        //remplazamos las llaves espacios y saltos de linea
        val datosLimpios = datos
            .replace("{", "")
            .replace("}", "")
            .replace("\n", "")
            .replace("\r", "")
            .replace("\"", "")

        // Division de los datos
        val partes = datosLimpios.split(",")
        tvData1 = partes.getOrNull(0) ?: "No disponible"
        tvData2 = partes.getOrNull(1) ?: "No disponible"
        tvData3 = partes.getOrNull(2) ?: "No disponible"
        tvData4 = partes.getOrNull(3) ?: "No disponible"
        tvData5 = partes.getOrNull(4) ?: "No disponible"
    }
}