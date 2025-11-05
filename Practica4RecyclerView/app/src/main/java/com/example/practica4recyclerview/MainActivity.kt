package com.example.practica4recyclerview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    lateinit var btn1: Button
    lateinit var edtText1: EditText
    lateinit var edtText2: EditText
    lateinit var recyclerView1: RecyclerView
    lateinit var adaptador: ArticleAdapter
    lateinit var img1: ImageView
    var filePath: String? = null // Inicializa correctamente a null

    private fun getPathFromURI(uri: Uri?): String?{
        //sacamos el aray de datos seleccionados, establecemos un apuntador y el apuntador lo movemos a la primera posicion
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri!!, projection, null, null, null)
        cursor?.moveToFirst()

        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

        val filePath = cursor?.getString(columnIndex!!)

        cursor?.close()

        return filePath
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // el 1 es que se haya selecccionado una imagen y el resul es que se le haya dado ok
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            val imagenURI= data.data

            img1.setImageURI(imagenURI)
            filePath = getPathFromURI(imagenURI)

        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn1 = findViewById(R.id.btn1)
        edtText1 = findViewById(R.id.edtText1)
        edtText2 = findViewById(R.id.edtText2)
        recyclerView1= findViewById(R.id.recicletView1)
        img1= findViewById(R.id.img1)
        adaptador= ArticleAdapter()
        recyclerView1.adapter= adaptador


        img1.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)
        }
        btn1.setOnClickListener {
            val tittle = edtText1.text.toString()
            val description = edtText2.text.toString()
            //al array list se le asigna lo del companion object de la clase article
            adaptador.articles.add(Article(tittle, description, filePath) )
            adaptador.notifyDataSetChanged()
        }
    }
}