package com.example.appandroid

import android.graphics.Typeface
import android.text.InputType
import android.widget.EditText
import android.widget.ImageButton

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