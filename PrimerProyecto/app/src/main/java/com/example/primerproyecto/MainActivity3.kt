package com.example.primerproyecto

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import com.squareup.picasso.Picasso
import java.io.File

class MainActivity3 : AppCompatActivity() {
    lateinit var img1: ImageView
    lateinit var btn1: Button

    lateinit var btn2: Button
    //private val pickImage = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        img1 = findViewById(R.id.imageView1)
        btn1 = findViewById(R.id.button1)
        btn2 = findViewById(R.id.button2)

        btn1.setOnClickListener {
            val ruta= "/storage/emulated/0/Pictures/Lionel_Messi_NE_Revolution_Inter_Miami_7.9.25-055.jpg"
            cargarImgPicasso(ruta)
        }

        btn2.setOnClickListener {
            val ruta= "/storage/emulated/0/Pictures/Lionel_Messi_NE_Revolution_Inter_Miami_7.9.25-055.jpg"
            cargarImgBitmap(ruta)

        }

    }
    fun cargarImgPicasso (path: String){
        /*            val gallery = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)*/
        val file = File(path)
        Picasso.get().load(file).into(img1)
    }

    fun cargarImgBitmap (path: String){
        val file = File(path)


        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            img1.setImageBitmap(bitmap)
        } else {
            // Manejar el caso en que el archivo no existe
        }
    }

/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            val imageUri: Uri? = data?.data
            img1.setImageURI(imageUri)
        }
    }*/
}