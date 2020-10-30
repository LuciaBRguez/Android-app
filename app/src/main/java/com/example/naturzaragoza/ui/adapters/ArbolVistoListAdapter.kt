package com.example.naturzaragoza.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.naturzaragoza.App
import com.example.naturzaragoza.model.ArbolVisto
import com.example.naturzaragoza.R
import com.example.naturzaragoza.data.DataBase.AppDatabase
import com.example.naturzaragoza.data.Metodos.ArbolVistoMetodos.DeleteArbolVistoAsync
import com.example.naturzaragoza.ui.activities.CommentActivity
import kotlinx.android.synthetic.main.card_arbolvisto_item.view.*
import org.jetbrains.anko.doAsync


//Recibe en el constructor los elementos de la lista y la acción al pulsar.
class ArbolVistoListAdapter(context: Context, val itemsPasado: MutableList<ArbolVisto>, val itemClick: (ArbolVisto) -> Unit)
    : RecyclerView.Adapter<ArbolVistoListAdapter.ViewHolder>() {

    private val context: Context
    private val items: MutableList<ArbolVisto> = mutableListOf()
    init {
        this.context = context
        this.items.addAll(itemsPasado)
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyDataSetChanged()
    }

    // Implementación de RecyclerView.ViewHolder que se usará en la función onCreateViewHolder
    class ViewHolder(val cardView: CardView, val itemClick: (ArbolVisto) -> Unit, val context: Context, arbolVistoListAdapter: ArbolVistoListAdapter)  : RecyclerView.ViewHolder(cardView) {

        // Constructor
        var arbolVistoListAdapter: ArbolVistoListAdapter
        init {
            this.arbolVistoListAdapter = arbolVistoListAdapter
        }

        // Hace cambios en la vista creada según el elemento pasado.
        fun bindArbolVisto(arbolVisto: ArbolVisto) {
            // Dentro del bloque with, this es arbol, itemView es la vista card_arbol_item cargada más adelante
            with(arbolVisto) {
                itemView.txCommonName.text = commonName
                itemView.txComentario.text = comentario
                itemView.setOnClickListener {
                    var popup: PopupMenu?
                    popup= PopupMenu(itemView.context, itemView)
                    popup.inflate(R.menu.menu)
                    popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

                        when (item!!.itemId) {
                            R.id.add_coment ->{
                                //Toast.makeText(itemView.context, "add coment", Toast.LENGTH_LONG).show()
                                val intent = Intent(context, CommentActivity::class.java).apply {
                                    putExtra("commonName", commonName)
                                    putExtra("comentario", comentario)
                                }
                                // start your next activity
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent)
                            }
                            R.id.delete ->{
                                doAsync {
                                    if (App.database == null) {
                                        App.database = Room.databaseBuilder(App.instance, AppDatabase::class.java, "base_datos").build()
                                    }
                                    var existeArbol = App.database?.arbolVistoDao()?.getExiste(commonName)!!
                                    if(existeArbol.size != 0) {
                                        var arbol = existeArbol.get(0)
                                        DeleteArbolVistoAsync(
                                            App.database!!.arbolVistoDao(),
                                            "DELETE_ARBOL_VISTO"
                                        ).execute(arbol)
                                    }
                                }
                                arbolVistoListAdapter.removeAt(getAdapterPosition())
                                itemClick(this)
                            }
                        }

                        true
                    })

                    popup.show()
                }

            }
        }
    }

    // Crea nuevas vistas para cada elemento de la lista. Lo invoca el LayoutManager.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Crea una nueva vista.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_arbolvisto_item, parent, false) as CardView
        // Retorna la vista bajo un objeto que herede de RecyclerView.ViewHolder
        return ViewHolder(view, itemClick, context, this)
    }

    // Devuelve el número de elementos.
    override fun getItemCount() = items.size

    // Llama a la función para rellenar la vista pasando el elemento de la posición dada.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindArbolVisto(items[position])
    }
}

