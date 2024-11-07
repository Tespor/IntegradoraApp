package com.example.appandroid

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class Endpoint {
    private val url_base = "http://192.168.30.151/Integradora/";

    val Login_URL: String
        get() = "${url_base}Login.php"
    val Registrar_URL: String
        get() = "${url_base}Registrar.php"
}

//funcion para enviar datos al login del php WEB
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
        .url(Endpoint().Login_URL)
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

                        // Verifica el rol de usuario
                        when (role) {
                            "admin" -> {
                                "Tipo de usuario no valido"
                            }
                            "client" -> {
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