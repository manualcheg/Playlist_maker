package com.practicum.playlistmaker.search.presentation.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.entities.Track
import java.text.SimpleDateFormat
import java.util.*

class SearchViewHolder(private val track_item: View): RecyclerView.ViewHolder(track_item) {
    private val trackName = track_item.findViewById<TextView>(R.id.track_name)
    private val artistName = track_item.findViewById<TextView>(R.id.artist_name)
    private val trackTime = track_item.findViewById<TextView>(R.id.track_time)
    private val imageCover = track_item.findViewById<ImageView>(R.id.image_cover)

    fun bind (model: Track){
        trackName.text = model.trackName?.trimEnd()
        artistName.text = model.artistName?.trimEnd()
//        костыль из-за нулевого времени при поиске по букве g:
        trackTime.text = model.trackTime.let {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(it?.toInt()) }

        Glide.with(imageCover)
            .load(model.artworkUrl60)
            .placeholder(R.drawable.placeholder_no_cover)
            .centerCrop()
            .transform(RoundedCorners(track_item.resources.getDimensionPixelSize(R.dimen.dp4)))
            .into(imageCover)
    }
}