package com.example.ejemplofragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner


class FragmentAzul : Fragment() {


    private var fotoSelect: Int? = null // ¡El cambio está aquí!
    lateinit var imagen1: ImageView

    lateinit var adaptador : ArrayAdapter<String>

    lateinit var lista: ArrayList<String>

    lateinit var spinner1: Spinner

    //este metodo es cuando se crea el objeto (las instancias)
    override fun onCreate(savedInstanceState: Bundle?) {
        //it es un objeto en el cua se le delega a traves del ley
        super.onCreate(savedInstanceState)
        arguments?.let {
            fotoSelect=it.getInt("foto")
            lista= it.getStringArrayList("lista")!!
        }


    }

    //cuando ya se visualizan los elementos
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imagen1 = view.findViewById(R.id.imageViewAzul)


        spinner1 = view.findViewById(R.id.spinner1)
        adaptador = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item)

        adaptador.addAll(lista)

        spinner1.adapter = adaptador
        fotoSelect?.let {
            imagen1.setImageResource(it)
        }
    }

    //cuando el objeto a visualizar ya creados y reunidos
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_azul, container, false)
    }

    companion object {

    }
}