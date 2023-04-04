package com.practicum.playlistmaker

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
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

        holder.itemView.setOnClickListener {
            val sharedPrefs: SharedPreferences = holder.itemView.context.getSharedPreferences(SHARED_PREFS_SELECTED_TRACKS, MODE_PRIVATE)
            SearchHistory(sharedPrefs).save(trackList[position])
            Toast.makeText(holder.itemView.context,"Добавлен трек:\n${trackList[position].trackName}",Toast.LENGTH_SHORT).show()
        }
    }

    //     функция setTracks() вместо такого кода в SearchActivity:
    //     trackList.clear()
    //     trackList.addAll(response.body()?.results!!)
    //     trackListAdapter.notifyDataSetChanged()

    fun setTracks(currentTrackList: ArrayList<Track>, newTracks: List<Track>) {
        currentTrackList.clear()
        currentTrackList.addAll(newTracks)
        notifyDataSetChanged()
    }
}
