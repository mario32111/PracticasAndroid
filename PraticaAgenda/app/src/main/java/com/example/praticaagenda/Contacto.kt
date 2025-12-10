package com.example.praticaagenda

data class Contacto(
    val id_contacto: Int,
    val nombre: String,
    val telefono: String,
    val imagen_url: String? = null
)