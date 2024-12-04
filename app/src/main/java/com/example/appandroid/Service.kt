package com.example.appandroid

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import android.util.Log
import java.util.concurrent.Executors
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import org.json.JSONArray
import org.json.JSONException
import java.net.URL

//const val url_base: String = "http://192.168.137.124/Integradora/";//Utt
//const val url_base: String = "http://192.168.117.151/Integradora/";//Casa
const val url_base: String = "http://192.168.0.104/Integradora/";//Chamba
const val sub_url: String = "${url_base}clientePhp/";

object Endpoints {
    const val login = "${url_base}Login.php"
    const val Registrar = "${url_base}registro.php"
    const val clienteTarjetas = "${sub_url}clienteTarjetas.php"
    const val datosCuenta = "${sub_url}datosCuenta.php"
    const val obtenerCuentas = "${sub_url}obtenerCuentas.php"
    const val registrarCuentas = "${sub_url}registrarCuentas.php"
    const val pagos = "${sub_url}Pagos.php"
}

var IdDeUsuario = "";


//Funcion para enviar datos al login del php WEB
fun enviarLogin(context: Context, username: String, password: String, callback: (String) -> Unit) {

    var respuesta = ""

    val client = OkHttpClient()

    // Crear un objeto JSON
    val jsonObject = JSONObject()
    jsonObject.put("nombre_usuario", username)
    jsonObject.put("contrasena", password)

    // Crear el cuerpo de la solicitud con JSON
    val requestBody = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    val request = Request.Builder()
        .url(Endpoints.login)
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            println("Fallo:" + e.message)
            (context as Activity).runOnUiThread {
                callback("Fallo: ${e.message}")
            }
        }

        override fun onResponse(call: Call, response: Response) {
            response.body?.string()?.let {
                // Aqui se procesa la respuesta y lo necesario para el login
                try {
                    val jsonResponse = JSONObject(it)
                    val status = jsonResponse.getString("status")

                    val mensaje = if (status == "success") {

                        val role = jsonResponse.getString("role")
                        IdDeUsuario = jsonResponse.getString("id")

                        // Verifica el rol de usuario
                        when (role) {
                            "admin" -> {
                                "Tipo de usuario no valido"
                            }
                            "cliente" -> {
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                                return //retorno vacío
                            }
                            else -> "Rol desconocido"
                        }
                    } else {
                        // Manejar error
                        jsonResponse.getString("message")
                    }

                    //mostrar mensaje en la UI
                    (context as Activity).runOnUiThread {
                        callback(mensaje)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    print("Se genero una excepcion" + e.message)
                    (context as Activity).runOnUiThread{
                        callback("Se genero una excepcion" + e.message)
                    }
                }
            }
        }
    })
}
//Funcion para registrar un usuario
fun registrarUsuario(context: Context, usuario: String, nombreCompleto: String, correo: String, contrasena: String, respuesta: (String) -> Unit) {
    if (usuario == "" || nombreCompleto =="" || correo == "" || contrasena == ""){
        (context as Activity).runOnUiThread {
            respuesta("No deben existir campos vacios")
        }
        return
    }

    val client = OkHttpClient()

    // JSON de los datos a enviar
    val jsonObject = JSONObject()
    jsonObject.put("username", usuario)
    jsonObject.put("fullname", nombreCompleto)
    jsonObject.put("correo", correo)
    jsonObject.put("password", contrasena)

    // Cuerpo de la solicitud en JSON
    val requestBody = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    // Solicitud HTTP POST
    val request = Request.Builder()
        .url(Endpoints.Registrar)
        .post(requestBody)
        .build()

    // Realizar la solicitud en un hilo secundario
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseData = response.body?.string()
                if (responseData != null) {
                    val jsonResponse = JSONObject(responseData)
                    val status = jsonResponse.getString("status")
                    val message = jsonResponse.getString("message")

                    if (status == "success") {
                        println("Usuario registrado con éxito: $message")
                        (context as Activity).runOnUiThread {
                            respuesta("1")
                        }
                    } else {
                        println("Error al registrar usuario: $message")
                        (context as Activity).runOnUiThread {
                            respuesta("Error al registrar usuario: $message")
                        }
                    }
                }
            } else {
                println("Respuesta fallida del servidor")
                (context as Activity).runOnUiThread {
                    respuesta("Respuesta fallida del servidor")
                }
            }
        }
    })
}

//=======================================================================//
//                       Funciones para cuentas                          //
//=======================================================================//

