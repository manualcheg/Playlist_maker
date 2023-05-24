package com.practicum.playlistmaker.player.domain.dao

import com.practicum.playlistmaker.player.domain.models.Track

interface TrackIntentDAO {
    fun getTrack():Track
}