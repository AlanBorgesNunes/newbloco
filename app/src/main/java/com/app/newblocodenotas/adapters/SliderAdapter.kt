package com.app.newblocodenotas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.app.newblocodenotas.R
import com.app.newblocodenotas.models.SliderItem
import com.makeramen.roundedimageview.RoundedImageView

class SliderAdapter internal constructor(
    sliderItems: MutableList<SliderItem>,
    viewPager: ViewPager2
): RecyclerView.Adapter<SliderAdapter.SliderViewHolder>(){

    private val sliderItems:  List<SliderItem> = sliderItems


    class SliderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val imageView: RoundedImageView = itemView.findViewById(R.id.imageSlide)

        fun image (sliderItem: SliderItem){
            imageView.setImageResource(sliderItem.image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slide_item_container,
                parent,
                false
            )

        )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {

        holder.image(sliderItems[position])

    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }


}