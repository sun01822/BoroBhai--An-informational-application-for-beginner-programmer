package com.sun.borobhai.adapter

import com.sun.borobhai.activities.DetailsScreen
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sun.borobhai.R
class HorizontalListAdapter(
    private val context: Context,
    private val data: List<String>,
    private val links: List<String>,
    private val checker : Int,
    private val youtubeLink : String
) :
    RecyclerView.Adapter<HorizontalListAdapter.ViewHolder>() {
    private lateinit var imageList : List<Int>
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tvItemName)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horizontal_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        imageList = listOf(R.drawable.book, R.drawable.coding, R.drawable.youtuber, R.drawable.compiler)
        val item = data[position]
        holder.textView.text = item
        val imageIcon = imageList[checker]
        Glide.with(context).load(imageIcon).into(holder.imageView)

        val link = links.getOrNull(position)

        holder.itemView.setOnClickListener {
            if (!link.isNullOrEmpty()) {
                val intent = Intent(context, DetailsScreen::class.java)
                intent.putExtra(DetailsScreen.EXTRA_LINK, link)
                intent.putExtra("youtubeLink", youtubeLink)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
