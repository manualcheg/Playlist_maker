package com.practicum.playlistmaker.mediateka.playlists.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.utils.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistAdapter(
    private val listOfPlaylists: MutableList<Playlist>,
    private val playlistWorkPlaylistClickListener: PlaylistWorkPlaylistClickListener
) :
    RecyclerView.Adapter<PlaylistViewHolder>() {
    private var isClickAllowed = true
    private lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_grid_item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfPlaylists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = listOfPlaylists[position]
        holder.bind(playlist)

        holder.itemView.setOnClickListener {
            if (isMakedClickable()) {
                playlistWorkPlaylistClickListener.playlistClick(listOfPlaylists[position])
            }
        }
    }

    interface PlaylistWorkPlaylistClickListener {
        fun playlistClick(playlist: Playlist)
    }

    private fun isMakedClickable(): Boolean {
        val currentState = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            view.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                delay(Constants.CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return currentState
    }
}