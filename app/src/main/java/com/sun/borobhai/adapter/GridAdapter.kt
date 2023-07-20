package com.sun.borobhai.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sun.borobhai.R
import com.sun.borobhai.data.GridItem

class GridAdapter(private val context : Context, private val gridItems: List<GridItem>) :
    RecyclerView.Adapter<GridAdapter.GridViewHolder>() {
    private var mOnClickListener: OnClickListener? = null
    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.languages_layout, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val currentItem = gridItems[position]
        holder.nameTextView.text = currentItem.name
        Glide.with(context).load(currentItem.imageUrl).into(holder.imageView)
        holder.itemView.setOnClickListener {
            if(mOnClickListener != null) {
                mOnClickListener!!.onClick(position, currentItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return gridItems.size
    }

    fun setOnclickListener(onClickListener: OnClickListener){
        this.mOnClickListener = onClickListener
    }


    interface OnClickListener : View.OnClickListener {
        fun onClick(position: Int, gridItem: GridItem)
    }
}
