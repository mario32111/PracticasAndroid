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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [escuelaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class escuelaFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_escuela, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Obtenemos el contenedor del layout del fragmento
        val contenedor = view.findViewById<LinearLayout>(R.id.llContenedorFotos)

        // Obtenemos el Bundle que fue pasado desde la Activity
        val fotosBundle = arguments

        // Verificamos si el Bundle no es nulo y tiene contenido
        if (fotosBundle != null && !fotosBundle.isEmpty) {

            // Obtenemos todas las claves (keys) del Bundle. Cada clave corresponde a una foto.
            for (key in fotosBundle.keySet()) {

                // Obtenemos la ruta del archivo (String) usando la clave
                val filePath = fotosBundle.getString(key)

                if (filePath != null) {
                    // Creamos una nueva ImageView
                    val imageView = ImageView(context)

                    // Convertimos la ruta del archivo a una URI y la establecemos en la ImageView
                    val imageUri = Uri.parse(filePath)
                    imageView.setImageURI(imageUri)

                    // Configuramos los parámetros de diseño para la ImageView
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // Ancho
                        500 // Alto en píxeles. Puedes ajustarlo.
                    )
                    params.bottomMargin = 30 // Un pequeño margen entre las imágenes
                    imageView.layoutParams = params
                    imageView.scaleType = ImageView.ScaleType.CENTER_CROP // Ajusta la escala de la imagen

                    // Añadimos la nueva ImageView al contenedor del layout
                    contenedor.addView(imageView)
                }
            }
        } else {
            // Si el Bundle está vacío o no se pasó, mostramos un mensaje.
            val textView = TextView(context)
            textView.text = "No hay fotos para mostrar en esta categoría."
            contenedor.addView(textView)
        }

    }

    companion object {

    }
}