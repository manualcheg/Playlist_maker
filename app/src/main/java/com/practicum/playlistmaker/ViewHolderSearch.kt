package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class ViewHolderSearch(track_item: View): RecyclerView.ViewHolder(track_item) {
    private val trackName = track_item.findViewById<TextView>(R.id.track_name)
    private val artistName = track_item.findViewById<TextView>(R.id.artist_name)
    private val trackTime = track_item.findViewById<TextView>(R.id.track_time)
    private val imageCover = track_item.findViewById<ImageView>(R.id.image_cover)

    fun bind (model: Track){
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
        Glide.with(imageCover)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.x_cross)
            .centerCrop()
            .transform(RoundedCorners(5))
            .into(imageCover)
    }
}