fun obtenerCuentas(context: Context, respuesta: (List<Int>) -> Unit) {
    val client = OkHttpClient()

    // URL de la solicitud POST
    val url = Endpoints.obtenerCuentas
    println(url)

    val jsonObject = JSONObject()
    jsonObject.put("id", IdDeUsuario.toInt())
    // Aquí no enviamos ningún dato en el cuerpo del POST
    val requestBody = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    // Solicitud HTTP POST
    val request = Request.Builder()
        .url(url) // URL a la que se realiza el POST
        .post(requestBody) // Enviamos un cuerpo vacío
        .build()

    // Realizar la solicitud en un hilo secundario
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            (context as Activity).runOnUiThread {
                respuesta(emptyList()) // En caso de fallo, devolver lista vacía
            }
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseData = response.body?.string()
                if (responseData != null) {
                    try {
                        // Escribimos la respuesta en el Log para ver si funciona
                        println("ID usuario: $IdDeUsuario")
                        Log.d("HacerPostCuentas", "Respuesta del servidor: $responseData")

                        // Aquí parseamos la respuesta que es un JSON con la lista de cuentas
                        val jsonArray = JSONArray(responseData)
                        val cuentasList = mutableListOf<Int>()

                        for (i in 0 until jsonArray.length()) {
                            cuentasList.add(jsonArray.getInt(i))
                        }

                        // Devolvemos la lista de cuentas
                        (context as Activity).runOnUiThread {
                            respuesta(cuentasList)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        (context as Activity).runOnUiThread {
                            respuesta(emptyList()) // En caso de error en el parseo, devolver lista vacía
                        }
                    }
                }
            } else {
                Log.e("HacerPostCuentas", "Error en la respuesta del servidor")
                (context as Activity).runOnUiThread {
                    respuesta(emptyList()) // Devolver lista vacía en caso de error
                }
            }
        }
    })
}

fun llenarDatosCuenta(context: Context, idCuenta: Int, callback: (CuentaDatos?) -> Unit) {
    val client = OkHttpClient()

    // Crear un objeto JSON con el idCuenta
    val jsonObject = JSONObject()
    jsonObject.put("idCuenta", idCuenta)

    // Crear el cuerpo de la solicitud
    val requestBody = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    // Configurar la solicitud POST
    val request = Request.Builder()
        .url(Endpoints.datosCuenta)
        .post(requestBody)
        .build()

    // Realizar la solicitud
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            println("Fallo al realizar la solicitud: ${e.message}")
            (context as Activity).runOnUiThread {
                callback(null) // Devolver null en caso de fallo
            }
        }

        override fun onResponse(call: Call, response: Response) {
            response.body?.string()?.let {
                try {
                    // Procesar la respuesta JSON
                    val jsonResponse = JSONObject(it)
                    if (jsonResponse.has("error")) {
                        // Manejar el error de la respuesta
                        println("Error desde el servidor: ${jsonResponse.getString("error")}")
                        (context as Activity).runOnUiThread {
                            callback(null)
                        }
                    } else {
                        // Mapear los datos a un objeto CuentaDatos
                        val cuentaDatos = CuentaDatos(
                            estadoServicio = jsonResponse.getString("estado_servicio"),
                            adeudoMes = jsonResponse.getString("adeudo_mes"),
                            tipoContrato = jsonResponse.getString("tipo_contrato"),
                            direccion = jsonResponse.getString("direccion"),
                            consumoPromedio = jsonResponse.getString("consumo_promedio"),
                            consumoMesReciente = jsonResponse.getString("consumo_mes_reciente"),
                            proximoVencimiento = jsonResponse.getString("proximo_vencimiento"),
                            adeudoTotal = jsonResponse.getString("adeudo_total"),
                            mesesAdeudo = jsonResponse.getString("meses_adeudo"),
                            nombreCompleto = jsonResponse.getString("nombre_completo")
                        )

                        // Devolver los datos en la UI principal
                        (context as Activity).runOnUiThread {
                            callback(cuentaDatos)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    println("Error al procesar la respuesta: ${e.message}")
                    (context as Activity).runOnUiThread {
                        callback(null)
                    }
                }
            }
        }
    })
}

// Clase de datos para mapear la respuesta
data class CuentaDatos(
    val estadoServicio: String,
    val adeudoMes: String,
    val tipoContrato: String,
    val direccion: String,
    val consumoPromedio: String,
    val consumoMesReciente: String,
    val proximoVencimiento: String,
    val adeudoTotal: String,
    val mesesAdeudo: String,
    val nombreCompleto: String
)

