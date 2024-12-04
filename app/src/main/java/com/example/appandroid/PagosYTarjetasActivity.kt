package com.example.appandroid

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log


class PagosYTarjetasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pagos_ytarjetas)

        // Para la tarjeta
        val tvBanco: TextView = findViewById(R.id.tvBanco)
        val tvNumeroTarjeta: TextView = findViewById(R.id.tvNumeroTarjeta)
        val tvVencimiento: TextView = findViewById(R.id.tvVencimiento)
        val tvNombre: TextView = findViewById(R.id.tvNombre)

        // Recuperar el valor del Intent
        // Recuperar valores del Intent
        val numeroCuenta = intent.getIntExtra("numeroCuenta", -1) // Valor por defecto: -1
        val mesesAduedo = intent.getIntExtra("mesesAduedo", -1)   // Valor por defecto: -1
        val totalAdeudo = intent.getFloatExtra("Total", -1f)      // Valor por defecto: -1f

        // Validar que los valores sean mayores a 0
        if (numeroCuenta > 0 && mesesAduedo > 0 && totalAdeudo > 0) {
            // Valores válidos
            println("Número de cuenta: $numeroCuenta")
            println("Meses de adeudo: $mesesAduedo")
            println("Total adeudo: $totalAdeudo")
        } else {
            // Valores inválidos: mostrar un mensaje o manejar el error
            println("Error: Uno o más valores no son válidos.")
            if (numeroCuenta <= 0) println("Número de cuenta inválido.")
            if (mesesAduedo <= 0) println("Meses de adeudo inválido.")
            if (totalAdeudo <= 0) println("Total adeudo inválido.")
        }


        //=======================================================================//
        //                Sacar las cuentas y llenar el spinner                  //
        //=======================================================================//
        //................SPINNER................//

        val spinnerTarjetas = findViewById<Spinner>(R.id.spinnerTarjetas)

        val tarjetas = mutableListOf<Tarjeta>()

        obtenerTarjetas(this) { tarjetasObtenidas ->
            if (tarjetasObtenidas.isNotEmpty()) {

                tarjetas.clear()
                tarjetas.addAll(tarjetasObtenidas)

                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    tarjetas.map { it.numeroTarjeta } // Solo los números de tarjeta
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerTarjetas.adapter = adapter

                // Configurar el listener del Spinner
                spinnerTarjetas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        // Obtener la tarjeta seleccionada
                        val tarjetaSeleccionada = tarjetas[position]

                        // Actualizar los TextView con los datos de la tarjeta seleccionada
                        tvBanco.text = tarjetaSeleccionada.banco
                        tvNumeroTarjeta.text = tarjetaSeleccionada.numeroTarjeta
                        tvVencimiento.text = tarjetaSeleccionada.fechaVencimiento
                        tvNombre.text = tarjetaSeleccionada.titular
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Opcional: Manejar el caso donde no se selecciona nada
                    }
                }
            } else {
                println("No se encontraron tarjetas.")
            }
        }

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



        //============================================================//
        //           Mostrar y Ocultar contenidos de tarjetas         //
        //============================================================//
        val btnTarjetas: Button = findViewById(R.id.btnTarjetas)
        val btnAgregar: ImageButton = findViewById(R.id.btnAgregar)
        val contentTarjetas: LinearLayout = findViewById(R.id.ContentTarjetas)
        val contentCrearTarjeta: LinearLayout = findViewById(R.id.ContentCrearTarjeta)
        val btnPagarCrear: Button = findViewById(R.id.btnPagarCrear)

        // Configurar clic para el botón "btnTarjetas"
        btnTarjetas.setOnClickListener {
            // Mostrar ContentTarjetas y ocultar ContentCrearTarjeta
            contentTarjetas.visibility = View.VISIBLE
            contentCrearTarjeta.visibility = View.GONE
            btnPagarCrear.text = "PAGAR"
        }

        // Configurar clic para el botón "btnAgregar"
        btnAgregar.setOnClickListener {
            // Mostrar ContentCrearTarjeta y ocultar ContentTarjetas
            contentCrearTarjeta.visibility = View.VISIBLE
            contentTarjetas.visibility = View.GONE
            btnPagarCrear.text = "CRAER"
        }
        //==================================//
        //            Validacion            //
        //============================== ===//
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






        //==================================//
        //           Crear tarjetas         //
        //============================== ===//
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

        //Cuando vaya a crear la tarjeta o a pagar
        btnPagarCrear.setOnClickListener(){
            val textoBoton = btnPagarCrear.text.toString()

            when (textoBoton) {
                "CRAER" -> {
                    if(txt1 == true && txt2 == true && txt3 == true){
                        btnCrearValid = true
                    } else {
                        showPopup(this, "Campos incompletos")
                        return@setOnClickListener
                    }

                    // Obtener los textos de los EditText
                    val numeroTarjeta = etNumeroTarjeta.text.toString().trim()
                    val nombreTitular = etNombreTitular.text.toString().trim()
                    val fechaVencimiento = etFechaVencimiento.text.toString().trim()
                    val cvv = etCVV.text.toString().trim()

                    // Obtener el texto seleccionado del Spinner
                    val bancoSeleccionado = spinnerBanco.selectedItem.toString().trim()

                    if (bancoSeleccionado == "Seleccione un banco"){
                        showPopup(this, "Seleccione un banco")
                        return@setOnClickListener
                    }

                    if (numeroTarjeta.isEmpty() || nombreTitular.isEmpty() || fechaVencimiento.isEmpty() || cvv.isEmpty()) {
                        Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("DatosTarjeta", "Número de Tarjeta: $numeroTarjeta")
                        Log.d("DatosTarjeta", "Nombre del Titular: $nombreTitular")
                        Log.d("DatosTarjeta", "Fecha de Vencimiento: $fechaVencimiento")
                        Log.d("DatosTarjeta", "CVV: $cvv")
                        Log.d("DatosTarjeta", "Banco Seleccionado: $bancoSeleccionado")

                        registrarTarjeta(
                            this,
                            tarjeta = numeroTarjeta,
                            titular = nombreTitular,
                            banco = bancoSeleccionado,
                            fechaVencimiento = fechaVencimiento,
                            cvv = cvv
                        ) { resultado ->
                            if (resultado == "1"){
                                showPopupSuccess(this, "Tarjeta agregada")
                                obtenerTarjetas(this) { tarjetasObtenidas ->
                                    if (tarjetasObtenidas.isNotEmpty()) {

                                        tarjetas.clear()
                                        tarjetas.addAll(tarjetasObtenidas)

                                        val adapter = ArrayAdapter(
                                            this,
                                            android.R.layout.simple_spinner_item,
                                            tarjetas.map { it.numeroTarjeta } // Solo los números de tarjeta
                                        )
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                        spinnerTarjetas.adapter = adapter

                                        // Configurar el listener del Spinner
                                        spinnerTarjetas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                                // Obtener la tarjeta seleccionada
                                                val tarjetaSeleccionada = tarjetas[position]

                                                // Actualizar los TextView con los datos de la tarjeta seleccionada
                                                tvBanco.text = tarjetaSeleccionada.banco
                                                tvNumeroTarjeta.text = tarjetaSeleccionada.numeroTarjeta
                                                tvVencimiento.text = tarjetaSeleccionada.fechaVencimiento
                                                tvNombre.text = tarjetaSeleccionada.titular
                                            }

                                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                                // Opcional: Manejar el caso donde no se selecciona nada
                                            }
                                        }
                                    } else {
                                        println("No se encontraron tarjetas.")
                                    }
                                }
                            } else if (resultado == "2"){
                                showPopup(this, "La tarjeta no pudo ser registrada")
                                return@registrarTarjeta
                            } else {
                                showPopup(this, resultado)
                            }
                        }
                    }

                }

                "PAGAR" -> {

                    val textoSeleccionado = spinnerTarjetas.selectedItem.toString()

                    enviarPago(
                        context = this,
                        monto = totalAdeudo.toDouble(),
                        mesesPagados = mesesAduedo,
                        fkCuenta = numeroCuenta,
                        fkTarjeta = textoSeleccionado
                    ) { resultado ->
                        if (resultado.mensaje == "1"){
                            showPopupSuccess(this, "Pago exitoso")
                            val intentInicio = Intent(this, MainActivity::class.java)
                            intentInicio.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK;
                            startActivity(intentInicio)
                            finish()
                            /*val txDataTransaccion: TextView = findViewById(R.id.txDataTransaccion)
                            val txDataEstado: TextView = findViewById(R.id.txDataEstado)
                            val txtDataFpago: TextView = findViewById(R.id.txtDataFpago)

                            txtDataFpago.text = obtenerFechaActual()
                            txDataTransaccion.text = resultado.transaccion
                            txDataEstado.text = "Liquidado"*/
                        } else {
                            showPopup(this, resultado.mensaje)
                        }
                    }
                }
                else -> showPopupSuccess(this, "Sin funcionamiento")
            }
        }


    }
}