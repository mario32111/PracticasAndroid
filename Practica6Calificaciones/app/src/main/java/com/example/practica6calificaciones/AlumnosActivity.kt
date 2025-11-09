package com.example.practica6calificaciones

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class AlumnosActivity : AppCompatActivity() {
    private lateinit var editTextMatricula: TextInputEditText
    private lateinit var editTextNombre: TextInputEditText
    private lateinit var editTextCarrera: TextInputEditText
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var buttonConsultar: Button
    private lateinit var buttonActualizar: Button
    private lateinit var buttonGuardar: Button
    private lateinit var buttonEliminar: Button
    private lateinit var buttonRegresar: ImageButton
    var adminSQLite: AdminSQLite? = null
    var conector: SQLiteDatabase? = null
    var datos: ContentValues? = null
    var fila: Cursor?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alumnos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editTextMatricula = findViewById(R.id.editTextMatricula)
        editTextNombre = findViewById(R.id.editTextNombre)
        editTextCarrera = findViewById(R.id.editTextCarrera)
        editTextEmail = findViewById(R.id.editTextEmail)
        buttonConsultar = findViewById(R.id.buttonConsultar)
        buttonActualizar = findViewById(R.id.buttonActualizar)
        buttonGuardar = findViewById(R.id.buttonGuardar)
        buttonEliminar = findViewById(R.id.buttonEliminar)
        buttonRegresar = findViewById(R.id.buttonRegresar)

        buttonRegresar.setOnClickListener {
            finish()
        }
        buttonGuardar.setOnClickListener {
            adminSQLite= AdminSQLite(this@AlumnosActivity, "Escuela", 1)
            conector= adminSQLite?.writableDatabase

            datos = ContentValues()
            datos?.put("matricula", editTextMatricula.text.toString())
            datos?.put("nombre", editTextNombre.text.toString())
            datos?.put("carrera", editTextCarrera.text.toString())
            datos?.put("email", editTextEmail.text.toString())
            conector?.insert("alumnos", null, datos)
            conector?.close()
        }

        buttonConsultar.setOnClickListener {
            adminSQLite= AdminSQLite(this@AlumnosActivity, "Escuela", 1)
            conector= adminSQLite?.writableDatabase

            fila= conector?.rawQuery("select * from alumnos where matricula='" + editTextMatricula.text.toString() + "'", null)
            if (fila?.moveToFirst() == true) {
                editTextNombre.setText(fila?.getString(1))
                editTextCarrera.setText(fila?.getString(2))
                editTextEmail.setText(fila?.getString(3))
            }
            conector?.close()
            }
        buttonEliminar.setOnClickListener {
            adminSQLite= AdminSQLite(this@AlumnosActivity, "Escuela", 1)
            conector= adminSQLite?.writableDatabase

            conector?.delete("alumnos", "matricula='" + editTextMatricula.text.toString() + "'", null)
            conector?.close()
        }
        buttonActualizar.setOnClickListener {
            adminSQLite= AdminSQLite(this@AlumnosActivity, "Escuela", 1)
            conector= adminSQLite?.writableDatabase

            datos = ContentValues()
            datos?.put("nombre", editTextNombre.text.toString())
            datos?.put("carrera", editTextCarrera.text.toString())
            datos?.put("email", editTextEmail.text.toString())
            conector?.update("alumnos", datos, "matricula='" + editTextMatricula.text.toString() + "'", null)
            conector?.close()
        }
    }

}