//=======================================================================//
//                         Crear Nueva Cuenta                            //
//=======================================================================//
fun crearServicio(
    context: Context,
    estadoServicio: String,
    tipoContrato: String,
    direccion: String,
    respuesta: (String) -> Unit
) {
    // Validación básica de campos vacíos
    if (estadoServicio == "" || tipoContrato == "" || direccion == "") {
        (context as Activity).runOnUiThread {
            respuesta("0")
        }
        return
    }

    val client = OkHttpClient()

    // Crear el objeto JSON con los datos a enviar
    val jsonObject = JSONObject()
    jsonObject.put("estadoServicio", estadoServicio)
    jsonObject.put("tipoContrato", tipoContrato)
    jsonObject.put("direccion", direccion)
    jsonObject.put("id", IdDeUsuario.toInt())

    // Cuerpo de la solicitud en JSON
    val requestBody = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    println("Este es el endpoin: ${Endpoints.registrarCuentas}")
    // Configurar la solicitud HTTP POST
    val request = Request.Builder()
        //.url("http://192.168.1.13/Integradora/clientePhp/registrarCuentas.php")
        .url(Endpoints.registrarCuentas)
        .post(requestBody)
        .build()

    // Realizar la solicitud en un hilo secundario
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            println("Error al conectar: ${e.message}")
            (context as Activity).runOnUiThread {
                respuesta("Error al conectar: ${e.message}")
            }
        }


        override fun onResponse(call: Call, response: Response) {
            val responseData = response.body?.string()
            println("Respuesta del servidor: $responseData")

            if (response.isSuccessful && responseData != null) {
                (context as Activity).runOnUiThread {
                    respuesta("1")
                }
            } else {
                (context as Activity).runOnUiThread {
                    respuesta("Error al crear la cuenta")
                }
            }
        }
    })
}
//=======================================================================//
//                       Funciones para tarjetas                         //
//=======================================================================//
data class Tarjeta(
    val numeroTarjeta: String,
    val titular: String,
    val banco: String,
    val CVV: String,
    val fechaVencimiento: String,
    val fk_usuario: Int
)


fun obtenerTarjetas(context: Context, respuesta: (List<Tarjeta>) -> Unit) {
    val client = OkHttpClient()

    // Construir la URL con el parámetro GET
    val url = "${Endpoints.clienteTarjetas}?id=$IdDeUsuario"

    // Crear la solicitud GET
    val request = Request.Builder()
        .url(url)
        .get()
        .build()

    // Ejecutar la solicitud en un hilo secundario
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            (context as Activity).runOnUiThread {
                respuesta(emptyList()) // En caso de fallo, devolver lista vacía
            }
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseData = response.body?.string()
                if (responseData != null) {
                    try {
                        Log.d("HacerGetTarjetas", "Respuesta del servidor: $responseData")

                        // Parsear la respuesta JSON como un arreglo de objetos
                        val jsonArray = JSONArray(responseData)
                        val tarjetasList = mutableListOf<Tarjeta>()

                        for (i in 0 until jsonArray.length()) {
                            val jsonTarjeta = jsonArray.getJSONObject(i)
                            val tarjeta = Tarjeta(
                                numeroTarjeta = jsonTarjeta.getString("numeroTarjeta"),
                                titular = jsonTarjeta.getString("titular"),
                                banco = jsonTarjeta.getString("banco"),
                                CVV = jsonTarjeta.getString("CVV"),
                                fechaVencimiento = jsonTarjeta.getString("fechaVencimiento"),
                                fk_usuario = jsonTarjeta.getInt("fk_usuario")
                            )
                            tarjetasList.add(tarjeta)
                        }

                        // Devolver la lista de tarjetas en el hilo principal
                        (context as Activity).runOnUiThread {
                            respuesta(tarjetasList)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        (context as Activity).runOnUiThread {
                            respuesta(emptyList()) // Devolver lista vacía en caso de error
                        }
                    }
                }
            } else {
                Log.e("HacerGetTarjetas", "Error en la respuesta del servidor")
                (context as Activity).runOnUiThread {
                    respuesta(emptyList()) // Devolver lista vacía en caso de error
                }
            }
        }
    })
}

