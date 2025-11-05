package com.example.ejemplofragment

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var btnAzul: Button
    lateinit var btnAmarillo: Button
    lateinit var contenedor: FrameLayout

    lateinit var radioBtn1: RadioButton
    lateinit var radioBtn2: RadioButton
    lateinit var radioBtn3: RadioButton

    lateinit var arrayList1: ArrayList<String>
    lateinit var datos: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        btnAzul = findViewById(R.id.button1)
        btnAmarillo = findViewById(R.id.button2)
        contenedor = findViewById(R.id.contFragment)
        radioBtn1 = findViewById(R.id.radioButton1)
        radioBtn2 = findViewById(R.id.radioButton2)
        radioBtn3 = findViewById(R.id.radioButton3)

        datos= Bundle()


        btnAzul.setOnClickListener {
            cargarFragmento(FragmentAzul(),checkState())
        }

        btnAmarillo.setOnClickListener {
            cargarFragmento(FragmentAmarillo(),checkState())
        }



    }

    private fun cargarFragmento(fragmento: androidx.fragment.app.Fragment, select: Int){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        arrayList1= arrayListOf()

        arrayList1.add("Quirino")
        arrayList1.add("Daniel")
        arrayList1.add("Nancy")
        arrayList1.add("Mario")
        arrayList1.add("Alan")
        arrayList1.add("Angel")

        datos.putStringArrayList("lista",arrayList1)

        datos.putInt("foto", select)
        fragmento.arguments = datos

        fragmentTransaction.replace(R.id.contFragment, fragmento)
        fragmentTransaction.commit()
    }

    private fun checkState () : Int{
        if (radioBtn1.isChecked) return (R.mipmap.img1)

        if (radioBtn2.isChecked) return (R.mipmap.im1)

        if (radioBtn3.isChecked) return (R.mipmap.ic_launcher_round)

        return 0
    }
}