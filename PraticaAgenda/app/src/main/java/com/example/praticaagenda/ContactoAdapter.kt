package com.example.praticaagenda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class ContactoAdapter(
    private val contactos: MutableList<Contacto>, // Se usa MutableList para permitir cambios
    private val clickListener: (Contacto) -> Unit // Lambda para manejar clics
) : RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder>() {

    class ContactoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivFotoContacto: CircleImageView = view.findViewById(R.id.ivFotoContacto)
        val tvNombreContacto: TextView = view.findViewById(R.id.tvNombreContacto)
        val tvTelefonoContacto: TextView = view.findViewById(R.id.tvTelefonoContacto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactoViewHolder {
        // Infla la plantilla item_contacto.xml
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contacto, parent, false)
        return ContactoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactoViewHolder, position: Int) {
        val contacto = contactos[position]

        holder.tvNombreContacto.text = contacto.nombre
        holder.tvTelefonoContacto.text = contacto.telefono

        // Carga de imagen (Necesitarías una librería como Glide o Picasso aquí)
        // Por ahora, solo usamos la imagen por defecto:
        holder.ivFotoContacto.setImageResource(R.drawable.ic_default_user)

        // Manejo de Clics
        holder.itemView.setOnClickListener {
            clickListener(contacto)
        }
    }

    override fun getItemCount(): Int = contactos.size
}