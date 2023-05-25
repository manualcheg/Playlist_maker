package com.practicum.playlistmaker.player.domain.usecases

import com.practicum.playlistmaker.player.domain.dao.TrackIntentDAO
import com.practicum.playlistmaker.player.domain.entities.Track

class GetTrackUseCase(private val trackIntentDAOImpl: TrackIntentDAO) {
    fun execute(): Track {
        return trackIntentDAOImpl.getTrack()
    }
}