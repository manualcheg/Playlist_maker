package com.practicum.playlistmaker.player.domain.dao

import com.practicum.playlistmaker.player.domain.entities.Track

interface TrackIntentDAO {
    fun getTrack():Track
}