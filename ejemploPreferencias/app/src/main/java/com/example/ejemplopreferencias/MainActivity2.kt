package com.example.ejemplopreferencias

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
    lateinit var txt1: TextView
    lateinit var txt2: TextView
    lateinit var btn1: Button

    var conector: SharedPreferences? =null
    var editor: Editor?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txt1= findViewById(R.id.textView1)
        txt2= findViewById(R.id.textView2)
        btn1= findViewById(R.id.button1)

        txt1.setText(intent.getStringExtra("matr").toString())
        txt2.setText(intent.getStringExtra("nom").toString())

        btn1.setOnClickListener {
            //se crea el objeto conector
            //con el modo privado, va en mayusculas por que es una constante del sistema
            conector = getSharedPreferences("datos", MODE_PRIVATE)
            //se crea el objeto editor y se le pasa el conector
            editor = conector?.edit()
            //se agregan los datos de los txt
            editor?.putString("matricula", txt1.text.toString())
            editor?.putString("nombre", txt2.text.toString())
            //hay que reflejar los cambios para que se guarden
            editor?.commit()
            finish()
        }

    }
}