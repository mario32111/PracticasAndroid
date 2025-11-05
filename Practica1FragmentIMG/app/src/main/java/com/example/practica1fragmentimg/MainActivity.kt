package com.example.practica1fragmentimg

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var imgVw1: ImageView
    lateinit var btn1: Button
    lateinit var radioBtn1: RadioButton
    lateinit var radioBtn2: RadioButton
    lateinit var contenedor: FrameLayout

    lateinit var fotosEscuela: Bundle
    lateinit var fotosTrabajo: Bundle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgVw1 = findViewById(R.id.imageView1)
        btn1 = findViewById(R.id.button1)
        contenedor = findViewById(R.id.contFragment)
        radioBtn1 = findViewById(R.id.radioButton1)
        radioBtn2 = findViewById(R.id.radioButton2)

        fotosEscuela = Bundle()
        fotosTrabajo = Bundle()
        btn1.setOnClickListener {
            if (radioBtn1.isChecked) cargarFragmento(escuelaFragment())
            if (radioBtn2.isChecked) cargarFragmento(trabajoFragment())
        }

        imgVw1.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // el 1 es que se haya selecccionado una imagen y el resul es que se le haya dado ok
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            val imagenURI= data.data


            val filePath : String? = getPathFromURI(imagenURI)

            imgVw1.setImageURI(imagenURI)

            if (radioBtn1.isChecked) {
                val count = fotosEscuela.size() // cuántos elementos hay ya
                fotosEscuela.putString("foto_$count", filePath)
            }

            if (radioBtn2.isChecked) {
                val count = fotosTrabajo.size() // cuántos elementos hay ya
                fotosTrabajo.putString("foto_$count", filePath)
            }
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


    private fun cargarFragmento(fragmento: androidx.fragment.app.Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        if (radioBtn1.isChecked) {
            fragmento.arguments = fotosEscuela
        }

        if (radioBtn2.isChecked) {
            fragmento.arguments = fotosTrabajo
        }


        fragmentTransaction.replace(R.id.contFragment, fragmento)
        fragmentTransaction.commit()
    }

}