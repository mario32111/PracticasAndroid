package com.example.ejemplosqlite

data class Materia(
    val codigoM: String,
    val nombre: String,
    val creditos: String
){
    companion object{
        val datos
            get() = arrayListOf(
                Materia(
                    "23232",
                    "Desarrollo de aplicaciones moviles",
                    "5"
                ),
                Materia(
                    "12433",
                    "Legislacion Informatica",
                    "4"
                ),
                Materia(
                    "38461",
                    "Calidad de Software",
                    "5"
                )
            )
    }
}

