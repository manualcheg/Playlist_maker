package com.practicum.playlistmaker.search.presentation.ui

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.mediateka.favourites.db.TracksDBFavourites
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.search.data.storage.SearchStorageImpl
import com.practicum.playlistmaker.utils.Constants.Companion.PLAYLISTMAKER_SHAREDPREFS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchAdapter(
    private val trackList: MutableList<Track>,
) : RecyclerView.Adapter<SearchViewHolder>(), KoinComponent {

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
        val track = trackList[position]
        holder.bind(track)
        val sharedPrefs: SharedPreferences =
            holder.itemView.context.getSharedPreferences(PLAYLISTMAKER_SHAREDPREFS, MODE_PRIVATE)

        holder.itemView.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    SearchStorageImpl(sharedPrefs, tracksDBFavourites).saveData(track)
                }
            //вариант
                            /*val prevousFragment = holder.itemView.findNavController().currentBackStackEntry?.destination?.id
                            when (prevousFragment){
                                R.id.searchFragment -> {
                                    holder.itemView.findNavController().navigate(R.id.action_searchFragment_to_playerFragment)
                                }
                                else -> {
                                    holder.itemView.findNavController().navigate(R.id.action_mediatekaFragment_to_playerFragment)
                                }
                            }*/
                holder.itemView.findNavController().navigate(R.id.moveToPlayerFragment)
            }
    }

    fun setTracks(newTracks: List<Track>) {
        trackList.clear()
        trackList.addAll(newTracks)
        notifyItemRangeChanged(0, newTracks.lastIndex)
    }
}