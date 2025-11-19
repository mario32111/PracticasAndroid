package com.example.practicaapirest

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ejemploapirest.Articles
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val articulo = intent.getParcelableExtra<Articles>("EXTRA_ARTICLE")

        if (articulo != null) {
            val img: ImageView = findViewById(R.id.imgDetail)
            val title: TextView = findViewById(R.id.txtTitleDetail)
            val content: TextView = findViewById(R.id.txtContentDetail)

            title.text = articulo.title
            content.text = articulo.description ?: articulo.content // Usamos descripcion si content es nulo

            if (!articulo.urlToImage.isNullOrEmpty()) {
                Picasso.get().load(articulo.urlToImage).into(img)
            }
        }
    }
}