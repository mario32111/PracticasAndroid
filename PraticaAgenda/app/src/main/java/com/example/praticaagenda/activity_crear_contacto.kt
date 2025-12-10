package com.example.praticaagenda

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import androidx.appcompat.widget.Toolbar // <-- AÑADE ESTA LÍNEA (si no se corrige automáticamente)
class activity_crear_contacto : AppCompatActivity() {
    companion object {
        private const val TAG = "CREAR_CONTACTO_APP"
        // Asegúrate de que esta URL sea correcta (usando HTTPS si es posible)
        private const val BASE_URL = "http://agendaa.atwebpages.com/"
    }

    // Variables de UI
    private lateinit var toolbar: Toolbar
    private lateinit var etNombre: TextInputEditText
    private lateinit var etTelefono: TextInputEditText
    private lateinit var etCorreo: TextInputEditText
    private lateinit var etDireccion: TextInputEditText
    private lateinit var btnGuardar: Button

    // Variables de Datos y Red
    private lateinit var requestQueue: RequestQueue
    private var idUsuario: Int = -1 // ID del usuario logueado, recogido del Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_contacto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

// 1. Recoger el ID del usuario del Intent
        idUsuario = intent.getIntExtra("id_usuario", -1)
        if (idUsuario == -1) {
            Toast.makeText(this, "Error de sesión. ID de usuario no encontrado.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // 2. Inicializar Toolbar y Habilitar Botón de Regreso
        toolbar = findViewById(R.id.toolbarCrearContacto)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Nuevo Contacto"

        // 3. Inicializar Vistas del Formulario
        etNombre = findViewById(R.id.etNombreCrear)
        etTelefono = findViewById(R.id.etTelefonoCrear)
        etCorreo = findViewById(R.id.etCorreoCrear)
        etDireccion = findViewById(R.id.etDireccionCrear)
        btnGuardar = findViewById(R.id.btnGuardarContacto)

        requestQueue = Volley.newRequestQueue(this)

        // 4. Configurar el Listener del Botón Guardar
        btnGuardar.setOnClickListener {
            guardarContacto()
        }
    }

    // Manejar el botón de regreso de la Toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun guardarContacto() {
        val nombre = etNombre.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val direccion = etDireccion.text.toString().trim()

        // La URL de la imagen se deja vacía o con un valor por defecto
        val imagenUrl = ""

        // Validación básica
        if (nombre.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Nombre y Teléfono son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // 1. Crear el cuerpo JSON para enviar al servidor
        val jsonBody = JSONObject()
        // Las claves deben coincidir con crear_contacto.php
        jsonBody.put("id_usuario", idUsuario)
        jsonBody.put("nombre", nombre)
        jsonBody.put("direccion", direccion)
        jsonBody.put("telefono", telefono)
        jsonBody.put("correo", correo)
        jsonBody.put("imagen_url", imagenUrl)

        val urlCrear = "${BASE_URL}crear_contacto.php"

        val stringRequest = object : StringRequest(
            Method.POST, urlCrear,

            Response.Listener { respuesta ->
                Log.i(TAG, "Respuesta de Creación: $respuesta")

                try {
                    val objJSON = JSONObject(respuesta)
                    val success = objJSON.getBoolean("success")

                    if (success) {
                        Toast.makeText(this, "Contacto creado con éxito", Toast.LENGTH_LONG).show()

                        // 2. Notificar a la actividad anterior para que recargue la lista
                        setResult(RESULT_OK)
                        finish()

                    } else {
                        val message = objJSON.getString("message")
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error al parsear JSON: ${e.message}", e)
                    Toast.makeText(this, "Error de datos del servidor", Toast.LENGTH_LONG).show()
                }
            },

            Response.ErrorListener { error ->
                Toast.makeText(this, "Error de red al crear contacto", Toast.LENGTH_LONG).show()
                Log.e(TAG, "Error de Volley en Creación:", error)
            }

        ) {
            // Sobreescribir métodos para enviar JSON
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return jsonBody.toString().toByteArray(charset("utf-8"))
            }
        }
        requestQueue.add(stringRequest)
    }
}