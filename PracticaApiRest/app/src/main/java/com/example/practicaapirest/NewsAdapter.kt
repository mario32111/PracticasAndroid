package com.example.practicaapirest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ejemploapirest.Articles
import com.squareup.picasso.Picasso

class NewsAdapter(
    private var lista: List<Articles>,
    private val onNoticiaClick: (Articles) -> Unit // Lambda para manejar el click
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.txtTitleItem)
        val autor: TextView = view.findViewById(R.id.txtAuthorItem)
        val imagen: ImageView = view.findViewById(R.id.imgItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val articulo = lista[position]
        holder.titulo.text = articulo.title
        holder.autor.text = articulo.author ?: "Desconocido"

        // Cargar imagen con Picasso, validando si viene vacía
        if (!articulo.urlToImage.isNullOrEmpty()) {
            Picasso.get().load(articulo.urlToImage).into(holder.imagen)
        }

        // Configurar el click
        holder.itemView.setOnClickListener {
            onNoticiaClick(articulo)
        }
    }

    override fun getItemCount(): Int = lista.size

    // Método para actualizar la lista sin recrear el adapter
    fun actualizarLista(nuevaLista: List<Articles>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}