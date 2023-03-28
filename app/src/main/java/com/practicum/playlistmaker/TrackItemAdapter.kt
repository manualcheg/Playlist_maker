package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class TrackItemAdapter(
    private val trackList: ArrayList<Track>
): RecyclerView.Adapter<ViewHolderSearch>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSearch {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item,parent,false)
        return ViewHolderSearch(view)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: ViewHolderSearch, position: Int) {
        holder.bind(trackList[position])
    }

    //     функция setTracks() вместо такого кода в SearchActivity:
    //     trackList.clear()
    //     trackList.addAll(response.body()?.results!!)
    //     trackListAdapter.notifyDataSetChanged()

    fun setTracks(trackList: ArrayList<Track>, newTracks: List<Track>) {
        trackList.clear()
        trackList.addAll(newTracks)
        notifyDataSetChanged()
    }
}
