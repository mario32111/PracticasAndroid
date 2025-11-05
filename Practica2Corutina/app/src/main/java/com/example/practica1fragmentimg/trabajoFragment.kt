package com.example.practica1fragmentimg

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [trabajoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class trabajoFragment : Fragment(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        //con esto se le especifica de que proceso se va a colgar, comunmente se usa main e IO
        get()= Dispatchers.Main


    private var proceso1: Job?= null
    var time: Int =0

    var idxImage: Int =0
    var fotosBundle: Bundle? = null

    var totalImageKeys: Int?= null

    lateinit var img: ImageView
    //esta es la tarea a correr, lista para ejecutarla en segundo plano
    fun correrTiempo(){
        proceso1 =  launch (Dispatchers.Main){
            while (true){
                delay(1000)

                if (time % 5 == 0 || idxImage==0) {
                    cambiarImagen()
                }else{
                    time++
                }
            }
        }
    }

    fun cambiarImagen(){

            // Construimos la clave dinámica
            val key = "foto_$idxImage"

            // 2. Intentamos obtener la ruta, que puede ser nula.
            val filePath = fotosBundle?.getString(key)

            // 3. Verificamos que la ruta no sea nula y que la clave exista.
            if (!filePath.isNullOrEmpty()) {

                // 4. Si la ruta es válida, la convertimos a URI y la establecemos.
                img.setImageURI(filePath.toUri())
                idxImage++
            }
            if (idxImage ==totalImageKeys) idxImage=0

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        fotosBundle = arguments

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trabajo, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        img= view.findViewById(R.id.imageView1)
        // Obtenemos el Bundle que fue pasado desde la Activity
        proceso1?.cancel()

        cambiarImagen()
        totalImageKeys = fotosBundle?.keySet()?.size ?: 0

        correrTiempo()

    }

    companion object {

    }
}
