package com.example.ejemplolistview

import android.os.Bundle
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var btn1: Button
    lateinit var EditText: EditText
    lateinit var ListView: ListView

    lateinit var adaptador: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn1= findViewById(R.id.button1)
        EditText = findViewById(R.id.EditText1)
        ListView = findViewById(R.id.listView1)
        adaptador = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_list_item_1)

        ListView.adapter= adaptador

        btn1.setOnClickListener {
            adaptador.add(EditText.text.toString())
            //notifica a quien este vinculado a ese adaptador que el conjunto de datos ha cambiado
            adaptador.notifyDataSetChanged()
        }
    }
}