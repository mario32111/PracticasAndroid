package com.example.primerproyecto

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
    lateinit var img1: ImageView
    lateinit var img2: ImageView
    lateinit var img3: ImageView
    lateinit var img4: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        img1 = findViewById(R.id.imageView1)
        img2 = findViewById(R.id.imageView2)
        img3 = findViewById(R.id.imageView3)
        img4 = findViewById(R.id.imageView4)

        img1.setOnClickListener {
            img2.setImageResource(R.mipmap.ic_launcher)
            img3.setImageResource(R.mipmap.ic_launcher)
            img4.setImageResource(R.mipmap.ic_launcher)
        }

    }
}