package com.example.ejemplosqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
    var adminSQLite: AdminSQLite? = null
    var conector: SQLiteDatabase? = null
    var fila: Cursor?= null

    lateinit var spn1: Spinner
    lateinit var spn2: Spinner

    lateinit var adapadorAlumno: ArrayAdapter<String>
    lateinit var adapadorMateria: ArrayAdapter<String>

    var listaAlumnos = mutableListOf<Alumno>()
    var listaMaterias = mutableListOf<Materia>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        spn1 = findViewById(R.id.spn1)
        spn2 = findViewById(R.id.spn2)

        adapadorAlumno = ArrayAdapter<String>(this@MainActivity2, android.R.layout.simple_spinner_dropdown_item)
        adapadorMateria = ArrayAdapter<String>(this@MainActivity2, android.R.layout.simple_spinner_dropdown_item)

        spn1.adapter = adapadorAlumno
        spn2.adapter = adapadorMateria

        llenarSpinnerAlumno(this@MainActivity2, "Escuela", "select * from alumnos", 1)
        llenarSpinnerMateria()

        spn1.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val datosAlert = arrayOfNulls<String>(1)

                datosAlert[0] = listaAlumnos.get(position).nombre


                var builder = AlertDialog.Builder(this@MainActivity2)

                builder.setTitle("Alumno Seleccionado")
                    .setItems(datosAlert){
                            dialog, pos ->

                    }
                builder.create().show()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }


    private fun llenarSpinnerAlumno(contexto: Context, bd: String, cadenaSQL: String, version: Int){
        adminSQLite= AdminSQLite(contexto, bd, version)
        conector= adminSQLite?.readableDatabase
        fila = conector?.rawQuery(cadenaSQL,null)
        var listaAlumnos: MutableList<Alumno> = mutableListOf()

        while (fila?.moveToNext() == true){
            listaAlumnos.add(Alumno(fila?.getString(0).toString(), fila?.getString(1).toString(),fila?.getString(2).toString() ))
            adapadorAlumno.add(fila?.getString(0).toString())
        }
        fila?.close()
        conector?.close()
    }

    private fun llenarSpinnerMateria (){
        listaMaterias = Materia.datos
        for (i in 0 until listaMaterias.size){
            adapadorMateria.add(listaMaterias.get(i).nombre)
        }
    }
}