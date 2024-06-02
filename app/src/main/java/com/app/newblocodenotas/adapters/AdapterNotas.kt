package com.app.newblocodenotas.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.app.newblocodenotas.R
import com.app.newblocodenotas.databinding.ItemLayoutBinding
import com.app.newblocodenotas.models.Anotation
import com.app.newblocodenotas.viewModels.viewModelAnotation

class AdapterNotas(private var list: ArrayList<Anotation>,
                   private val context: Context,
                   val clickDelete: ClickDelete,
                   val clickEdit: ClickEdit):
RecyclerView.Adapter<AdapterNotas.ViewHolder>(){

    interface ClickEdit{
        fun clickEdite(anotation: Anotation)
    }

    interface ClickDelete{
        fun clickDelete(anotation: Anotation)
    }
    inner class ViewHolder(private val binding: ItemLayoutBinding):
        RecyclerView.ViewHolder(binding.root){
            fun bind(anotation: Anotation){
                binding.title.text = anotation.title
                binding.description.text = anotation.anotation

                binding.tresPontosItem.setOnClickListener {
                    menuItemShow(binding.tresPontosItem,anotation)
                }

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

    private fun menuItemShow(imageView: ImageView, anotation: Anotation){
        val menu = PopupMenu(context,imageView)
        menu.inflate(R.menu.menu_item)
        menu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.editar_note ->{
                    clickEdit.clickEdite(anotation)
                    true
                }

                R.id.delete_note ->{
                    clickDelete.clickDelete(anotation)
                    true
                }

                else-> false
            }
        }
        menu.show()
    }
}