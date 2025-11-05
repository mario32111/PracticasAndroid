package com.example.ejemploreciclerview
//esto es el equivalente a la interfaz

data class Article( val tittle: String,  val description: String, val imageRes: Int) {
    companion object {
        val data
            get() = arrayListOf(
                Article(
                    "Nezuko",
                    description = "Hermana de Tanjiro",
                    R.mipmap.im4
                ),
                Article(
                    "Nezuko2",
                    description = "Hermana de Tanjiro",
                    R.mipmap.im4
                ),
                Article(
                    "Nezuko3",
                    description = "Hermana de Tanjiro",
                    R.mipmap.im4
                ),
                Article(
                    "Nezuko4",
                    description = "Hermana de Tanjiro",
                    R.mipmap.im4
                ),
            )
    }
}