package com.example.ejemplocorutina

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    lateinit var txt1: TextView

    lateinit var btnStart: Button
    lateinit var btnEnd: Button

    var cont: Int =0
    override val coroutineContext: CoroutineContext
        //con esto se le especifica de que proceso se va a colgar, comunmente se usa main e IO
        get()= Dispatchers.Main

    private var proceso1: Job?= null

    //esta es la tarea a correr, lista para ejecutarla en segundo plano
    fun tarea1(){
        proceso1 =  launch (Dispatchers.Main){
            while (true){
                delay(300)
                txt1.text =  cont.toString()
                cont++
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txt1= findViewById(R.id.txt1)
        btnStart = findViewById(R.id.btnStart)
        btnEnd = findViewById(R.id.btnEnd)

        btnStart.setOnClickListener {
            proceso1?.cancel()
            cont = 0

            tarea1()
        }

        btnEnd.setOnClickListener {
            cont = 0
            proceso1?.cancel()
            txt1.text= "00"

        }
    }
}