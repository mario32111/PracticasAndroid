package com.example.practica4recyclerview

import android.net.Uri

//esto es el equivalente a la interfaz

data class Article(val tittle: String, val description: String, val imageRes: String?) {
    companion object {
        val data
            get() = arrayListOf(
                Article(
                    "Nezuko",
                    description = "Hermana de Tanjiro",
                    imageRes = null
                )

            )
    }
}