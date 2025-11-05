package com.example.ejemploalertdialog

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var editText1: EditText
    lateinit var button1: Button
    lateinit var button2: Button

    lateinit var datosAlert : Array<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editText1 = findViewById(R.id.editText1)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)

        button1.setOnClickListener {
            datosAlert = arrayOfNulls<String>(6)

            datosAlert[0] = "Ingenieria Informatica"
            datosAlert[1] = "Ingenieria en Inteligencia Artificial"
            datosAlert[2] = "Ingenieria Industrial"
            datosAlert[3] = "Ingenieria en Industrias Alimentarias"
            datosAlert[4] = "Ingenieria Mecatronica"
            datosAlert[5] = "Ingenieria en Administracion"

            var builder = AlertDialog.Builder(this@MainActivity)

            //el builder es ocmo una caja cerrada que tiene entrada y salida de datos y tiene que haber un intermediario que manipule la informacion
            //en este caso el objeto es dialog
            builder.setTitle("Selecciona tu carrera")
                .setItems(datosAlert){
                    dialog, pos ->
                    Toast.makeText(this@MainActivity,  datosAlert[pos].toString(), Toast.LENGTH_SHORT).show()

                }
            builder.create().show()

        }

        button2.setOnClickListener {

        }

    }
}