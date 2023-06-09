package com.practicum.playlistmaker.player.data.intent

import android.content.Intent
import com.google.gson.Gson
import com.practicum.playlistmaker.player.domain.dao.TrackIntentDAO
import com.practicum.playlistmaker.search.domain.entities.Track


class TrackIntentDAOImpl(val intent: Intent): TrackIntentDAO {
    override fun getTrack(): Track {
        return Gson().fromJson(intent.getStringExtra("track"), Track::class.java)
    }
}