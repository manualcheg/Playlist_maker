package com.practicum.playlistmaker

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class TrackItemAdapter(
    private val trackList: MutableList<Track>
) : RecyclerView.Adapter<ViewHolderSearch>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSearch {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return ViewHolderSearch(view)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: ViewHolderSearch, position: Int) {
        holder.bind(trackList[position])
        val sharedPrefs: SharedPreferences = holder.itemView.context.getSharedPreferences(SHARED_PREFS_SELECTED_TRACKS, MODE_PRIVATE)

        holder.itemView.setOnClickListener {
            SearchHistory(sharedPrefs).save(trackList[position])
        }
    }

    fun setTracks(newTracks: List<Track>) {
        trackList.clear()
        trackList.addAll(newTracks)
        notifyDataSetChanged()
    }
}
