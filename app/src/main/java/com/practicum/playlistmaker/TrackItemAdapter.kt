package com.practicum.playlistmaker

import android.content.Context.MODE_PRIVATE
import android.content.Intent
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
        val track = trackList[position]
        holder.bind(track)
        val sharedPrefs: SharedPreferences = holder.itemView.context.getSharedPreferences(SHARED_PREFS_SELECTED_TRACKS, MODE_PRIVATE)

        holder.itemView.setOnClickListener {
            SearchHistory(sharedPrefs).save(track)
            val intent = Intent(holder.itemView.context, PlayerActivity::class.java)
            intent.putExtra("artistName", track.artistName)
            intent.putExtra("trackName",track.trackName)
            intent.putExtra("trackTime",track.trackTime)
            intent.putExtra("artworkUrl500",track.getCoverArtwork())
            intent.putExtra("country",track.country)
            intent.putExtra("releaseDate",track.releaseDate)
            intent.putExtra("primaryGenreName",track.primaryGenreName)
            intent.putExtra("collectionName",track.collectionName)
            holder.itemView.context.startActivity(intent)
        }
    }

    fun setTracks(newTracks: List<Track>) {
        trackList.clear()
        trackList.addAll(newTracks)
        notifyDataSetChanged()
    }
}
