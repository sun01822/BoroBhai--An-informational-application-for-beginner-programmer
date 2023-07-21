package com.sun.borobhai.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sun.borobhai.R

class HorizontalListAdapter(
    private val context: Context,
    private val data: List<String>,
    private val links: List<String>,
    private val image : List<Int>
) :
    RecyclerView.Adapter<HorizontalListAdapter.ViewHolder>() {

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
        val item = data[position]
        holder.textView.text = item
        val imageIcon = image[0]
        holder.imageView.setImageResource(imageIcon)

        holder.itemView.setOnClickListener {
            val link = links.getOrNull(position)
            if (!link.isNullOrEmpty()) {
                openLinkInBrowser(link)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun openLinkInBrowser(link: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(browserIntent)
    }
}
