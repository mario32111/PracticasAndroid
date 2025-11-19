package com.example.ejemploapirest

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Hacemos los campos "nullable" (?) porque a veces la API devuelve nulos
@Parcelize
data class Articles(
    val author: String?,
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val content: String?
) : Parcelable