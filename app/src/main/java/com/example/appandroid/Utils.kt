package com.example.appandroid

import android.graphics.Typeface
import android.media.ImageWriter
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

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

class Utils {
}