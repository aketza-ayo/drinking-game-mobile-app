package com.devapp.drinkinggame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_item.view.*

class CardAdapter(private val exampleList: List<CardItem>) : RecyclerView.Adapter<CardAdapter.ExampleViewHolder>(){

    class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.image_view
//        val textView1: TextView = itemView.text_view_1
//        val textView2: TextView = itemView.text_view_2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent,false)

        return ExampleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem = exampleList[position]
        holder.imageView.setImageResource(currentItem.imageResource)
//        holder.textView1.text = currentItem.text1
//        holder.textView2.text = currentItem.text2

    }

    public fun getItemAt(position: Int): CardItem{
        return exampleList.get(position)
    }

    override fun getItemCount() = exampleList.size

}
