package com.example.praticaagenda

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class activity_editar_contacto : AppCompatActivity() {

    companion object {
        private const val TAG = "EDITAR_CONTACTO_APP"
        private const val BASE_URL = "http://agendaa.atwebpages.com/"
    }

    // Variables de UI (Se usan los mismos IDs que en CrearContacto)
    private lateinit var toolbar: Toolbar
    private lateinit var etNombre: TextInputEditText
    private lateinit var etTelefono: TextInputEditText
    private lateinit var etCorreo: TextInputEditText
    private lateinit var etDireccion: TextInputEditText
    private lateinit var btnGuardar: Button

    // Variables de Datos y Red
    private lateinit var requestQueue: RequestQueue
    private var idUsuario: Int = -1
    private var idContacto: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_contacto) // Reutilizamos el layout del formulario

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true

        // 1. Recuperar IDs del Intent
        idUsuario = intent.getIntExtra("id_usuario", -1)
        idContacto = intent.getIntExtra("id_contacto", -1)

        if (idUsuario == -1 || idContacto == -1) {
            Toast.makeText(this, "Error: IDs faltantes para editar.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // 2. Inicializar Vistas
        // Usamos R.id.toolbarCrearContacto (si usaste este ID en el XML)
        toolbar = findViewById(R.id.toolbarCrearContacto)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Editar Contacto"

        etNombre = findViewById(R.id.etNombreCrear)
        etTelefono = findViewById(R.id.etTelefonoCrear)
        etCorreo = findViewById(R.id.etCorreoCrear)
        etDireccion = findViewById(R.id.etDireccionCrear)
        btnGuardar = findViewById(R.id.btnGuardarContacto)
        btnGuardar.text = "Actualizar Contacto" // Cambiar texto del botón

        requestQueue = Volley.newRequestQueue(this)

        // 3. Cargar datos existentes del contacto
        obtenerDetalleContacto(idUsuario, idContacto)

        // 4. Configurar Listener para guardar los cambios
        btnGuardar.setOnClickListener {
            actualizarContacto()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // --- LÓGICA DE CARGA: Usa obtener_contactos.php para llenar el formulario ---
    private fun obtenerDetalleContacto(idUsuario: Int, idContacto: Int) {
        val jsonBody = JSONObject()
        jsonBody.put("id_usuario", idUsuario)
        jsonBody.put("id_contacto", idContacto)
        val urlDetalle = "${BASE_URL}obtener_contactos.php"

        val stringRequest = object : StringRequest(
            Method.POST, urlDetalle,
            Response.Listener { respuesta ->
                try {
                    val objJSON = JSONObject(respuesta)
                    if (objJSON.getBoolean("success")) {
                        val data = objJSON.getJSONObject("data")
                        llenarFormulario(data)
                    } else {
                        Toast.makeText(this, "Error al cargar datos: " + objJSON.getString("message"), Toast.LENGTH_LONG).show()
                        finish()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error al parsear datos de carga:", e)
                    Toast.makeText(this, "Error al procesar datos del servidor.", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e(TAG, "Error de red en carga:", error)
                Toast.makeText(this, "Error de red al cargar el contacto.", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getBodyContentType(): String { return "application/json; charset=utf-8" }
            override fun getBody(): ByteArray { return jsonBody.toString().toByteArray(charset("utf-8")) }
        }
        requestQueue.add(stringRequest)
    }

    private fun llenarFormulario(data: JSONObject) {
        etNombre.setText(data.getString("nombre"))
        etTelefono.setText(data.getString("telefono"))
        etCorreo.setText(data.getString("correo"))
        etDireccion.setText(data.getString("direccion"))
        // Si tuviera la URL de la imagen, la podrías cargar aquí
    }

    // --- LÓGICA DE ACTUALIZACIÓN: Usa actualizar_contacto.php para guardar cambios ---
    private fun actualizarContacto() {
        val nombre = etNombre.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val direccion = etDireccion.text.toString().trim()
        val imagenUrl = "" // Se debe obtener la URL actual o la nueva URL si la imagen fue cambiada

        if (nombre.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Nombre y Teléfono son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val jsonBody = JSONObject()
        // Claves requeridas por actualizar_contacto.php
        jsonBody.put("id_usuario", idUsuario)
        jsonBody.put("id_contacto", idContacto)
        jsonBody.put("nombre", nombre)
        jsonBody.put("direccion", direccion)
        jsonBody.put("telefono", telefono)
        jsonBody.put("correo", correo)
        jsonBody.put("imagen_url", imagenUrl)

        val urlActualizar = "${BASE_URL}actualizar_contacto.php"

        val stringRequest = object : StringRequest(
            Method.POST, urlActualizar,

            Response.Listener { respuesta ->
                Log.i(TAG, "Respuesta de Actualización: $respuesta")
                try {
                    val objJSON = JSONObject(respuesta)
                    if (objJSON.getBoolean("success")) {
                        Toast.makeText(this, "Contacto actualizado con éxito", Toast.LENGTH_LONG).show()

                        // Notificar a DetalleActivity (y ContactosActivity) para que recarguen
                        setResult(RESULT_OK)
                        finish()

                    } else {
                        Toast.makeText(this, "Error al actualizar: " + objJSON.getString("message"), Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error al parsear JSON después de actualizar:", e)
                }
            },

            Response.ErrorListener { error ->
                Log.e(TAG, "Error de Volley en Actualización:", error)
                Toast.makeText(this, "Error de red al actualizar", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getBodyContentType(): String { return "application/json; charset=utf-8" }
            override fun getBody(): ByteArray { return jsonBody.toString().toByteArray(charset("utf-8")) }
        }
        requestQueue.add(stringRequest)
    }
}