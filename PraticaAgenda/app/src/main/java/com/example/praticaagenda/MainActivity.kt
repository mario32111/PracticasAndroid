package com.example.praticaagenda

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import android.util.Log // <- ¡Añade esto!

class MainActivity : AppCompatActivity() {
    // Define el TAG como constante de clase
    companion object {
        private const val TAG = "LOGIN_AGENDA_APP"
    }

    // ... (Variables de lateinit sin cambios)
    lateinit var edtUsuario: TextInputEditText
    lateinit var edtPass: TextInputEditText
    lateinit var btnLogin: Button

    lateinit var requestQueue: RequestQueue
    // NOTA: StringRequest se define como una variable local dentro de loginUsuario()

    // CAMBIA ESTO a tu URL real
    private var url = "http://agendaa.atwebpages.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtUsuario = findViewById(R.id.etUsernameLogin)
        edtPass = findViewById(R.id.etPasswordLogin)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            loginUsuario()
        }

        val btnGoToRegister: Button = findViewById(R.id.btnGoToRegister)
        btnGoToRegister.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUsuario() {

        val usuario = edtUsuario.text.toString()
        val contrasena = edtPass.text.toString()

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Llena todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        requestQueue = Volley.newRequestQueue(this@MainActivity)

        // 1. Crear el cuerpo JSON para enviar al servidor
        val jsonBody = JSONObject()
        // NOTA IMPORTANTE: Las claves deben ser 'username' y 'password'
        // para coincidir con lo que espera el script PHP.
        jsonBody.put("username", usuario)
        jsonBody.put("password", contrasena)

        val stringRequest = object : StringRequest(
            Method.POST, "${url}login.php",

            Response.Listener { respuesta ->
                // 2. El manejo de la respuesta JSON es correcto y se mantiene
                val objJSON = JSONObject(respuesta)
                val success = objJSON.getBoolean("success")

                if (success) {
                    val idUsuario = objJSON.getInt("id_usuario")

                    Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()

                    // Abre la actividad de contactos
                    val intent = Intent(this, ContactosActivity::class.java)
                    intent.putExtra("id_usuario", idUsuario)
                    startActivity(intent)
                    finish() // Opcional: cierra la actividad de login
                } else {
                    val message = objJSON.getString("message")
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            },

            Response.ErrorListener { error ->

                // --- MODIFICACIÓN DEL BLOQUE DE ERROR ---

                // 1. Mostrar mensaje genérico al usuario (Toast)
                val errorMessage = error.message ?: "Error desconocido en la solicitud"
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()

                // 2. Imprimir detalles del error en el Logcat para depuración
                Log.e(TAG, "Error de Volley: $errorMessage")
                Log.e(TAG, "Clase de Error: ${error::class.java.simpleName}")

                // Mostrar el código de estado HTTP si está disponible
                error.networkResponse?.let {
                    Log.e(TAG, "Código de Estado HTTP: ${it.statusCode}")
                    Log.e(TAG, "Datos de Respuesta (si existen): ${String(it.data)}")
                }

                // Mostrar la traza de la excepción
                Log.e(TAG, "Traza de la Pila:", error)

            }

        ) {
            // 3. Sobreescribir getBodyContentType para indicar que enviamos JSON
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            // 4. Sobreescribir getBody para enviar el cuerpo JSON como un array de bytes
            override fun getBody(): ByteArray {
                return jsonBody.toString().toByteArray(charset("utf-8"))
            }

            // ELIMINAMOS el override de fun getParams(): MutableMap<String, String>
        }

        requestQueue.add(stringRequest)
    }
}