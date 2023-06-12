package com.practicum.playlistmaker.search.ui

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.data.storage.SearchStorageImpl
import com.practicum.playlistmaker.utils.Constants.Companion.SHARED_PREFS_SELECTED_TRACKS

var isClickAllowed = true

class SearchAdapter(
    private val trackList: MutableList<Track>
) : RecyclerView.Adapter<SearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val track = trackList[position]
        holder.bind(track)
        val sharedPrefs: SharedPreferences =
            holder.itemView.context.getSharedPreferences(SHARED_PREFS_SELECTED_TRACKS, MODE_PRIVATE)

        holder.itemView.setOnClickListener {
            if (isMakedClickable()) {
//                SearchHistory(sharedPrefs).save(track) заменил на:
                SearchStorageImpl(sharedPrefs).saveData(track)
                val intent = Intent(holder.itemView.context, PlayerActivity::class.java)
                intent.putExtra("track", Gson().toJson(track))
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    fun setTracks(newTracks: List<Track>) {
        trackList.clear()
        trackList.addAll(newTracks)
        notifyDataSetChanged()
    }

    private fun isMakedClickable(): Boolean {
        val currentState = isClickAllowed
        val handler = android.os.Handler(Looper.getMainLooper())
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, 1000L)
        }
        return currentState
    }
}
