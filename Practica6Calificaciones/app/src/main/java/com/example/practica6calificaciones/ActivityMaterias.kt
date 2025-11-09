package com.example.practica6calificaciones

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class ActivityMaterias : AppCompatActivity() {

    // --- 1. Declaración de Vistas ---
    private lateinit var editTextCodigoMateria: TextInputEditText
    private lateinit var editTextNombreMateria: TextInputEditText
    private lateinit var editTextCreditos: TextInputEditText
    private lateinit var buttonConsultar: Button
    private lateinit var buttonActualizar: Button
    private lateinit var buttonGuardar: Button
    private lateinit var buttonEliminar: Button
    private lateinit var buttonRegresar: ImageButton

    // --- 2. Declaración de variables de BD (misma lógica) ---
    var adminSQLite: AdminSQLite? = null
    var conector: SQLiteDatabase? = null
    var datos: ContentValues? = null
    var fila: Cursor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_materias)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- 3. Inicialización de Vistas ---
        buttonRegresar = findViewById(R.id.buttonRegresar)
        editTextCodigoMateria = findViewById(R.id.editTextCodigoMateria)
        editTextNombreMateria = findViewById(R.id.editTextNombreMateria)
        editTextCreditos = findViewById(R.id.editTextCreditos)
        buttonConsultar = findViewById(R.id.buttonConsultar)
        buttonActualizar = findViewById(R.id.buttonActualizar)
        buttonGuardar = findViewById(R.id.buttonGuardar)
        buttonEliminar = findViewById(R.id.buttonEliminar)

        // --- 4. Lógica de Botones (adaptada a Materias) ---

        buttonRegresar.setOnClickListener {
            finish()
        }

        buttonGuardar.setOnClickListener {
            adminSQLite = AdminSQLite(this@ActivityMaterias, "Escuela", 1)
            conector = adminSQLite?.writableDatabase

            datos = ContentValues()
            datos?.put("codigo", editTextCodigoMateria.text.toString())
            datos?.put("nombre", editTextNombreMateria.text.toString())
            datos?.put("creditos", editTextCreditos.text.toString()) // Guardado como TEXTO
            conector?.insert("materias", null, datos)
            conector?.close()
        }

        buttonConsultar.setOnClickListener {
            adminSQLite = AdminSQLite(this@ActivityMaterias, "Escuela", 1)
            conector = adminSQLite?.writableDatabase

            val codigo = editTextCodigoMateria.text.toString()
            fila = conector?.rawQuery("select * from materias where codigo='$codigo'", null)

            if (fila?.moveToFirst() == true) {
                // Columna 0 es 'codigo', 1 es 'nombre', 2 es 'creditos'
                editTextNombreMateria.setText(fila?.getString(1))
                editTextCreditos.setText(fila?.getString(2))
            }
            conector?.close()
        }

        buttonEliminar.setOnClickListener {
            adminSQLite = AdminSQLite(this@ActivityMaterias, "Escuela", 1)
            conector = adminSQLite?.writableDatabase

            val codigo = editTextCodigoMateria.text.toString()
            conector?.delete("materias", "codigo='$codigo'", null)
            conector?.close()
        }

        buttonActualizar.setOnClickListener {
            adminSQLite = AdminSQLite(this@ActivityMaterias, "Escuela", 1)
            conector = adminSQLite?.writableDatabase

            datos = ContentValues()
            datos?.put("nombre", editTextNombreMateria.text.toString())
            datos?.put("creditos", editTextCreditos.text.toString()) // Actualizado como TEXTO

            val codigo = editTextCodigoMateria.text.toString()
            conector?.update("materias", datos, "codigo='$codigo'", null)
            conector?.close()
        }
    }
}