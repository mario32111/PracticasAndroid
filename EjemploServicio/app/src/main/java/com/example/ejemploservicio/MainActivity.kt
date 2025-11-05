package com.example.ejemploservicio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var txt1: TextView
    lateinit var btn1: Button
    lateinit var btn2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txt1= findViewById(R.id.textView1)
        btn1= findViewById(R.id.button1)
        btn2= findViewById(R.id.button2)

        btn1.setOnClickListener {
            iniciarContador()
        }

        btn2.setOnClickListener {
            detenerContador()
        }

        //se debe hacer una instancia del servicio
        MyService.setUpdateListener(this@MainActivity)
    }

    fun iniciarContador(){
        //se sua el intent por que se va a llmar un recuros fuera del main activity
        val servicio = Intent(this@MainActivity, MyService::class.java)

        startService(servicio)
    }

    fun detenerContador(){
        //se sua el intent por que se va a llmar un recuros fuera del main activity
        val servicio = Intent(this@MainActivity, MyService::class.java)

        stopService(servicio)
    }

    fun actualizarContador(parContador: Int){
        txt1.text = parContador.toString()
    }
}