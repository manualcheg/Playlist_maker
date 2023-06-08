package com.practicum.playlistmaker.playerActivity.domain.dao

import com.practicum.playlistmaker.playerActivity.domain.entities.Track

interface TrackIntentDAO {
    fun getTrack():Track
}