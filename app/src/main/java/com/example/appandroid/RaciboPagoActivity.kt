package com.example.appandroid

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
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

        val etNumeroTarjeta = findViewById<EditText>(R.id.etNumeroTarjeta)
        val etNombreTitular = findViewById<EditText>(R.id.etNombreTitular)
        val etFechaVencimiento = findViewById<EditText>(R.id.etFechaVencimiento)
        val etCVV = findViewById<EditText>(R.id.etCVV)

        var btnCrearValid: Boolean = false
        var txt1: Boolean = false
        var txt2: Boolean = false
        var txt3: Boolean = false

        //Validacion numero tarjetas
        etNumeroTarjeta.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                if (editable == null) return

                // Remove any non-digit characters or spaces
                val cleanString = editable.toString().replace(" ", "").replace(Regex("[^0-9]"), "")

                // Add space every 4 characters
                val formattedString = StringBuilder()
                for (i in cleanString.indices) {
                    if (i > 0 && i % 4 == 0) {
                        formattedString.append(" ")
                    }
                    formattedString.append(cleanString[i])
                }

                // Set the formatted string back to the EditText
                if (editable.toString() != formattedString.toString()) {
                    etNumeroTarjeta.removeTextChangedListener(this) // Remove listener to avoid infinite loop
                    etNumeroTarjeta.setText(formattedString.toString())
                    etNumeroTarjeta.setSelection(formattedString.length) // Move the cursor to the end
                    etNumeroTarjeta.addTextChangedListener(this) // Add listener back
                }

                // Validate minimum length (19 characters including spaces)
                if (formattedString.length < 19) {
                    etNumeroTarjeta.error = "Debe contener al menos 19 caracteres (incluyendo espacios)"
                    txt1 = false
                } else {
                    etNumeroTarjeta.error = null
                    txt1 = true
                }
            }
        })

        //validacion de fecha de vencimiento
        etFechaVencimiento.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                if (editable == null) return

                // Evitar loops infinitos
                etFechaVencimiento.removeTextChangedListener(this)

                val input = editable.toString()
                val cleanInput = input.replace("/", "") // Remover cualquier "/" existente

                // Formatear solo si la entrada es menor o igual a 4 caracteres
                val formattedInput = StringBuilder()
                for (i in cleanInput.indices) {
                    if (i == 2) { // Agregar "/" después de los dos primeros caracteres
                        formattedInput.append("/")
                    }
                    formattedInput.append(cleanInput[i])
                }

                // Actualizar el texto del EditText
                val finalInput = formattedInput.toString()
                if (input != finalInput) {
                    etFechaVencimiento.setText(finalInput)
                    etFechaVencimiento.setSelection(finalInput.length) // Mover el cursor al final
                }

                // Validar longitud mínima (5 caracteres incluyendo "/")
                if (finalInput.length < 5) {
                    etFechaVencimiento.error = "Debe contener al menos 5 caracteres (formato MM/YY)"
                    txt2 = false
                } else {
                    etFechaVencimiento.error = null
                    txt2 = true
                }

                etFechaVencimiento.addTextChangedListener(this) // Reagregar el listener
            }
        })

        //Validacion del CVV
        etCVV.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                if (editable == null) return

                // Validar longitud mínima (3 caracteres)
                if (editable.length < 3) {
                    etCVV.error = "Debe contener al menos 3 caracteres"
                    txt3 = false
                } else {
                    etCVV.error = null // Limpiar error si es válido
                    txt3 = true
                }
            }
        })
        val spinnerBanco = findViewById<Spinner>(R.id.spinnerBanco)
        val bancos = listOf(
            "Seleccione un banco",
            "BBVA Bancomer",
            "Citibanamex",
            "Santander",
            "Banorte",
            "HSBC",
            "Scotiabank",
            "Banco Inbursa",
            "Banco Azteca",
            "Banregio",
            "Nu Mexico",
            "Banco Famsa",
            "Afirme",
            "Ci Banco",
            "Banco Finterra",
            "Bancoppel",
            "Compartamos"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            bancos
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBanco.adapter = adapter

        val btnPagar: Button = findViewById(R.id.btnPagar)
        btnPagar.setOnClickListener(){
            val numeroTarjeta = etNumeroTarjeta.text.toString()

            enviarPago(
                context = this,
                monto = tvData5.text.toString().toDouble(),
                mesesPagados = tvData4.text.toString().toInt(),
                fkCuenta = tvData3.text.toString().toInt(),
                fkTarjeta = numeroTarjeta
            ) { mensaje ->
                // Manejar la respuesta en la interfaz de usuario
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
            }
        }
    }
}