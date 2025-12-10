package com.example.praticaagenda

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject
import androidx.core.view.WindowCompat // Necesitas esta importación
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult

class DetalleContactoActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DETALLE_AGENDA_APP"
        private const val BASE_URL = "http://agendaa.atwebpages.com/"
    }

    // Variables de UI (ID's del layout de detalle)
    private lateinit var toolbar: Toolbar
    private lateinit var ivFotoDetalle: CircleImageView
    private lateinit var tvNombreDetalle: TextView
    private lateinit var tvTelefonoDetalle: TextView
    private lateinit var tvCorreoDetalle: TextView
    private lateinit var tvDireccionDetalle: TextView
    private lateinit var fabGoToEdit: FloatingActionButton

    // Variables de Datos y Red
    private lateinit var requestQueue: RequestQueue
    private var idUsuario: Int = -1
    private var idContacto: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_contacto) // Asumiendo que el layout es activity_detalle.xml
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true

        // 1. Recuperar IDs del Intent (enviados desde ContactosActivity)
        idUsuario = intent.getIntExtra("id_usuario", -1)
        idContacto = intent.getIntExtra("id_contacto", -1)

        // 2. Inicializar Vistas
        toolbar = findViewById(R.id.toolbarDetalle)
        ivFotoDetalle = findViewById(R.id.ivFotoDetalle)
        tvNombreDetalle = findViewById(R.id.tvNombreDetalle)
        tvTelefonoDetalle = findViewById(R.id.tvTelefonoDetalle)
        tvCorreoDetalle = findViewById(R.id.tvCorreoDetalle)
        tvDireccionDetalle = findViewById(R.id.tvDireccionDetalle)
        fabGoToEdit = findViewById(R.id.fabGoToEdit)

        // Configurar Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        requestQueue = Volley.newRequestQueue(this)

        if (idUsuario != -1 && idContacto != -1) {
            // 3. Iniciar la carga de datos con los dos IDs
            obtenerDetalleContacto(idUsuario, idContacto)
        } else {
            Toast.makeText(this, "Error: Faltan IDs de usuario o contacto.", Toast.LENGTH_LONG).show()
            finish()
        }

        // Listener del FAB (Pendiente de implementación de Edición)
        fabGoToEdit.setOnClickListener {
            Toast.makeText(this, "Editando contacto ID: $idContacto", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalle_contacto, menu) // Usar el nuevo menú
        return true
    }

    // 2. Manejar las acciones de los botones del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Acción para el botón de regreso (flecha) en el Toolbar
                onBackPressedDispatcher.onBackPressed()
                true
            }
            R.id.action_editar -> {
                // Programar el botón EDITAR
                abrirActividadEdicion()
                true
            }
            R.id.action_eliminar -> {
                // Programar el botón ELIMINAR (requiere confirmación)
                mostrarDialogoConfirmacionEliminar()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // --- Lógica del Botón EDITAR ---
    private val editarContactoLauncher =
        registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // 1. Recargar el detalle del contacto (OK)
                obtenerDetalleContacto(idUsuario, idContacto)
                Toast.makeText(this, "Detalles actualizados.", Toast.LENGTH_SHORT).show()

                // 2. IMPORTANTE: Notificar a ContactosActivity que hubo un cambio
                setResult(RESULT_OK)
            }
        }

    // Reemplaza la función abrirActividadEdicion:
    private fun abrirActividadEdicion() {
        val intent = Intent(this, activity_editar_contacto::class.java)
        intent.putExtra("id_usuario", idUsuario)
        intent.putExtra("id_contacto", idContacto)

        // USAR EL LAUNCHER para esperar el resultado
        editarContactoLauncher.launch(intent)
    }

    // --- Lógica del Botón ELIMINAR ---
    private fun mostrarDialogoConfirmacionEliminar() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este contacto?")
            .setPositiveButton("Eliminar") { dialog, which ->
                ejecutarEliminacion(idUsuario, idContacto)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun ejecutarEliminacion(idUsuario: Int, idContacto: Int) {
        val jsonBody = JSONObject()
        jsonBody.put("id_usuario", idUsuario)
        jsonBody.put("id_contacto", idContacto)

        val urlEliminar = "${BASE_URL}eliminar_contacto.php" // Usar tu script DELETE

        val stringRequest = object : StringRequest(
            Method.POST, urlEliminar,

            Response.Listener { respuesta ->
                Log.i(TAG, "Respuesta de Eliminación: $respuesta")
                try {
                    val objJSON = JSONObject(respuesta)
                    val success = objJSON.getBoolean("success")

                    if (success) {
                        Toast.makeText(this, "Contacto eliminado con éxito", Toast.LENGTH_LONG).show()

                        // Notificar a ContactosActivity para que se refresque la lista
                        setResult(RESULT_OK)
                        finish() // Cerrar DetalleActivity para regresar a la lista

                    } else {
                        val message = objJSON.getString("message")
                        Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error al parsear JSON después de eliminar:", e)
                    Toast.makeText(this, "Error de datos del servidor", Toast.LENGTH_LONG).show()
                }
            },

            Response.ErrorListener { error ->
                Toast.makeText(this, "Error de red al eliminar contacto", Toast.LENGTH_LONG).show()
                Log.e(TAG, "Error de Volley en Eliminación:", error)
            }
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
            override fun getBody(): ByteArray {
                return jsonBody.toString().toByteArray(charset("utf-8"))
            }
        }
        requestQueue.add(stringRequest)
    }

    private fun obtenerDetalleContacto(idUsuario: Int, idContacto: Int) {

        // JSON Body requiere ambos IDs
        val jsonBody = JSONObject()
        jsonBody.put("id_usuario", idUsuario)
        jsonBody.put("id_contacto", idContacto)

        val urlDetalle = "${BASE_URL}obtener_contactos.php"

        val stringRequest = object : StringRequest(
            Method.POST, urlDetalle,

            Response.Listener { respuesta ->
                Log.i(TAG, "Respuesta Detalle: $respuesta")

                try {
                    val objJSON = JSONObject(respuesta)
                    val success = objJSON.getBoolean("success")

                    if (success) {
                        // El script devuelve 'data' como un objeto JSON
                        val contactoData = objJSON.getJSONObject("data")

                        // Llenar la interfaz con la información detallada
                        llenarDetallesEnPantalla(contactoData)

                    } else {
                        val message = objJSON.getString("message")
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        finish()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error al parsear JSON:", e)
                    Toast.makeText(this, "Error de datos del servidor", Toast.LENGTH_LONG).show()
                }
            },

            Response.ErrorListener { error ->
                Toast.makeText(this, "Error de red al cargar detalle", Toast.LENGTH_LONG).show()
                Log.e(TAG, "Error de Volley en Detalle:", error)
            }

        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
            override fun getBody(): ByteArray {
                return jsonBody.toString().toByteArray(charset("utf-8"))
            }
        }
        requestQueue.add(stringRequest)
    }

    private fun llenarDetallesEnPantalla(data: JSONObject) {
        // Campos que vienen del servidor: id_contacto, id_usuario, nombre, direccion, telefono, correo, imagen_url
        val nombre = data.getString("nombre")
        val telefono = data.getString("telefono")
        val correo = data.getString("correo")
        val direccion = data.getString("direccion")
        val imagenUrl = data.getString("imagen_url")

        // 4. Asignar datos a las vistas
        supportActionBar?.title = nombre // Pone el nombre en el Toolbar
        tvNombreDetalle.text = nombre
        tvTelefonoDetalle.text = telefono
        tvCorreoDetalle.text = correo
        tvDireccionDetalle.text = direccion

        // Cargar imagen (REQUIERE LIBRERÍA DE IMAGEN como Glide)
        if (imagenUrl.isNotEmpty()) {
            // Glide.with(this).load(imagenUrl).placeholder(R.drawable.ic_default_user).into(ivFotoDetalle)
        }
    }
}