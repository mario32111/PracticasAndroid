package com.example.praticaagenda

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import org.json.JSONArray
import androidx.core.view.WindowCompat // Necesitas esta importación


class ContactosActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CONTACTOS_AGENDA_APP"
        private const val BASE_URL = "http://agendaa.atwebpages.com/"
    }

    private lateinit var toolbar: Toolbar
    private lateinit var rvContactos: RecyclerView
    private lateinit var fabAddContact: FloatingActionButton

    private lateinit var requestQueue: RequestQueue
    private var idUsuario: Int = -1

    private lateinit var contactoList: MutableList<Contacto>
    private lateinit var contactoAdapter: ContactoAdapter

    // Launcher para iniciar CrearContactoActivity y esperar un resultado (para refrescar la lista)
    private val crearContactoLauncher =
        registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Si el resultado es OK, recargar la lista de contactos
                obtenerContactos(idUsuario)
                Toast.makeText(this, "Lista actualizada.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contactos)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true


        // 1. Recoger ID y Comprobar
        idUsuario = intent.getIntExtra("id_usuario", -1)
        if (idUsuario == -1) {
            Toast.makeText(this, "Error de sesión.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // 2. Inicializar Vistas y Toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Mis Contactos"
        requestQueue = Volley.newRequestQueue(this)

        // 3. Configurar RecyclerView
        rvContactos = findViewById(R.id.rvContactos)
        contactoList = mutableListOf()

        // Define la acción al hacer clic en un contacto (Abrir DetalleActivity)
        contactoAdapter = ContactoAdapter(contactoList) { contacto ->
            abrirDetalleContacto(contacto.id_contacto)
        }
        rvContactos.layoutManager = LinearLayoutManager(this)
        rvContactos.adapter = contactoAdapter

        // 4. Configurar FAB con el Launcher
        fabAddContact = findViewById(R.id.fabAddContact)
        fabAddContact.setOnClickListener {
            val intent = Intent(this, activity_crear_contacto::class.java)
            intent.putExtra("id_usuario", idUsuario)
            crearContactoLauncher.launch(intent) // Usa el launcher para la recarga
        }

        // 5. Carga inicial de contactos
        obtenerContactos(idUsuario)
    }

    // --- Manejo de la Toolbar (Menú) ---
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contactos_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    // ------------------------------------
    private val detalleLauncher =
        registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Si regresa RESULT_OK (eliminación exitosa), recarga la lista
                obtenerContactos(idUsuario)
                Toast.makeText(this, "Lista de contactos recargada.", Toast.LENGTH_SHORT).show()
            }
        }
    private fun abrirDetalleContacto(idContacto: Int) {
        val intent = Intent(this, DetalleContactoActivity::class.java)
        intent.putExtra("id_usuario", idUsuario)
        intent.putExtra("id_contacto", idContacto)

        // USAR EL LAUNCHER EN LUGAR DE startActivity(intent)
        detalleLauncher.launch(intent)
    }

    private fun obtenerContactos(idUsuario: Int) {
        val jsonBody = JSONObject()
        jsonBody.put("id_usuario", idUsuario)

        val urlContactos = "${BASE_URL}obtener_contactos.php"

        val stringRequest = object : StringRequest(
            Method.POST, urlContactos,

            Response.Listener { respuesta ->
                Log.i(TAG, "Respuesta de Contactos: $respuesta")
                try {
                    val objJSON = JSONObject(respuesta)
                    val success = objJSON.getBoolean("success")

                    if (success) {
                        val contactosArray: JSONArray = objJSON.getJSONArray("data")

                        contactoList.clear() // Vaciar lista existente
                        for (i in 0 until contactosArray.length()) {
                            val contactoJson = contactosArray.getJSONObject(i)
                            contactoList.add(
                                Contacto(
                                    id_contacto = contactoJson.getInt("id_contacto"),
                                    nombre = contactoJson.getString("nombre"),
                                    telefono = contactoJson.getString("telefono"),
                                    imagen_url = contactoJson.getString("imagen_url")
                                )
                            )
                        }
                        contactoAdapter.notifyDataSetChanged() // Refrescar la vista
                    } else {
                        val message = objJSON.getString("message")
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error al parsear JSON:", e)
                    Toast.makeText(this, "Error al procesar datos del servidor", Toast.LENGTH_LONG).show()
                }
            },

            Response.ErrorListener { error ->
                Toast.makeText(this, "Error de red al cargar contactos", Toast.LENGTH_LONG).show()
                Log.e(TAG, "Error de Volley:", error)
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
}