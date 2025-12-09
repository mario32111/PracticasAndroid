package com.example.praticaagenda

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import android.util.Log

class RegistroActivity : AppCompatActivity() {

    // Define la etiqueta de logging y la URL base
    companion object {
        private const val TAG = "REGISTRO_AGENDA_APP"
        // Asegúrate de que esta URL sea correcta (usa HTTPS si es posible, o asegúrate de tener el Cleartext Traffic habilitado)
        private const val BASE_URL = "http://agendaa.atwebpages.com/"
    }

    // 1. Variables para los elementos de la interfaz (ahora con edtNombre)
    private lateinit var edtNombre: TextInputEditText // Nuevo campo
    private lateinit var edtUsername: TextInputEditText
    private lateinit var edtPassword: TextInputEditText
    private lateinit var btnRegistro: Button

    // Volley
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // 2. Inicialización de vistas (incluyendo el nuevo campo)
        edtNombre = findViewById(R.id.etNombreRegistro) // Se asocia con el ID del layout
        edtUsername = findViewById(R.id.etUsernameRegistro)
        edtPassword = findViewById(R.id.etPasswordRegistro)
        btnRegistro = findViewById(R.id.btnOkRegistro)

        // Inicializar Volley
        requestQueue = Volley.newRequestQueue(this)

        // 3. Configurar el listener del botón
        btnRegistro.setOnClickListener {
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        // Leer los 3 campos
        val nombre = edtNombre.text.toString().trim()
        val username = edtUsername.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if (nombre.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear el cuerpo JSON para enviar al servidor
        val jsonBody = JSONObject()
        // NOTA: Las claves deben coincidir con las esperadas por registro.php: 'nombre', 'username', 'password'
        jsonBody.put("nombre", nombre)
        jsonBody.put("username", username)
        jsonBody.put("password", password)

        val urlRegistro = "${BASE_URL}registro.php"

        val stringRequest = object : StringRequest(
            Method.POST, urlRegistro,

            Response.Listener { respuesta ->
                Log.i(TAG, "Respuesta del registro: $respuesta")

                try {
                    val objJSON = JSONObject(respuesta)
                    val success = objJSON.getBoolean("success")

                    if (success) {
                        Toast.makeText(this, "Registro exitoso. ¡Inicia sesión!", Toast.LENGTH_LONG).show()
                        // Finaliza la actividad de registro y regresa a MainActivity (login)
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
                val errorMessage = error.message ?: "Error desconocido de conexión"
                Toast.makeText(this, "Error de red: $errorMessage", Toast.LENGTH_LONG).show()

                // Logging detallado del error de Volley
                Log.e(TAG, "--------------------------------------------------")
                Log.e(TAG, "Error de Volley en Registro: $errorMessage")
                error.networkResponse?.let {
                    Log.e(TAG, "Código de Estado HTTP: ${it.statusCode}")
                    Log.e(TAG, "Cuerpo del Error: ${String(it.data, charset("utf-8"))}")
                }
                Log.e(TAG, "Traza de la Pila Completa:", error)
                Log.e(TAG, "--------------------------------------------------")
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