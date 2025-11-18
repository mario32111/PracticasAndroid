package com.example.practica6calificaciones

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class CalificacionesActivity : AppCompatActivity() {

    // --- 1. Declaración de Vistas ---
    private lateinit var buttonRegresar: ImageButton
    private lateinit var autoCompleteAlumno: AutoCompleteTextView
    private lateinit var autoCompleteMateria: AutoCompleteTextView
    private lateinit var editTextUnidad1: TextInputEditText
    private lateinit var editTextUnidad2: TextInputEditText
    private lateinit var editTextUnidad3: TextInputEditText
    private lateinit var editTextUnidad4: TextInputEditText
    private lateinit var buttonGuardar: Button
    private lateinit var buttonConsultar: Button
    private lateinit var buttonActualizar: Button
    private lateinit var buttonEliminar: Button

    // --- 2. Declaración de variables de BD (misma lógica) ---
    var adminSQLite: AdminSQLite? = null
    var conector: SQLiteDatabase? = null
    var datos: ContentValues? = null
    var fila: Cursor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calificaciones)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- 3. Inicialización de Vistas ---
        buttonRegresar = findViewById(R.id.buttonRegresar)
        autoCompleteAlumno = findViewById(R.id.autoCompleteAlumno)
        autoCompleteMateria = findViewById(R.id.autoCompleteMateria)
        editTextUnidad1 = findViewById(R.id.editTextUnidad1)
        editTextUnidad2 = findViewById(R.id.editTextUnidad2)
        editTextUnidad3 = findViewById(R.id.editTextUnidad3)
        editTextUnidad4 = findViewById(R.id.editTextUnidad4)
        buttonGuardar = findViewById(R.id.buttonGuardar)
        buttonConsultar = findViewById(R.id.buttonConsultar)
        buttonActualizar = findViewById(R.id.buttonActualizar)
        buttonEliminar = findViewById(R.id.buttonEliminar)

        // --- 4. Cargar datos en Spinners ---
        cargarSpinners()

        // --- 5. Lógica de Botones ---
        buttonRegresar.setOnClickListener {
            finish()
        }

        buttonGuardar.setOnClickListener {
            adminSQLite = AdminSQLite(this@CalificacionesActivity, "Escuela", 1)
            conector = adminSQLite?.writableDatabase

            // Extraer IDs de los AutoCompleteTextView
            val matricula = autoCompleteAlumno.text.toString().split(" - ")[0]
            val codigo = autoCompleteMateria.text.toString().split(" - ")[0]

            datos = ContentValues()
            datos?.put("matricula", matricula)
            datos?.put("codigo", codigo)
            datos?.put("unidad1", editTextUnidad1.text.toString())
            datos?.put("unidad2", editTextUnidad2.text.toString())
            datos?.put("unidad3", editTextUnidad3.text.toString())
            datos?.put("unidad4", editTextUnidad4.text.toString())

            conector?.insert("calificaciones", null, datos)
            conector?.close()
        }

        buttonConsultar.setOnClickListener {
            adminSQLite = AdminSQLite(this@CalificacionesActivity, "Escuela", 1)
            conector = adminSQLite?.writableDatabase

            val matricula = autoCompleteAlumno.text.toString().split(" - ")[0]
            val codigo = autoCompleteMateria.text.toString().split(" - ")[0]

            fila = conector?.rawQuery(
                "SELECT * FROM calificaciones WHERE matricula='$matricula' AND codigo='$codigo'",
                null
            )

            if (fila?.moveToFirst() == true) {
                // 0=matricula, 1=codigo, 2=u1, 3=u2, 4=u3, 5=u4
                editTextUnidad1.setText(fila?.getString(2))
                editTextUnidad2.setText(fila?.getString(3))
                editTextUnidad3.setText(fila?.getString(4))
                editTextUnidad4.setText(fila?.getString(5))
            } else {
                Toast.makeText(this, "No se encontró calificación", Toast.LENGTH_SHORT).show()
            }
            conector?.close()
        }

        buttonActualizar.setOnClickListener {
            adminSQLite = AdminSQLite(this@CalificacionesActivity, "Escuela", 1)
            conector = adminSQLite?.writableDatabase

            val matricula = autoCompleteAlumno.text.toString().split(" - ")[0]
            val codigo = autoCompleteMateria.text.toString().split(" - ")[0]

            datos = ContentValues()
            datos?.put("unidad1", editTextUnidad1.text.toString())
            datos?.put("unidad2", editTextUnidad2.text.toString())
            datos?.put("unidad3", editTextUnidad3.text.toString())
            datos?.put("unidad4", editTextUnidad4.text.toString())

            conector?.update(
                "calificaciones",
                datos,
                "matricula='$matricula' AND codigo='$codigo'",
                null
            )
            conector?.close()
        }

        buttonEliminar.setOnClickListener {
            adminSQLite = AdminSQLite(this@CalificacionesActivity, "Escuela", 1)
            conector = adminSQLite?.writableDatabase

            val matricula = autoCompleteAlumno.text.toString().split(" - ")[0]
            val codigo = autoCompleteMateria.text.toString().split(" - ")[0]

            conector?.delete(
                "calificaciones",
                "matricula='$matricula' AND codigo='$codigo'",
                null
            )
            conector?.close()
        }
    }

    /**
     * Consulta las tablas 'alumnos' y 'materias' para llenar los menús desplegables.
     */
    private fun cargarSpinners() {
        // --- Cargar Alumnos ---
        val alumnosList = ArrayList<String>()
        adminSQLite = AdminSQLite(this, "Escuela", 1)
        conector = adminSQLite?.readableDatabase
        fila = conector?.rawQuery("SELECT matricula, nombre FROM alumnos", null)

        if (fila?.moveToFirst() == true) {
            do {
                val matricula = fila!!.getString(0)
                val nombre = fila!!.getString(1)
                alumnosList.add("$matricula - $nombre")
            } while (fila!!.moveToNext())
        }
        fila?.close()
        conector?.close()

        val alumnosAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, alumnosList)
        autoCompleteAlumno.setAdapter(alumnosAdapter)

        // --- Cargar Materias ---
        val materiasList = ArrayList<String>()
        conector = adminSQLite?.readableDatabase
        fila = conector?.rawQuery("SELECT codigo, nombre FROM materias", null)

        if (fila?.moveToFirst() == true) {
            do {
                val codigo = fila!!.getString(0)
                val nombre = fila!!.getString(1)
                materiasList.add("$codigo - $nombre")
            } while (fila!!.moveToNext())
        }
        fila?.close()
        conector?.close()

        val materiasAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, materiasList)
        autoCompleteMateria.setAdapter(materiasAdapter)
    }
}