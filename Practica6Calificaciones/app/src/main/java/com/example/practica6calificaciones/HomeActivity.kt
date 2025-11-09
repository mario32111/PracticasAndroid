package com.example.practica6calificaciones

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {

    private lateinit var txtUsuario: TextView
    private lateinit var imgUsuario: ImageView
    private lateinit var btnSalir: Button
    private lateinit var btnAlumnos: Button
    private lateinit var btnMaterias: Button
    private lateinit var btnCalificaciones: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        txtUsuario = findViewById(R.id.textViewUsuario)
        imgUsuario = findViewById(R.id.imageViewUsuario)
        btnSalir = findViewById(R.id.buttonSalir)
        btnAlumnos = findViewById(R.id.buttonAlumnos)
        btnMaterias = findViewById(R.id.buttonMaterias)
        btnCalificaciones = findViewById(R.id.buttonCalificar)

        val usuario = intent.getStringExtra("usuario") ?: "Usuario"

        // Mostrar información del usuario
        txtUsuario.text = "Nombre de usuario: $usuario"

        btnSalir.setOnClickListener {
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnAlumnos.setOnClickListener {
            val intent = Intent(this@HomeActivity, AlumnosActivity::class.java)
            startActivity(intent)
        }

        btnMaterias.setOnClickListener {
            val intent = Intent(this@HomeActivity, ActivityMaterias::class.java)
            startActivity(intent)
        }

        btnCalificaciones.setOnClickListener {
            val intent = Intent(this@HomeActivity, CalificacionesActivity::class.java)
            startActivity(intent)
        }
    }
}