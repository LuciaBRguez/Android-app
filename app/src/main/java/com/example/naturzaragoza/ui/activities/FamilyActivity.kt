package com.example.naturzaragoza.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.naturzaragoza.App
import com.example.naturzaragoza.model.Family
import com.example.naturzaragoza.ui.adapters.FamilyListAdapter
import com.example.naturzaragoza.R
import com.example.naturzaragoza.data.DataBase.AppDatabase
import com.example.naturzaragoza.ui.Settings
import kotlinx.android.synthetic.main.activity_family.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class FamilyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family)
        setSupportActionBar(main_toolbar)
        // Mostrar el botón atrás
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initialize()
    }
    private fun initialize() {
        familyList.layoutManager = LinearLayoutManager(this)

        doAsync {
            if (App.database == null) {
                App.database = Room.databaseBuilder(App.instance, AppDatabase::class.java, "base_datos").build()
            }
            val listaFamilias = App.database?.familyDao()?.getAll()!!
            uiThread {
                familyList.adapter = FamilyListAdapter(listaFamilias) {
                    Settings.instance(App.instance).family = it.family
                    Toast.makeText(App.instance, it.family, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}