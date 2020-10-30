package com.example.naturzaragoza.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.naturzaragoza.model.Family
import com.example.naturzaragoza.R
import kotlinx.android.synthetic.main.card_family_item.view.*

//Recibe en el constructor los elementos de la lista y la acción al pulsar.
class FamilyListAdapter(val items: List<Family>, val itemClick: (Family) -> Unit)
    : RecyclerView.Adapter<FamilyListAdapter.ViewHolder>() {
    // Implementación de RecyclerView.ViewHolder que se usará en la función onCreateViewHolder
    class ViewHolder(val cardView: CardView, val itemClick: (Family) -> Unit) : RecyclerView.ViewHolder(cardView) {
        // Hace cambios en la vista creada según el elemento pasado.
        fun bindFamily(family: Family) {
            // Dentro del bloque with, this es family, itemView es la vista card_family_item cargada más adelante
            with(family) {
                itemView.txFamily.text = family.family
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }

    // Crea nuevas vistas para cada elemento de la lista. Lo invoca el LayoutManager.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Crea una nueva vista.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_family_item, parent, false) as CardView
        // Retorna la vista bajo un objeto que herede de RecyclerView.ViewHolder
        return ViewHolder(view, itemClick)
    }

    // Devuelve el número de elementos.
    override fun getItemCount() = items.size

    // Llama a la función para rellenar la vista pasando el elemento de la posición dada.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindFamily(items[position])
    }
}