fun registrarTarjeta(
    context: Context,
    tarjeta: String,
    titular: String,
    banco: String,
    fechaVencimiento: String,
    cvv: String,
    respuesta: (String) -> Unit
) {
    if (tarjeta.isEmpty() || titular.isEmpty() || banco.isEmpty() || fechaVencimiento.isEmpty() || cvv.isEmpty()) {
        (context as Activity).runOnUiThread {
            respuesta("No deben existir campos vacíos")
        }
        return
    }

    val client = OkHttpClient()

    // Crear el JSON de los datos a enviar
    val jsonObject = JSONObject()
    jsonObject.put("tarjeta", tarjeta)
    jsonObject.put("titular", titular)
    jsonObject.put("banco", banco)
    jsonObject.put("vencimiento", fechaVencimiento)
    jsonObject.put("CVV", cvv)
    jsonObject.put("userID", IdDeUsuario)

    // Crear el cuerpo de la solicitud en formato JSON
    val requestBody = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    // Crear la solicitud POST
    val request = Request.Builder()
        .url(Endpoints.clienteTarjetas) // Cambia por tu URL local
        .post(requestBody)
        .addHeader("Content-Type", "application/json")
        .build()

    // Realizar la solicitud
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            (context as Activity).runOnUiThread {
                respuesta("Error de conexión: ${e.message}")
            }
        }

        override fun onResponse(call: Call, response: Response) {
            val responseData = response.body?.string()
            if (responseData != null) {
                println("Respuesta del servidor: $responseData") // Para depuración
                try {
                    val jsonResponse = JSONObject(responseData)
                    if (jsonResponse.has("status")) {
                        val status = jsonResponse.getInt("status")
                        val message = jsonResponse.getString("message")

                        (context as Activity).runOnUiThread {
                            if (status == 1) {
                                println(message)
                                respuesta("1")
                            } else {
                                println("Error al registrar la tarjeta: $message")
                                respuesta("2")
                            }
                        }
                    } else {
                        (context as Activity).runOnUiThread {
                            respuesta("Respuesta inesperada del servidor: clave 'status' no encontrada")
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    (context as Activity).runOnUiThread {
                        respuesta("Error al procesar la respuesta JSON")
                    }
                }
            } else {
                (context as Activity).runOnUiThread {
                    respuesta("Respuesta vacía del servidor")
                }
            }
        }
    })
}

data class PagoCallBack(
    val mensaje: String,
    val transaccion: String
)
fun enviarPago(
    context: Context,
    monto: Double,
    mesesPagados: Int,
    fkCuenta: Int,
    fkTarjeta: String,
    callback: (PagoCallBack) -> Unit
) {
    // Validar los campos
    if (monto <= 0 || mesesPagados <= 0 || fkCuenta <= 0 || fkTarjeta.isEmpty()) {
        (context as Activity).runOnUiThread {
            callback(PagoCallBack("Todos los campos deben ser válidos y no estar vacíos", ""))
        }
        return
    }

    val client = OkHttpClient()

    // Crear el JSON de los datos a enviar
    val jsonObject = JSONObject()
    jsonObject.put("monto", monto)
    jsonObject.put("meses_pagados", mesesPagados)
    jsonObject.put("fk_cuenta", fkCuenta)
    jsonObject.put("fk_tarjeta", fkTarjeta)

    // Crear el cuerpo de la solicitud en formato JSON
    val requestBody = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    // Crear la solicitud POST
    val request = Request.Builder()
        .url(Endpoints.pagos)
        .post(requestBody)
        .addHeader("Content-Type", "application/json")
        .build()

    // Realizar la solicitud
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            (context as Activity).runOnUiThread {
                callback(PagoCallBack("Error de conexión: ${e.message}", ""))
            }
        }

        override fun onResponse(call: Call, response: Response) {
            val responseData = response.body?.string()
            if (responseData != null) {
                println("Respuesta del servidor: $responseData") // Para depuración
                try {
                    val jsonResponse = JSONObject(responseData)
                    if (jsonResponse.has("status")) {
                        val status = jsonResponse.getInt("status")
                        val message = jsonResponse.getString("message")

                        (context as Activity).runOnUiThread {
                            if (status == 1) {
                                println(message)
                                //respuesta("Pago registrado exitosamente: ${jsonResponse.getString("transaccion")}")
                                callback(PagoCallBack("1",jsonResponse.getString("transaccion")))
                            } else {
                                println("Error al registrar el pago: $message")
                                callback(PagoCallBack("Error: $message", ""))
                            }
                        }
                    } else {
                        (context as Activity).runOnUiThread {
                            callback(PagoCallBack("Respuesta inesperada del servidor: clave 'status' no encontrada", ""))
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    (context as Activity).runOnUiThread {
                        callback(PagoCallBack("Error al procesar la respuesta JSON", ""))
                    }
                }
            } else {
                (context as Activity).runOnUiThread {
                    callback(PagoCallBack("Respuesta vacía del servidor", ""))
                }
            }
        }
    })
}






