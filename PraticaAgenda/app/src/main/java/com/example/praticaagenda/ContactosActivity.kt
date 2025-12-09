package com.example.praticaagenda

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar // Importar Toolbar de appcompat

class ContactosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contactos)

        // 1. Obtener la referencia al Toolbar usando el ID 'toolbar'
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        // 2. Establecer el Toolbar como la ActionBar de la actividad
        setSupportActionBar(toolbar)

        // Configurar el título del Toolbar (opcional, ya está en XML)
        // supportActionBar?.title = "Mis Contactos"

        // (El resto del código de ViewCompat es opcional si usas enableEdgeToEdge)
    }

    // 3. Inflar (cargar) el menú en la Toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Asumiendo que el archivo de menú se llama menu_contactos.xml
        menuInflater.inflate(R.menu.menu_contactos_list, menu)
        return true
    }

    // 4. Manejar las acciones del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                // Lógica para cerrar la sesión:
                // 1. Eliminar datos de sesión (ej. SharedPreferences)
                // 2. Redirigir al usuario a la pantalla de Login (MainActivity)

                Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show()

                 val intent = Intent(this, MainActivity::class.java)
                 intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                 startActivity(intent)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}