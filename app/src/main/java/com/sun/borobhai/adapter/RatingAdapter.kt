package com.sun.borobhai.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sun.borobhai.R
import com.sun.borobhai.data.Rating
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class RatingsAdapter : RecyclerView.Adapter<RatingsAdapter.RatingViewHolder>() {
    private val ratingsList: MutableList<Rating> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_rating, parent, false)
        return RatingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        val rating = ratingsList[position]
        holder.bind(rating)
    }

    override fun getItemCount(): Int {
        return ratingsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addRating(rating: Rating) {
        ratingsList.add(rating)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        ratingsList.clear()
        notifyDataSetChanged()
    }

    class RatingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profileImage: CircleImageView = itemView.findViewById(R.id.ivProfileImage)
        private val nameTextView: TextView = itemView.findViewById(R.id.tvName)
        private val datetimeTextView: TextView = itemView.findViewById(R.id.tvDatetime)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val commentTextView: TextView = itemView.findViewById(R.id.tvComment)

        fun bind(rating: Rating) {
            // Set the data to the views
            ratingBar.rating = rating.ratingValue
            commentTextView.text = rating.comment

            // Fetch user data based on userId (UID)
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val uid = rating.userId
            val userRef = database.reference.child("users").child(uid!!)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("name").value.toString()
                    val imageUrl = snapshot.child("imageUrl").value.toString()

                    // Set user name to the nameTextView
                    nameTextView.text = name

                    // Set profile image (if available)
                    // Load the image using a library like Glide or Picasso
                    Glide.with(itemView.context).load(imageUrl).into(profileImage)

                    // Format timestamp to date-time string
                    datetimeTextView.text = getFormattedDateTime(rating.timestamp)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle any errors that may occur while fetching user data
                    nameTextView.text = "Error"
                    profileImage.setImageResource(R.drawable.logo)
                }
            })
        }

        private fun getFormattedDateTime(timestamp: Long): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }
}
