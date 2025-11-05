package com.example.ejemploreciclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//es un adaptaidor para el reciclerview
//esto es el operador, hace todas las operaciones necesarias en el adapter
//necesita de un holder
//es lo equivalente al array adapter
//se parte de una clase
class ArticleAdapter: RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    //se usa una variable de tipo array list y se define su su metodo set
    //esto agrega nuevos elementos al article
    var articles = arrayListOf<Article>()
        set(value){
            //el field es la informacion que va a tener
            field= value
            notifyDataSetChanged()
        }

    //dibuja la vista, crea la vista pero sin datos
    //hace un elemento vista desde el contexto establecido, lo infla en vase al los items
    //y se agrega al contenedor padre (el recicler view)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.article_item,parent,false)

        return ArticleViewHolder(view)
    }


    //vincula cada uno de los elementos, por eso hay que enviar cada elemento al holder

    override fun onBindViewHolder(
        holder: ArticleViewHolder,
        position: Int
    ) {
        val article = articles[position]
        holder.vincular(article)
    }

    //con esto sabe cuantas veces iterar en los items
    override fun getItemCount()= articles.size

    //manipula los datos con lo que tiene la vista
    //esto vincula los datos con la vista
    class ArticleViewHolder(view: View): RecyclerView.ViewHolder(view){
        private var title: TextView = view.findViewById(R.id.article_tittle)
        private var description: TextView = view.findViewById(R.id.article_description)
        private var image: ImageView = view.findViewById(R.id.article_image)

        fun vincular (article: Article){
            title.text = article.tittle
            description.text = article.description
            image.setImageResource(article.imageRes)
        }

    }

}