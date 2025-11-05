package com.example.ejemplopreferencias

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity3 : AppCompatActivity() {
    lateinit var txt1: TextView
    lateinit var txt2: TextView

    var conector: SharedPreferences?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        txt1= findViewById(R.id.txt1)
        txt2= findViewById(R.id.txt2)

        conector= getSharedPreferences("datos", MODE_PRIVATE)
        txt1.setText(conector?.getString("matricula", "No hay datos"))
        txt2.setText(conector?.getString("nombre", "No hay datos"))

    }
}