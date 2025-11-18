package com.example.ejemploapirest

data class Categoria(
    val nombre: String,
    val valor: String
) {
    override fun toString(): String {
        return nombre
    }

    companion object {
        val datos
            get() = arrayListOf(
                Categoria("Negocios", "business"),
                Categoria("Entretenimiento", "entertainment"),
                Categoria("General", "general"),
                Categoria("Salud", "health"),
                Categoria("Ciencia", "science"),
                Categoria("Deportes", "sports"),
                Categoria("Tecnología", "technology")
            )
    }
}
