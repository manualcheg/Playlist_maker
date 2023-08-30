package com.practicum.playlistmaker.mediateka.playlists.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist

class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val image: ImageView = itemView.findViewById(R.id.playlistGridItemImageView)
    private val playlistName: TextView = itemView.findViewById(R.id.playlistGridItemTextViewPlaylistName)
    private val countOfTracks: TextView = itemView.findViewById(R.id.playlistGridItemTextViewCountOfTracks)

    fun bind(playlist:Playlist){
        playlistName.text = playlist.playlistName
        countOfTracks.text = playlist.countOfTracks.toString()

        // TODO: убрать Glide?
        Glide.with(image)
            .load(playlist.playlistCover)
            .placeholder(R.drawable.placeholder_album_cover)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.dp8)))
            .into(image)
    }
}