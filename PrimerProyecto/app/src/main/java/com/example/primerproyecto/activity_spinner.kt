package com.example.primerproyecto

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_spinner : AppCompatActivity() {
    lateinit var img1: ImageView
    lateinit var spn1: Spinner
    lateinit var adaptador1: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spinner)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        img1 = findViewById(R.id.imageView1)
        spn1 = findViewById(R.id.spinner1)

        adaptador1 = ArrayAdapter<String>(this@activity_spinner, android.R.layout.simple_spinner_dropdown_item)
//        spn1.setAdapter(adaptador1)
        spn1.adapter = adaptador1
        adaptador1.add("foto 1")
        adaptador1.add("foto 2")
        adaptador1.add("foto 3")
        adaptador1.add("foto 4")

        adaptador1.notifyDataSetChanged()

        spn1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0){
                    img1.setImageResource(R.mipmap.patricio_ferrari)
                }
                if (position == 1){
                    img1.setImageResource(R.mipmap.patricio1)
                }
                if (position == 2){
                    img1.setImageResource(R.mipmap.patricio2)
                }
                if (position == 3){
                    img1.setImageResource(R.mipmap.patricio3)
                }
                Toast.makeText(this@activity_spinner, position.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

}