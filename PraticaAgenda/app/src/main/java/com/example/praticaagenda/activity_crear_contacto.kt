package com.example.praticaagenda

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class activity_crear_contacto : AppCompatActivity() {

    companion object {
        private const val TAG = "CREAR_CONTACTO_APP"
        private const val BASE_URL = "http://agendaa.atwebpages.com/"

        // Clave API de ImgBB (obtenida de tu código de referencia)
        private const val IMGBB_API_KEY = "154897a630461f4a463c5b822d291c15"
        private const val IMGBB_URL = "https://api.imgbb.com/1/upload"
    }

    // Variables de UI
    private lateinit var toolbar: Toolbar
    private lateinit var ivFotoCrear: CircleImageView // Para mostrar y seleccionar la imagen
    private lateinit var etNombre: TextInputEditText
    private lateinit var etTelefono: TextInputEditText
    private lateinit var etCorreo: TextInputEditText
    private lateinit var etDireccion: TextInputEditText
    private lateinit var btnGuardar: Button

    // Variables de Datos y Red
    private lateinit var requestQueue: RequestQueue
    private var idUsuario: Int = -1
    private var imagenBase64: String? = null // Almacena el Base64 de la imagen seleccionada
    private var imagenUrlFinal: String = "" // Almacena la URL final de ImgBB o ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_contacto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Recoger el ID del usuario
        idUsuario = intent.getIntExtra("id_usuario", -1)
        if (idUsuario == -1) {
            Toast.makeText(this, "Error de sesión. ID de usuario no encontrado.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // 2. Inicializar Vistas y Toolbar
        toolbar = findViewById(R.id.toolbarCrearContacto)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Nuevo Contacto"

        ivFotoCrear = findViewById(R.id.ivFotoCrear)
        etNombre = findViewById(R.id.etNombreCrear)
        etTelefono = findViewById(R.id.etTelefonoCrear)
        etCorreo = findViewById(R.id.etCorreoCrear)
        etDireccion = findViewById(R.id.etDireccionCrear)
        btnGuardar = findViewById(R.id.btnGuardarContacto)

        requestQueue = Volley.newRequestQueue(this)

        // 3. Listener para seleccionar imagen
        ivFotoCrear.setOnClickListener {
            seleccionarImagenGaleria()
        }

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

    // ----------------------------------------------------------------------
    // --- LÓGICA DE IMAGEN: SELECCIÓN Y CÓDIGO ---
    // ----------------------------------------------------------------------

    private fun seleccionarImagenGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagenLauncher.launch(intent)
    }

    private val imagenLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val imagenUri: Uri? = result.data?.data
            if (imagenUri != null) {
                try {
                    // Cargar y mostrar la imagen seleccionada
                    ivFotoCrear.setImageURI(imagenUri)

                    // Convertir a Base64 para la subida
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagenUri)
                    imagenBase64 = encodeBitmapToBase64(bitmap)

                } catch (e: Exception) {
                    Log.e(TAG, "Error al procesar imagen: ${e.message}", e)
                    Toast.makeText(this, "Error al cargar la imagen.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Función para redimensionar y codificar un Bitmap a Base64 (similar a tu ejemplo).
     * @param original Bitmap a codificar.
     * @return Cadena Base64 de la imagen comprimida y redimensionada.
     */
    private fun encodeBitmapToBase64(original: Bitmap): String {
        // Redimensionar para reducir el tamaño del Base64
        val maxSize = 1080
        val width = original.width
        val height = original.height
        val ratio = minOf(maxSize.toFloat() / width, maxSize.toFloat() / height)

        val newWidth = (width * ratio).toInt()
        val newHeight = (height * ratio).toInt()

        val resized = Bitmap.createScaledBitmap(original, newWidth, newHeight, true)

        val outputStream = ByteArrayOutputStream()
        // Comprimir a JPEG con calidad 90
        resized.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }


    // ----------------------------------------------------------------------
    // --- LÓGICA DE GUARDADO Y SUBIDA A IMGBB ---
    // ----------------------------------------------------------------------

    private fun guardarContacto() {
        val nombre = etNombre.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()

        if (nombre.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Nombre y Teléfono son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        btnGuardar.isEnabled = false // Deshabilitar botón para evitar doble envío

        if (imagenBase64 != null) {
            // 1. Subir imagen a ImgBB para obtener la URL
            subirImagenAImgBB()
        } else {
            // 2. Si no hay imagen, guardar contacto directamente con URL vacía
            registrarContactoEnBD(imagenUrlFinal)
        }
    }

    private fun subirImagenAImgBB() {
        Toast.makeText(this, "Subiendo imagen...", Toast.LENGTH_SHORT).show()
        val stringRequest = object : StringRequest(Request.Method.POST, IMGBB_URL,
            Response.Listener { response ->
                try {
                    val objJSON = JSONObject(response)
                    val success = objJSON.getBoolean("success")

                    if (success) {
                        val imagenUrl = objJSON.getJSONObject("data").getString("url")
                        Log.i(TAG, "Imagen subida exitosamente. URL: $imagenUrl")

                        // Una vez que tenemos la URL, registramos el contacto en la BD
                        registrarContactoEnBD(imagenUrl)
                    } else {
                        // ImgBB devuelve errores bajo 'error' si el 'success' es false
                        val message = objJSON.getString("error")
                        Toast.makeText(this, "Error ImgBB: $message", Toast.LENGTH_LONG).show()
                        btnGuardar.isEnabled = true
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parseando respuesta de ImgBB:", e)
                    Toast.makeText(this, "Error al procesar la subida de imagen.", Toast.LENGTH_LONG).show()
                    btnGuardar.isEnabled = true
                }
            },
            Response.ErrorListener { error ->
                Log.e(TAG, "Error Volley ImgBB:", error)
                Toast.makeText(this, "Error de red al subir imagen.", Toast.LENGTH_LONG).show()
                btnGuardar.isEnabled = true
            }
        ) {
            // ImgBB espera los parámetros en formato form-urlencoded
            override fun getParams(): MutableMap<String, String> {
                val parametros = HashMap<String, String>()
                parametros["image"] = imagenBase64 ?: ""
                parametros["key"] = IMGBB_API_KEY
                return parametros
            }
        }
        requestQueue.add(stringRequest)
    }

    private fun registrarContactoEnBD(imagenUrl: String) {
        val nombre = etNombre.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val direccion = etDireccion.text.toString().trim()

        val jsonBody = JSONObject()
        // Claves deben coincidir con crear_contacto.php
        jsonBody.put("id_usuario", idUsuario)
        jsonBody.put("nombre", nombre)
        jsonBody.put("direccion", direccion)
        jsonBody.put("telefono", telefono)
        jsonBody.put("correo", correo)
        jsonBody.put("imagen_url", imagenUrl) // Usamos la URL obtenida (o vacía)

        val urlCrear = "${BASE_URL}crear_contacto.php"

        val stringRequest = object : StringRequest(
            Method.POST, urlCrear,

            Response.Listener { respuesta ->
                try {
                    val objJSON = JSONObject(respuesta)
                    if (objJSON.getBoolean("success")) {
                        Toast.makeText(this, "Contacto creado con éxito", Toast.LENGTH_LONG).show()
                        setResult(RESULT_OK) // Notificar a ContactosActivity
                        finish()
                    } else {
                        Toast.makeText(this, "Error BD: " + objJSON.getString("message"), Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error al parsear respuesta final:", e)
                    Toast.makeText(this, "Error de datos del servidor", Toast.LENGTH_LONG).show()
                }
                btnGuardar.isEnabled = true // Habilitar al finalizar
            },
            Response.ErrorListener { error ->
                Log.e(TAG, "Error Volley BD:", error)
                Toast.makeText(this, "Error de red al guardar contacto.", Toast.LENGTH_LONG).show()
                btnGuardar.isEnabled = true // Habilitar al finalizar
            }
        ) {
            // Para tu propio servidor, el cuerpo sigue siendo JSON
            override fun getBodyContentType(): String { return "application/json; charset=utf-8" }
            override fun getBody(): ByteArray { return jsonBody.toString().toByteArray(charset("utf-8")) }
        }
        requestQueue.add(stringRequest)
    }
}