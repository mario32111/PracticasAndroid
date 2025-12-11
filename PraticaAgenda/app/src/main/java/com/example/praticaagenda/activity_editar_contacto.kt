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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
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
import com.squareup.picasso.Picasso

class activity_editar_contacto : AppCompatActivity() {

    companion object {
        private const val TAG = "EDITAR_CONTACTO_APP"
        private const val BASE_URL = "http://agendaa.atwebpages.com/"

        // Clave API de ImgBB
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
    private var idContacto: Int = -1
    private var imagenBase64: String? = null // Base64 de la NUEVA imagen seleccionada
    private var imagenUrlActual: String = "" // URL de la imagen existente del contacto
    private var isImageChanged: Boolean = false // Bandera para saber si se seleccionó una nueva foto


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
        toolbar = findViewById(R.id.toolbarCrearContacto)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Editar Contacto"

        ivFotoCrear = findViewById(R.id.ivFotoCrear) // Inicializar ImageView
        etNombre = findViewById(R.id.etNombreCrear)
        etTelefono = findViewById(R.id.etTelefonoCrear)
        etCorreo = findViewById(R.id.etCorreoCrear)
        etDireccion = findViewById(R.id.etDireccionCrear)
        btnGuardar = findViewById(R.id.btnGuardarContacto)
        btnGuardar.text = "Actualizar Contacto" // Cambiar texto del botón

        requestQueue = Volley.newRequestQueue(this)

        // Listener para seleccionar imagen
        ivFotoCrear.setOnClickListener {
            seleccionarImagenGaleria()
        }

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
                    // Cargar y mostrar la NUEVA imagen seleccionada
                    ivFotoCrear.setImageURI(imagenUri)
                    isImageChanged = true // Marcamos que la imagen cambió

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

    private fun encodeBitmapToBase64(original: Bitmap): String {
        val maxSize = 1080
        val width = original.width
        val height = original.height
        val ratio = minOf(maxSize.toFloat() / width, maxSize.toFloat() / height)

        val newWidth = (width * ratio).toInt()
        val newHeight = (height * ratio).toInt()

        val resized = Bitmap.createScaledBitmap(original, newWidth, newHeight, true)

        val outputStream = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
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
        // Almacenar URL actual
        imagenUrlActual = data.getString("imagen_url")

        etNombre.setText(data.getString("nombre"))
        etTelefono.setText(data.getString("telefono"))
        etCorreo.setText(data.getString("correo"))
        etDireccion.setText(data.getString("direccion"))

        // Cargar imagen existente (requiere Picasso/Glide)
        if (imagenUrlActual.isNotEmpty()) {
            Picasso.get().load(imagenUrlActual).placeholder(R.drawable.ic_default_user).into(ivFotoCrear)
        }
    }

    // --- LÓGICA DE ACTUALIZACIÓN: Flujo de Subida y Guardado ---

    private fun actualizarContacto() {
        val nombre = etNombre.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val direccion = etDireccion.text.toString().trim()

        if (nombre.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Nombre y Teléfono son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        btnGuardar.isEnabled = false // Deshabilitar botón durante el proceso

        if (isImageChanged && imagenBase64 != null) {
            // 1. Si la imagen cambió, subirla primero a ImgBB
            subirImagenAImgBB(nombre, telefono, correo, direccion)
        } else {
            // 2. Si NO cambió, usar la URL antigua (imagenUrlActual)
            ejecutarUpdateEnBD(nombre, telefono, correo, direccion, imagenUrlActual)
        }
    }

    private fun subirImagenAImgBB(nombre: String, telefono: String, correo: String, direccion: String) {
        Toast.makeText(this, "Subiendo nueva imagen...", Toast.LENGTH_SHORT).show()
        val stringRequest = object : StringRequest(Request.Method.POST, IMGBB_URL,
            Response.Listener { response ->
                try {
                    val objJSON = JSONObject(response)
                    if (objJSON.getBoolean("success")) {
                        val nuevaUrl = objJSON.getJSONObject("data").getString("url")
                        Log.i(TAG, "Imagen subida. URL: $nuevaUrl")

                        // Una vez con la nueva URL, actualizamos la BD
                        ejecutarUpdateEnBD(nombre, telefono, correo, direccion, nuevaUrl)
                    } else {
                        Toast.makeText(this, "Error ImgBB: " + objJSON.getString("error"), Toast.LENGTH_LONG).show()
                        btnGuardar.isEnabled = true
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parseando respuesta de ImgBB:", e)
                    Toast.makeText(this, "Error al procesar la subida.", Toast.LENGTH_LONG).show()
                    btnGuardar.isEnabled = true
                }
            },
            Response.ErrorListener { error ->
                Log.e(TAG, "Error Volley ImgBB:", error)
                Toast.makeText(this, "Error de red al subir imagen.", Toast.LENGTH_LONG).show()
                btnGuardar.isEnabled = true
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val parametros = HashMap<String, String>()
                parametros["image"] = imagenBase64 ?: ""
                parametros["key"] = IMGBB_API_KEY
                return parametros
            }
        }
        requestQueue.add(stringRequest)
    }

    private fun ejecutarUpdateEnBD(nombre: String, telefono: String, correo: String, direccion: String, url: String) {
        val jsonBody = JSONObject()
        // Claves requeridas por actualizar_contacto.php
        jsonBody.put("id_usuario", idUsuario)
        jsonBody.put("id_contacto", idContacto)
        jsonBody.put("nombre", nombre)
        jsonBody.put("direccion", direccion)
        jsonBody.put("telefono", telefono)
        jsonBody.put("correo", correo)
        jsonBody.put("imagen_url", url)

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
                btnGuardar.isEnabled = true
            },

            Response.ErrorListener { error ->
                Log.e(TAG, "Error de Volley en Actualización:", error)
                Toast.makeText(this, "Error de red al actualizar", Toast.LENGTH_LONG).show()
                btnGuardar.isEnabled = true
            }
        ) {
            override fun getBodyContentType(): String { return "application/json; charset=utf-8" }
            override fun getBody(): ByteArray { return jsonBody.toString().toByteArray(charset("utf-8")) }
        }
        requestQueue.add(stringRequest)
    }
}