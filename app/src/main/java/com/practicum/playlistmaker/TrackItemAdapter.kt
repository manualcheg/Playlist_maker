package com.practicum.playlistmaker

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.playerActivity.presentation.PlayerActivity

var isClickAllowed = true

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
        val sharedPrefs: SharedPreferences =
            holder.itemView.context.getSharedPreferences(SHARED_PREFS_SELECTED_TRACKS, MODE_PRIVATE)

        holder.itemView.setOnClickListener {
            if (isMakedClickable()) {
                SearchHistory(sharedPrefs).save(track)
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
