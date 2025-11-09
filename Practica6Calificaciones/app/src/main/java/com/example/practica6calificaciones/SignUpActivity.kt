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

class SignUpActivity : AppCompatActivity() {
    private lateinit var edtNuevoUsuario: EditText
    private lateinit var edtNuevaPassword: EditText
    private lateinit var edtConfirmarPassword: EditText
    private lateinit var btnGuardar: Button
    private var conector: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edtNuevoUsuario = findViewById(R.id.editTextNuevoUsuario)
        edtNuevaPassword = findViewById(R.id.editTextNuevaPassword)
        edtConfirmarPassword = findViewById(R.id.editTextConfirmarPassword)
        btnGuardar = findViewById(R.id.buttonGuardar)

        conector = getSharedPreferences("usuarios", MODE_PRIVATE)
        editor = conector?.edit()

        btnGuardar.setOnClickListener {
            val usuario = edtNuevoUsuario.text.toString()
            val password = edtNuevaPassword.text.toString()
            val confirmarPassword = edtConfirmarPassword.text.toString()

            if (usuario.isEmpty() || password.isEmpty() || confirmarPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmarPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verificar si el usuario ya existe
            val usuarioExistente = conector?.getString("usuario_$usuario", null)
            if (usuarioExistente != null) {
                Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Guardar el nuevo usuario
            editor?.putString("usuario_$usuario", usuario)
            editor?.putString("password_$usuario", password)

            // Asignar imagen según el usuario (puedes personalizar esta lógica)
            val imagenId = when (usuario.hashCode() % 3) {
                0 -> R.drawable.user1 // Debes crear estos drawables
                1 -> R.drawable.user2
                else -> R.drawable.user3
            }
            editor?.putInt("imagen_$usuario", imagenId)

            editor?.apply()

            Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()

            // Regresar al login
            val intent = Intent(this@SignUpActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}