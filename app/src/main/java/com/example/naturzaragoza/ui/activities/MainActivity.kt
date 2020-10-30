package com.example.naturzaragoza.ui.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.naturzaragoza.App
import com.example.naturzaragoza.model.Arbol
import com.example.naturzaragoza.ui.adapters.ArbolListAdapter
import com.example.naturzaragoza.R
import com.example.naturzaragoza.data.DataBase.AppDatabase
import com.example.naturzaragoza.data.Metodos.FamilyMetodos.InsertFamilyAsync
import com.example.naturzaragoza.model.CoordenadasConverter
import com.example.naturzaragoza.model.Family
import com.example.naturzaragoza.ui.Settings
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject

private const val TAG = "MainActivity"
private const val REQUEST_READ_EXTERNAL_STORAGE_PERMISSIONS = 1

class MainActivity : AppCompatActivity() {

    val LOG_TAG = "Etiqueta"
    val resultList = ArrayList<Arbol>()
    // Variables para el check del permiso READ_EXTERNAL_STORAGE
    var commonName: String = ""
    var photo: String = ""
    var shortDescription: String = ""
    var description: String = ""
    var lat: Double = 0.0
    var lon: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)
        initialize()
    }

    private fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.action_ver -> {
                startActivity<VistosActivity>()
                true
            }
            R.id.action_filtrar -> {
                startActivity<FamilyActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Permisos  READ_EXTERNAL_STORAGE
    private fun checkReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Se entra si el usuario no aceptó el permiso anteriormente.
            }
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE_PERMISSIONS)
        } else {
            performAction()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // El permiso se ha dado y se puede hacer la tarea oportuna.
                    performAction()
                }
            }
        }
    }

    fun performAction() {
        startActivity<DetailActivity>(
            DetailActivity.EXTRA_COMMON_NAME to commonName,
            DetailActivity.EXTRA_PHOTO to photo,
            DetailActivity.EXTRA_SHORT_DESCRIPTION to shortDescription,
            DetailActivity.EXTRA_DESCRIPTION to description,
            DetailActivity.EXTRA_LATITUD to lat,
            DetailActivity.EXTRA_LONGITUD to lon
        )
    }
    // Fin permisos  READ_EXTERNAL_STORAGE

    private fun initialize() {
        arbolList.layoutManager = LinearLayoutManager(this)

        doAsync {
            val family = Settings.instance(App.instance).family
            var url = "https://www.zaragoza.es/sede/servicio/especie.json"
            if (family != "TODAS") {
                url = "https://www.zaragoza.es/sede/servicio/especie.json?family="+family
            }

            val queue = Volley.newRequestQueue(App.instance)

            val jsonObjectRequest = JsonObjectRequest(url, null,
                Listener { response ->
                    Log.i(LOG_TAG, response.getString("result"))
                    val arbolesJson = JSONArray(response.getString("result"))

                    for (i in 0 until arbolesJson.length()) {
                        val loc = arbolesJson.get(i) as JSONObject
                        val geometry = JSONObject(loc.getString("geometry"))
                        val coordinates = JSONArray(geometry.getString("coordinates"))
                        val x = coordinates.get(0).toString().toDouble()
                        val y = coordinates.get(1).toString().toDouble()
                        val convertidas = CoordenadasConverter(x, y)
                        val latitud = convertidas.converterLatitud()
                        val longitud = convertidas.converterLongitud()

                        resultList.add(Arbol(
                            loc.getString("commonName"),
                            loc.getString("family"),
                            loc.getString("type"),
                            loc.getString("shortDescription"),
                            loc.getString("description"),
                            "https:"+loc.getString("photo"),
                            latitud,
                            longitud
                        ))
                    }
                    uiThread {
                        arbolList.adapter = ArbolListAdapter(resultList) {
                            commonName = it.commonName
                            photo = it.photo
                            shortDescription = it.shortDescription
                            description = it.description
                            lat = it.latitud
                            lon = it.longitud

                            checkReadExternalStoragePermission()
                        }
                    }
                },

                Response.ErrorListener { error ->
                    error.printStackTrace()
                })

            queue.add(jsonObjectRequest)
        }

        doAsync {
            if (App.database == null) {
                App.database = Room.databaseBuilder(App.instance, AppDatabase::class.java, "base_datos").build()

            }
            val items = listOf(
                Family(0 ,"TODAS"),
                Family(1 ,"ACERÁCEAS"),
                Family(2 ,"AGAVÁCEAS"),
                Family(3 ,"ANACARDIÁCEAS"),
                Family(4 ,"APOCINÁCEAS"),
                Family(5 ,"AQUIFOLIÁCEAS"),
                Family(6 ,"ARALIÁCEAS"),
                Family(7 ,"ARECÁCEAS"),
                Family(8 ,"BERBERIDÁCEAS"),
                Family(9 ,"BETULÁCEAS"),
                Family(10 ,"BIGNONIÁCEAS"),
                Family(11 ,"BUDLEIÁCEAS"),
                Family(12 ,"CAPRIFOLIÁCEAS"),
                Family(13 ,"CASUARINÁCEAS"),
                Family(14 ,"CELASTRÁCEAS"),
                Family(15 ,"CESALPINIÁCEAS"),
                Family(16 ,"CORNÁCEAS"),
                Family(17 ,"CUPRESÁCEAS"),
                Family(18 ,"EBENÁCEAS"),
                Family(19 ,"ELEAGNÁCEAS"),
                Family(20 ,"ERICÁCEAS"),
                Family(21 ,"ESCROFULARIÁCEAS"),
                Family(21 ,"ESTERCULIÁCEAS"),
                Family(23 ,"FABÁCEAS"),
                Family(24 ,"FAGÁCEAS"),
                Family(25 ,"GINGOÁCEAS"),
                Family(26 ,"HAMAMELIDÁCEAS"),
                Family(27 ,"HIDRANGEÁCEAS"),
                Family(28 ,"HIPOCASTANÁCEAS"),
                Family(29 ,"JUGLANDÁCEAS"),
                Family(30 ,"LAUREÁCEAS"),
                Family(31 ,"LITRÁCEAS"),
                Family(32 ,"MAGNOLIÁCEAS"),
                Family(33 ,"MALVÁCEAS"),
                Family(34 ,"MELIÁCEAS"),
                Family(35 ,"MIMOSÁCEAS"),
                Family(36 ,"MIRTÁCEAS"),
                Family(37 ,"MORÁCEAS"),
                Family(38 ,"NICTAGINÁCEAS"),
                Family(39 ,"OLEÁCEAS"),
                Family(40 ,"PASIFLORÁCEAS"),
                Family(41 ,"PINÁCEAS"),
                Family(42 ,"PITOSPORÁCEAS"),
                Family(43 ,"PLATANÁCEAS"),
                Family(44 ,"PUNICÁCEAS"),
                Family(45 ,"ROSÁCEAS"),
                Family(46 ,"RUTÁCEAS"),
                Family(47 ,"SALIÁCEAS"),
                Family(48 ,"SAPINDÁCEAS"),
                Family(49 ,"SIMARUBÁCEAS"),
                Family(50 ,"TAMARICÁCEAS"),
                Family(51 ,"TAXÁCEAS"),
                Family(52 ,"TAXODIÁCEAS"),
                Family(53 ,"TILIDÁCEAS"),
                Family(54 ,"ULMÁCEAS")
            )
            val listaFamilias = App.database?.familyDao()?.getAll()!!
            if (listaFamilias.size == 0) {
                for (i in 0 until items.size) {
                    InsertFamilyAsync(
                        App.database!!.familyDao(),
                        "INSERT_FAMILY"
                    ).execute(items.get(i))
                }
            }

        }
    }

}