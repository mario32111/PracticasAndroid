package com.example.practica3cronometro

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
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
    lateinit var btnStart: Button
    lateinit var btnPause: Button
    lateinit var btnReset: Button
    lateinit var btnLap: Button
    lateinit var txtSecs: TextView
    lateinit var txtMins: TextView
    lateinit var txtHours: TextView
    lateinit var lista: ListaView
    lateinit var adaptador: ArrayAdapter<String>


    override val coroutineContext: CoroutineContext
        //con esto se le especifica de que proceso se va a colgar, comunmente se usa main e IO
        get()= Dispatchers.Main
    var contSecs = 0
    var contMins = 0
    var contHours = 0
    var stop = false




    private var proceso1: Job?= null

    //esta es la tarea a correr, lista para ejecutarla en segundo plano
    fun tarea1(){
        stop = false
        proceso1 =  launch (Dispatchers.Main){
            while (!stop){
                delay(1000)
                contSecs++
                if(contSecs < 10){
                    txtSecs.text = "0"+contSecs.toString()
                }else{
                    txtSecs.text = contSecs.toString()
                }
                if(contMins < 10){
                    txtMins.text = "0"+contMins.toString()
                }else{
                    txtMins.text = contMins.toString()
                }
                if(contHours < 10){
                    txtHours.text = "0"+contHours.toString()
                }else{
                    txtHours.text = contHours.toString()
                }
                if(contSecs == 60 && txtMins.text.toString().toInt() < 59){
                    contSecs = 0
                    contMins++
                    txtMins.text = contMins.toString()
                    txtSecs.text = contSecs.toString()
                }
                if(txtMins.text.toString().toInt() == 59 && contSecs.toString().toInt() == 59){
                    contSecs = 0
                    contMins = 0
                    contHours++
                    txtHours.text = contHours.toString()
                    txtMins.text = contMins.toString()
                    txtSecs.text = contSecs.toString()
                }

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

        btnStart = findViewById(R.id.btn1)
        btnPause = findViewById(R.id.btn2)
        btnReset = findViewById(R.id.btn3)
        btnLap = findViewById(R.id.btn4)
        txtSecs = findViewById(R.id.txtSecs)
        txtMins = findViewById(R.id.txtMins)
        txtHours = findViewById(R.id.txtHours)

        lista = findViewById(R.id.listView1)
        adaptador = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_list_item_1)

        lista.adapter= adaptador

        lista.setOnItemClickListener { parent, view, position, id ->
            adaptador.remove(adaptador.getItem(position))
            adaptador.notifyDataSetChanged()
        }

        btnStart.setOnClickListener {
            tarea1()
        }
        btnPause.setOnClickListener {
            if(proceso1?.isActive == true && !stop)
            {
                proceso1?.cancel()
                stop = true
            }else{
                stop = false
                tarea1()
            }

        }
        btnReset.setOnClickListener {
            stop = true
            proceso1?.cancel()
            contSecs = 0
            contMins = 0
            contHours = 0
            txtSecs.text = "00"
            txtMins.text = "00"
            txtHours.text = "00"
        }

        btnLap.setOnClickListener {
            adaptador.add(txtHours.text.toString()+":"+txtMins.text.toString()+":"+txtSecs.text.toString())
            adaptador.notifyDataSetChanged()
        }
    }
}