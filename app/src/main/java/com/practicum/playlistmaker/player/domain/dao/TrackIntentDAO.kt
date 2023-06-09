package com.practicum.playlistmaker.player.domain.dao

import com.practicum.playlistmaker.search.domain.entities.Track

interface TrackIntentDAO {
    fun getTrack(): Track
}