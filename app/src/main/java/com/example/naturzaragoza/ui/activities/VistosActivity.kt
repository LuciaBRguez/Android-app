package com.example.naturzaragoza.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.naturzaragoza.App
import com.example.naturzaragoza.model.ArbolVisto
import com.example.naturzaragoza.ui.adapters.ArbolVistoListAdapter
import com.example.naturzaragoza.R
import com.example.naturzaragoza.data.DataBase.AppDatabase
import com.example.naturzaragoza.ui.Settings
import com.example.naturzaragoza.ui.adapters.FamilyListAdapter
import kotlinx.android.synthetic.main.activity_vistos.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class VistosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vistos)
        setSupportActionBar(main_toolbar)
        // Mostrar el botón atrás
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initialize()
    }
    private fun initialize() {
        arbolvistosList.layoutManager = LinearLayoutManager(this)

        doAsync {
            if (App.database == null) {
                App.database = Room.databaseBuilder(App.instance, AppDatabase::class.java, "base_datos").build()
            }
            val listaArbolesVistos = App.database?.arbolVistoDao()?.getAll()!!
            uiThread {
                arbolvistosList.adapter = ArbolVistoListAdapter(App.instance, listaArbolesVistos) {

                }
            }
        }
    }

}
