package com.example.practicaapirest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ejemploapirest.Articles
import com.example.ejemploapirest.Categoria
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var spn1: Spinner
    private lateinit var rvNoticias: RecyclerView
    private lateinit var adapterNoticias: NewsAdapter

    // Reemplaza esto con tu API KEY real si cambia
    private val API_KEY = "dfa30107b91b4b35a578cda6e89b37f4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Configurar Views
        spn1 = findViewById(R.id.spn1)
        rvNoticias = findViewById(R.id.rvNoticias)

        // 2. Configurar RecyclerView
        rvNoticias.layoutManager = LinearLayoutManager(this)
        // Inicializamos el adapter con lista vacía y la acción de click
        adapterNoticias = NewsAdapter(emptyList()) { articuloSeleccionado ->
            abrirDetalle(articuloSeleccionado)
        }
        rvNoticias.adapter = adapterNoticias

        // 3. Configurar Spinner
        val adaptadorSpinner = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            Categoria.datos
        )
        spn1.adapter = adaptadorSpinner

        spn1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                val categoria = adaptadorSpinner.getItem(pos)!!.valor
                val url = "https://newsapi.org/v2/top-headlines?country=us&category=$categoria&apiKey=$API_KEY"
                cargarNoticias(url)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun cargarNoticias(url: String) {
        val queue = Volley.newRequestQueue(this)

        val request = object : StringRequest(Request.Method.GET, url,
            { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val articlesArray = jsonObject.getJSONArray("articles").toString()

                    // Parseo con GSON
                    val tipo = object : TypeToken<List<Articles>>() {}.type
                    val listaArticulos: List<Articles> = Gson().fromJson(articlesArray, tipo)

                    // Actualizamos el RecyclerView
                    adapterNoticias.actualizarLista(listaArticulos)

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error procesando datos", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error de conexión: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }

        queue.add(request)
    }

    private fun abrirDetalle(articulo: Articles) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("EXTRA_ARTICLE", articulo)
        startActivity(intent)
    }
}