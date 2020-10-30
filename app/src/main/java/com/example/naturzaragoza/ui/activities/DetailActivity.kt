package com.example.naturzaragoza.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.room.Room
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail_arbol.*
import kotlinx.android.synthetic.main.activity_main.*

// Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.naturzaragoza.App
import com.example.naturzaragoza.R
import com.example.naturzaragoza.data.DataBase.AppDatabase
import com.example.naturzaragoza.data.Metodos.ArbolVistoMetodos.InsertArbolVistoAsync
import com.example.naturzaragoza.model.ArbolVisto
import org.jetbrains.anko.doAsync

@GlideModule
class MyAppGlideModule : AppGlideModule()
// Fin Glide

private const val REQUEST_ACCESS_FINE_LOCATION_PERMISSIONS = 1

class DetailActivity : AppCompatActivity() {
    var cName: String = ""
    var lat: Double = 0.0
    var lon: Double = 0.0

    companion object {
        const val EXTRA_COMMON_NAME = "DetailActivity::commonName"
        const val EXTRA_PHOTO = "DetailActivity::photo"
        const val EXTRA_SHORT_DESCRIPTION = "DetailActivity::shortDescription"
        const val EXTRA_DESCRIPTION = "DetailActivity::description"
        const val EXTRA_LONGITUD = "DetailActivity::longitud"
        const val EXTRA_LATITUD = "DetailActivity::latitud"
    }

    // Permisos ACCESS_FINE_LOCATION
    private fun checkReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Se entra si el usuario no aceptó el permiso anteriormente.
            }
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_ACCESS_FINE_LOCATION_PERMISSIONS)
        } else {
            performAction()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ACCESS_FINE_LOCATION_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // El permiso se ha dado y se puede hacer la tarea oportuna.
                    performAction()
                }
            }
        }
    }

    fun performAction() {
        MapsActivity.startMapActivity(this@DetailActivity, cName, lat, lon)
    }
    // Fin permisos  READ_EXTERNAL_STORAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(main_toolbar)
        // Mostrar el botón atrás
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val commonName = intent.getStringExtra(EXTRA_COMMON_NAME)
        val textoHtml = HtmlCompat.fromHtml(intent.getStringExtra(EXTRA_DESCRIPTION), HtmlCompat.FROM_HTML_MODE_LEGACY)
        txCommonNameDetail.text = commonName
        Glide.with(this).load(intent.getStringExtra(EXTRA_PHOTO)).into(photo)
        txShortDescription.text = intent.getStringExtra(EXTRA_SHORT_DESCRIPTION)
        txDescription.text = textoHtml
        val longitud = intent.getDoubleExtra(EXTRA_LONGITUD, 0.0)
        val latitud = intent.getDoubleExtra(EXTRA_LATITUD, 0.0)

        bnVisto.setOnClickListener {
            doAsync {
                if (App.database == null) {
                    App.database = Room.databaseBuilder(App.instance, AppDatabase::class.java, "base_datos").build()
                }
                val arbol = ArbolVisto(commonName=commonName, comentario="")
                val existeArbol = App.database?.arbolVistoDao()?.getExiste(commonName)!!
                if(existeArbol.size == 0) {
                    InsertArbolVistoAsync(
                        App.database!!.arbolVistoDao(),
                        "INSERT_ARBOL_VISTO"
                    ).execute(arbol)
                }
            }
            Toast.makeText(App.instance, "Añadido a vistos", Toast.LENGTH_LONG).show()
        }

        mapa.setOnClickListener {
            cName = commonName
            lat = latitud
            lon = longitud
            checkReadExternalStoragePermission()
        }
    }
}