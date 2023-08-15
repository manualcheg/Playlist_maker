package com.practicum.playlistmaker.search.presentation.ui

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.mediateka.data.db.TracksDBFavourites
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.player.presentation.ui.PlayerActivity
import com.practicum.playlistmaker.search.data.storage.SearchStorageImpl
import com.practicum.playlistmaker.utils.Constants.Companion.CLICK_DEBOUNCE_DELAY_MILLIS
import com.practicum.playlistmaker.utils.Constants.Companion.PLAYLISTMAKER_SHAREDPREFS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchAdapter(
    private val trackList: MutableList<Track>,
) : RecyclerView.Adapter<SearchViewHolder>(), KoinComponent {

    var isClickAllowed = true
    lateinit var view: View
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
            if (isMakedClickable()) {
                CoroutineScope(Dispatchers.IO).launch {
                    SearchStorageImpl(sharedPrefs, tracksDBFavourites).saveData(track)
                }
                val intent = Intent(holder.itemView.context, PlayerActivity::class.java)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    fun setTracks(newTracks: List<Track>) {
        trackList.clear()
        trackList.addAll(newTracks)
        notifyItemRangeChanged(0, newTracks.lastIndex)
    }

    private fun isMakedClickable(): Boolean {
        val currentState = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            view.findViewTreeLifecycleOwner()?.lifecycleScope?.launch() {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return currentState
    }
}
