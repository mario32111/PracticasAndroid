package com.example.primerproyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Menu : AppCompatActivity() {
    lateinit var button1: Button
    lateinit var button2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)

        button1.setOnClickListener {
            //el intent es una clase que se utiliza para mandar llamar cosas fuera del activity,
            // sirve para comunicar cosas entre la app
            // se le manda la vista desde la cual se va a mandar lllamar el  y el activity que se va a mandar llamar
            val llamado1 = Intent(this@Menu, MainActivity::class.java)
            startActivity(llamado1)
        }

        button2.setOnClickListener {
            val llamado2 = Intent(this@Menu, MainActivity2::class.java)
            startActivity(llamado2)
        }
    }
}