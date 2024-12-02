package com.example.appandroid

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RaciboPagoActivity : AppCompatActivity() {

    private lateinit var tvData1: TextView
    private lateinit var tvData2: TextView
    private lateinit var tvData3: TextView
    private lateinit var tvData4: TextView
    private lateinit var tvData5: TextView
    //Despues de pagar
    private lateinit var tvPagado1: TextView
    private lateinit var tvPagado2: TextView
    private lateinit var tvPagado3: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recibo_pago)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvData1 = findViewById(R.id.txDataTitular)
        tvData2 = findViewById(R.id.txDataDireccion)
        tvData3 = findViewById(R.id.txDataCuenta)
        tvData4 = findViewById(R.id.txDataMesesAdeudo)
        tvData5 = findViewById(R.id.txtDataAdeudoTotal)
        //Despues de pagar
        tvPagado1 = findViewById(R.id.txDataTransaccion)
        tvPagado2 = findViewById(R.id.txDataEstado)
        tvPagado3 = findViewById(R.id.txtDataFpago)

        //Obtener los datos del escaneo del QR
        tvData1.text = intent.getStringExtra("Data1")
        tvData2.text = intent.getStringExtra("Data2")
        tvData3.text = intent.getStringExtra("Data3")
        tvData4.text = intent.getStringExtra("Data4")
        tvData5.text = intent.getStringExtra("Data5")
    }
}