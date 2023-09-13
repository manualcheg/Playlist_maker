package com.practicum.playlistmaker.mediateka.playlists.presentation

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.mediateka.favourites.db.TracksDBFavourites
import com.practicum.playlistmaker.search.data.storage.SearchStorageImpl
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.search.presentation.ui.SearchViewHolder
import com.practicum.playlistmaker.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlaylistWorkAdapter(
    private val trackList: List<Track>,
    private val longClickListener: LongClickListener
) :
    RecyclerView.Adapter<SearchViewHolder>(),
    KoinComponent {
    private lateinit var view: View
    private val tracksDBFavourites: TracksDBFavourites by inject()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(trackList[position])

        val sharedPrefs: SharedPreferences =
            holder.itemView.context.getSharedPreferences(
                Constants.PLAYLISTMAKER_SHAREDPREFS,
                Context.MODE_PRIVATE
            )

        holder.itemView.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                SearchStorageImpl(sharedPrefs, tracksDBFavourites).saveData(trackList[position])
            }
            holder.itemView.findNavController().navigate(R.id.moveToPlayerFragment)
        }

        holder.itemView.setOnLongClickListener { _ ->
            longClickListener.trackInPlaylistClick(trackList[position].trackId)
            true
        }
    }

    interface LongClickListener {
        fun trackInPlaylistClick(currentTrackId: String)
    }
}