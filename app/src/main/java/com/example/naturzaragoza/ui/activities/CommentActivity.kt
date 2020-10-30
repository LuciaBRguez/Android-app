package com.example.naturzaragoza.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.naturzaragoza.App
import com.example.naturzaragoza.R
import com.example.naturzaragoza.data.DataBase.AppDatabase
import com.example.naturzaragoza.data.Metodos.ArbolVistoMetodos.UpdateArbolVistoAsync
import com.example.naturzaragoza.model.ArbolVisto
import kotlinx.android.synthetic.main.activity_comment.*
import org.jetbrains.anko.doAsync

class CommentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        setSupportActionBar(main_toolbar)
        // Mostrar el botón atrás
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val commonName = intent.getStringExtra("commonName")
        val comentario = intent.getStringExtra("comentario")
        txCommonName.text = commonName
        intxComment.setText(comentario)

        btSubmitComment.setOnClickListener {
            doAsync {
                if (App.database == null) {
                    App.database = Room.databaseBuilder(App.instance, AppDatabase::class.java, "base_datos").build()
                }
                var existeArbol = App.database?.arbolVistoDao()?.getExiste(commonName)!!
                if(existeArbol.size != 0) {
                    var arbol = existeArbol.get(0)
                    var arbolActualizado = ArbolVisto(arbol.id, arbol.commonName, intxComment.text.toString())
                    UpdateArbolVistoAsync(
                        App.database!!.arbolVistoDao(),
                        "UPDATE_ARBOL_VISTO"
                    ).execute(arbolActualizado)
                }
            }
            Toast.makeText(App.instance, "Comentario actualizado", Toast.LENGTH_LONG).show()
        }

    }


}
