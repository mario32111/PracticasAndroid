package com.example.ejemploapirest

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var spn1: Spinner
    //private lateinit var txt1: TextView
    private lateinit var img1: ImageView
    private lateinit var txt1: TextView
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    lateinit var adaptador1: ArrayAdapter<Categoria>
    var listaArticulos = mutableListOf<Articles>()
    var cont: Int = 0
    lateinit var requestQueue: RequestQueue //este es como el sql connection
    lateinit var stringRequest: StringRequest //este es como el sqlCommand
    lateinit var stringRequestOK: StringRequest //este es como el sqlCommand

    lateinit var objetoJSON: JSONObject
    lateinit var articulosJSON: JSONArray
    lateinit var url: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        spn1= findViewById(R.id.spn1)
        adaptador1 = ArrayAdapter<Categoria>(
            this@MainActivity,
            android.R.layout.simple_spinner_dropdown_item,
            Categoria.datos)

        spn1.adapter= adaptador1
        //txt1= findViewById(R.id.txt1)
        img1= findViewById(R.id.img1)
        txt1= findViewById(R.id.textView1)
        btn1= findViewById(R.id.button1)
        btn2= findViewById(R.id.button2)


        btn1.setOnClickListener {
            if (cont >= 0 && cont < listaArticulos.size) {
                Picasso.get().load(listaArticulos.get(cont).urlToImage).into(img1)
                txt1.text = listaArticulos.get(cont).title
                cont--
            }
        }

        btn2.setOnClickListener {
            if (cont >= 0 && cont < listaArticulos.size) {
                Picasso.get().load(listaArticulos.get(cont).urlToImage).into(img1)
                txt1.text = listaArticulos.get(cont).title
                cont++
            }
        }



        spn1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //Toast.makeText(this@MainActivity, adaptador1.getItem(position)!!.valor, Toast.LENGTH_SHORT)
                //url="http://192.168.15.47/android/ejemploJSON.php"
                url = "https://newsapi.org/v2/top-headlines?country=us&category=${adaptador1.getItem(position)!!.valor}&apiKey=dfa30107b91b4b35a578cda6e89b37f4"
                peticion(url)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun peticion(dirUrl: String){
        requestQueue= Volley.newRequestQueue(this@MainActivity)

        stringRequestOK = object : StringRequest(Request.Method.GET, dirUrl,
            Response.Listener {
                objetoJSON = JSONObject(it)

                //txt1.text = objetoJSON.getJSONArray("articles").toString()


                val tipo = object : TypeToken<List<Articles>>() {}.type
                listaArticulos = Gson().fromJson(objetoJSON.getJSONArray("articles").toString(), tipo)
                Picasso.get().load(listaArticulos.get(0).urlToImage).into(img1)
                /*                        txt1.text = ""
                                        for (i in 0 until listaArticulos.size){
                                            txt1.append("${listaArticulos.get(i).author}, ${listaArticulos.get(i).title} \\n")
                                        }*/

            },
            Response.ErrorListener {

            }){
            override fun getParams(): Map<String?, String?>? {
                return super.getParams()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers: HashMap<String, String> = HashMap()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }


        }

        stringRequest= object : StringRequest(Method.GET, url,
            Response.Listener{
                //objetoJSON = JSONObject(it)
                //articulosJSON = objetoJSON.getJSONArray("articles")



                //listaArticulos = mutableListOf<Articles>()

                /*                        for (i in 0 until articulosJSON.length()) {
                                            //pasarlo directamente TextView desde el objeto JSON

                                            //txt1.append("${articulosJSON.getJSONObject(i).getString("author")}${articulosJSON.getJSONObject(i).getString("title")} /n")
                                            val autor= articulosJSON.getJSONObject(i).getString("author")
                                            val titulo= articulosJSON.getJSONObject(i).getString("title")

                                            listaArticulos.add(Articles(autor, titulo))

                                            txt1.append("${listaArticulos.get(i).author}, ${listaArticulos.get(i).title} /n")
                                        }*/
                //txt1.text = objetoJSON.getJSONArray("articles").toString()


                //pasarla diretamenta a la lista mutable sin usar un for
                /*                        val tipo = object : TypeToken<List<Articles>>() {}.type
                                        listaArticulos = Gson().fromJson(articulosJSON.toString(), tipo)*/

            },
            Response.ErrorListener {

            }){
            override fun getParams(): Map<String?, String?>? {
                return super.getParams()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers: HashMap<String, String> = HashMap()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }

        requestQueue.add(stringRequestOK)
    }
}