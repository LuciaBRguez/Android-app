package com.example.naturzaragoza.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.naturzaragoza.model.Arbol
import com.example.naturzaragoza.R
import kotlinx.android.synthetic.main.card_arbol_item.view.*

//Recibe en el constructor los elementos de la lista y la acción al pulsar.
class ArbolListAdapter(val items: List<Arbol>, val itemClick: (Arbol) -> Unit)
    : RecyclerView.Adapter<ArbolListAdapter.ViewHolder>() {

    // Implementación de RecyclerView.ViewHolder que se usará en la función onCreateViewHolder
    class ViewHolder(val cardView: CardView, val itemClick: (Arbol) -> Unit) : RecyclerView.ViewHolder(cardView) {

        // Hace cambios en la vista creada según el elemento pasado.
        fun bindArbol(arbol: Arbol) {
            // Dentro del bloque with, this es arbol, itemView es la vista card_arbol_item cargada más adelante
            with(arbol) {
                itemView.txCommonName.text = commonName
                itemView.txFamily.text = family
                itemView.txType.text = type
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }

    // Crea nuevas vistas para cada elemento de la lista. Lo invoca el LayoutManager.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Crea una nueva vista.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_arbol_item, parent, false) as CardView
        // Retorna la vista bajo un objeto que herede de RecyclerView.ViewHolder
        return ViewHolder(view, itemClick)
    }

    // Devuelve el número de elementos.
    override fun getItemCount() = items.size

    // Llama a la función para rellenar la vista pasando el elemento de la posición dada.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindArbol(items[position])
    }
}