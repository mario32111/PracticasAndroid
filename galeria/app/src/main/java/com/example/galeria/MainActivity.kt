package com.example.galeria

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var img1: ImageView
    lateinit var txt1: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

/*        img1.setOnClickListener (object : View.OnClickListener{
            override fun onClick(v: View?) {
                TODO("Not yet implemented")
            }
        })*/
        img1 = findViewById(R.id.imageView1)
        txt1 = findViewById(R.id.textView1)
        // se le pasa lo que voy a hacer (en este caso es un action pick, seleccionar una imagen)
        //ademas de donde va a obtener la uri de la imagen en esta caso va a brir la galeria y se van a abrir las imagenes
        img1.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)

            //aqui hay una respuesta del activity

        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // el 1 es que se haya selecccionado una imagen y el resul es que se le haya dado ok
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            val imagenURI= data.data
            val filePath = getPathFromURI(imagenURI)

            img1.setImageURI(imagenURI)

            //txt1.setText(filePath.toString())
            //.toUri() o Uri.parse() son lo mismo
            txt1.setText(imagenURI.toString())

            //TODO:construye un archhivo a partir de una ruta, se extrae la ruta del archivo

            //TODO: tres fragmento, en la parte de arriba poner un image view, dos radio button, poner un boton (ver), se vana estar seleccionando imagenes, lo simage view se van a crear en tiempo de ejecucion

        }
    }

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
}