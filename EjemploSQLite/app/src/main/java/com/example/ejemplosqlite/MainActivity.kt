package com.example.ejemplosqlite

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.Int
import kotlin.arrayOfNulls

class MainActivity : AppCompatActivity() {
    lateinit var edt1: EditText
    lateinit var edt2: EditText

    lateinit var edt3: EditText

    lateinit var btn1: Button
    lateinit var btn2: Button
    lateinit var btn3: Button
    lateinit var btn4: Button

    lateinit var btn5: Button
    lateinit var btn6: Button
    lateinit var btn7: Button

    val db = Firebase.firestore

    var adminSQLite: AdminSQLite? = null
    var conector: SQLiteDatabase? = null
    var datos: ContentValues? = null
    var fila: Cursor?= null
    lateinit var datosAlert: Array<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edt1= findViewById(R.id.edtxt1)
        edt2= findViewById(R.id.edtxt2)
        edt3= findViewById(R.id.edtxt3)

        btn1= findViewById(R.id.btn1)
        btn2= findViewById(R.id.btn2)
        btn3= findViewById(R.id.btn3)
        btn4= findViewById(R.id.btn4)
        btn5= findViewById(R.id.btn5)
        btn6= findViewById(R.id.btn6)
        btn7= findViewById(R.id.btn7)


        btn1.setOnClickListener {
            adminSQLite= AdminSQLite(this@MainActivity, "Escuela", 1)
            conector= adminSQLite?.writableDatabase

            datos = ContentValues()
            datos?.put("matricula", edt1.text.toString())
            datos?.put("nombre", edt2.text.toString())
            datos?.put("carrera", edt3.text.toString())

            conector?.insert("alumnos", null, datos)
            conector?.close()
        }

        btn2.setOnClickListener {
            createAlertDialog()
        }

        btn3.setOnClickListener {
            var query = "delete from alumnos where matricula = '${edt1.text.toString()}'"
            eliminarDatos(this@MainActivity, "Escuela", 1, query)
        }

        btn4.setOnClickListener {
            var query = "update alumnos set nombre = '${edt2.text.toString()}', carrera = '${edt3.text.toString()}' where matricula = '${edt1.text.toString()}'"
            actualizarDatos(this@MainActivity, "Escuela", 1, query)
        }

        btn5.setOnClickListener {
            // Create a new user with a first and last name
            val user = hashMapOf(
                "first" to "Ada",
                "last" to "Lovelace",
                "born" to 1815
            )

            db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }

        btn6.setOnClickListener {
            val data= consultarDatos(
                this@MainActivity,
                "Escuela",
                1,
                "select * from alumnos where matricula = ${edt1.text.toString()}")


            edt2.text.clear()
            edt3.text.clear()
            while (data!!.moveToNext()){
                edt2.setText(data?.getString(1).toString())
                edt3.setText(data?.getString(2).toString())
            }

        }

        btn7.setOnClickListener {
            val data= consultarDatosClass(
                this@MainActivity,
                "Escuela",
                1,
                "select * from alumnos where matricula = ${edt1.text.toString()}")


                edt2.setText(data[0].nombre.toString())
                edt3.setText(data[0].carrera.toString())


        }
    }

    private fun consultarDatosClass( contexto: Context, bd: String, version: Int, query: String): MutableList<Alumno>{
        adminSQLite= AdminSQLite(contexto, bd, version)
        conector= adminSQLite?.readableDatabase
        fila = conector?.rawQuery(query,null)
        var listaAlumnos: MutableList<Alumno> = mutableListOf()

        while (fila?.moveToNext() == true){
            listaAlumnos.add(Alumno(fila?.getString(0).toString(), fila?.getString(1).toString(),fila?.getString(2).toString() ))
        }
        fila?.close()
        conector?.close()
        return listaAlumnos;
    }

    private fun consultarDatos( contexto: Context, bd: String, version: Int, query: String): Cursor?{
        adminSQLite= AdminSQLite(contexto, bd, version)
        conector= adminSQLite?.readableDatabase
        fila = conector?.rawQuery(query,null)
        //conector?.close()
        return fila;
    }

    private fun createAlertDialog(){
        var builder = AlertDialog.Builder(this@MainActivity)

        var i:Int=0
        var infoAlert: Array<String?>
        var datosBD = consultarDatos(this@MainActivity, "Escuela", 1, "select * from alumnos")
        infoAlert=  arrayOfNulls(datosBD!!.count)


        while (fila!!.moveToNext()){
            infoAlert[i]= fila?.getString(1).toString()
            i++
        }
        //el builder es ocmo una caja cerrada que tiene entrada y salida de datos y tiene que haber un intermediario que manipule la informacion
        //en este caso el objeto es dialog
        builder.setTitle("Selecciona tu carrera")
            .setItems(infoAlert){
                    dialog, pos ->
                Toast.makeText(this@MainActivity,  datosAlert[pos].toString(), Toast.LENGTH_SHORT).show()
            }
        builder.create().show()
        conector?.close()
    }

    private fun eliminarDatos( contexto: Context, bd: String, version: Int, query: String){
        adminSQLite= AdminSQLite(contexto, bd, version)
        conector= adminSQLite?.writableDatabase
        conector?.execSQL(query)
        conector?.close()
    }
    private fun actualizarDatos( contexto: Context, bd: String, version: Int, query: String){
        adminSQLite= AdminSQLite(contexto, bd, version)
        conector= adminSQLite?.writableDatabase
        conector?.execSQL(query)
        conector?.close()
    }

}