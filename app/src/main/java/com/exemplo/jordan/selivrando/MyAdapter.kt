package com.exemplo.jordan.selivrando

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.exemplo.jordan.selivrando.models.Livro
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.itemlist.view.*

/**
 * Created by eqs on 20/03/2018.
 */
class MyAdapter (val context: ValueEventListener, val livros:ArrayList<Livro>, val clickListener:(Livro)->Unit):
        RecyclerView.Adapter<MyAdapter.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.itemlist, parent, false)
        var vh = ViewHolder(v)
        return vh
    }

    override fun onBindViewHolder(holder:ViewHolder , position: Int) {
        holder.itemView.tvNome.text = livros[position].titulo
        holder.itemView.tvuUerPublic.text = livros[position].proprietario

        holder.itemView.setOnClickListener{clickListener(livros[position])}
    }

    override fun getItemCount(): Int {
        return livros.size
    }

}