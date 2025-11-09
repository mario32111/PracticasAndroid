package com.example.practica6calificaciones

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var edtUsuario: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegistrar: Button
    private var conector: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edtUsuario = findViewById(R.id.editTextUsuario)
        edtPassword = findViewById(R.id.editTextPassword)
        btnLogin = findViewById(R.id.buttonLogin)
        btnRegistrar = findViewById(R.id.buttonRegistrar)

        conector = getSharedPreferences("usuarios", MODE_PRIVATE)

        btnLogin.setOnClickListener {
            val usuario = edtUsuario.text.toString()
            val password = edtPassword.text.toString()

            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verificar si el usuario existe
            val usuarioGuardado = conector?.getString("usuario_$usuario", null)
            val passwordGuardada = conector?.getString("password_$usuario", null)

            if (usuarioGuardado != null && passwordGuardada == password) {
                // Login exitoso
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                intent.putExtra("usuario", usuario)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegistrar.setOnClickListener {
            val intent = Intent(this@MainActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}