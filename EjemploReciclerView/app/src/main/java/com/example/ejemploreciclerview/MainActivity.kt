package com.example.ejemploreciclerview

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    lateinit var btn1: Button
    lateinit var btn2: Button
    lateinit var btn3: Button

    lateinit var recycler: RecyclerView

    lateinit var adaptador: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn1=findViewById(R.id.button1)
        btn2=findViewById(R.id.button2)
        btn3=findViewById(R.id.button3)

        recycler= findViewById(R.id.recicletView1)

        adaptador= ArticleAdapter()
        recycler.adapter= adaptador

        btn1.setOnClickListener {
            //al array list se le asigna lo del companion object de la clase article
            adaptador.articles.addAll(Article.data)
            adaptador.notifyDataSetChanged()
        }

        btn2.setOnClickListener{
            adaptador.articles.clear()
            adaptador.notifyDataSetChanged()
        }

        btn3.setOnClickListener {
            adaptador.articles.add(Article("Monito", "Mascota de An droid", R.mipmap.ic_launcher) )
            adaptador.notifyDataSetChanged()
        }

    }

}