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
    private val tracksCount: TextView = itemView.findViewById(R.id.playlistGridItemTextViewCountOfTracks)
    private var countOfTracks = 1

    fun bind(playlist:Playlist){
        playlistName.text = playlist.playlistName
        countOfTracks = playlist.countOfTracks
        tracksCount.text = buildString {
        append(playlist.countOfTracks.toString())
        append(" ")
        append(defineWord())
    }

        Glide.with(image)
            .load(playlist.playlistCover)
            .placeholder(R.drawable.placeholder_album_cover)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.dp8)))
            .into(image)
    }

    fun defineWord(): String {
        val predPosCifra = countOfTracks % 100 / 10
        if (predPosCifra == 1) {
            return "треков"
        }
        return when (countOfTracks % 10) {
            0, in 5..9 -> {"треков"}
            1 -> {"трек"}
            else -> {"трека"}
        }
    }
}