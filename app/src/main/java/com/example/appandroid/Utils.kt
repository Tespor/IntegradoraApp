package com.example.appandroid

import android.content.Context
import android.graphics.Typeface
import android.media.ImageWriter
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

//Funcion para ocultar o ver contraseñas
fun VerOcultarPass (btnVer: ImageButton, txtPass: EditText, passVisible: Boolean): Boolean{
    if (passVisible){
        btnVer.setImageResource(R.drawable.ojox)
        txtPass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        txtPass.typeface = Typeface.SANS_SERIF
        txtPass.setSelection(txtPass.text.length)
        return false

    }
    else {
        btnVer.setImageResource(R.drawable.ojo)
        txtPass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        txtPass.typeface = Typeface.SANS_SERIF
        txtPass.setSelection(txtPass.text.length)
        return true
    }
}

fun showPopup(context: Context, mensaje: String) {
    val inflater = LayoutInflater.from(context)
    val layout = inflater.inflate(R.layout.alert_popup, null)

    // Texto del Alert
    val toastText = layout.findViewById<TextView>(R.id.txtAlertLogin)
    toastText.text = mensaje

    val toast = Toast(context)
    toast.view = layout
    toast.show()
}

class Utils {
}