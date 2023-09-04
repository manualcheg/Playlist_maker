package com.practicum.playlistmaker.player.presentation

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

class PlayerBottomSheetRecycleViewAdapter(
    private val listOfPlaylists: List<Playlist>,
    private val playlistClickListener: PlaylistClickListener
) :
    RecyclerView.Adapter<PlayerBottomSheetViewHolder>() {
    private var isClickAllowed = true
    private lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerBottomSheetViewHolder {
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist_in_bottom_sheet_add_to_playlist, parent, false)
        return PlayerBottomSheetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfPlaylists.size
    }

    override fun onBindViewHolder(holder: PlayerBottomSheetViewHolder, position: Int) {
        val playlist = listOfPlaylists[position]
        holder.bind(playlist)


        holder.itemView.setOnClickListener {
            if (isMakedClickable()) {
                playlistClickListener.playlistClick(playlist)
            }
        }
    }

    interface PlaylistClickListener {
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