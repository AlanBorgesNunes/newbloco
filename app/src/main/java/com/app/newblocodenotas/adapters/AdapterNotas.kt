package com.app.newblocodenotas.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.newblocodenotas.R
import com.app.newblocodenotas.databinding.ItemLayoutBinding
import com.app.newblocodenotas.models.Anotation

class AdapterNotas(private var list: ArrayList<Anotation>):
RecyclerView.Adapter<AdapterNotas.ViewHolder>(){

    inner class ViewHolder(private val binding: ItemLayoutBinding):
        RecyclerView.ViewHolder(binding.root){
            fun bind(anotation: Anotation){
                binding.title.text = anotation.title
                binding.description.text = anotation.anotation

                if (anotation.privateOrPublic == "Public"){
                    binding.imgItem.setImageResource(R.drawable.bg)
                }else{
                    binding.imgItem.setImageResource(R.drawable.pv)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val currentNote = list[position]
        holder.bind(currentNote)
    }
}