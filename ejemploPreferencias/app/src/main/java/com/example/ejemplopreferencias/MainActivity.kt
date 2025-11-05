package com.example.ejemplopreferencias

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var edt1: EditText
    lateinit var edt2: EditText
    lateinit var btn1: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edt1= findViewById(R.id.editText1)
        edt2= findViewById(R.id.editText2)
        btn1= findViewById(R.id.button1)

        btn1.setOnClickListener {
            var intento = Intent(this@MainActivity, MainActivity2::class.java)
            intento.putExtra("matr", edt1.text.toString())
            intento.putExtra("nom", edt2.text.toString())

            startActivity(intento)
        }
    }
}