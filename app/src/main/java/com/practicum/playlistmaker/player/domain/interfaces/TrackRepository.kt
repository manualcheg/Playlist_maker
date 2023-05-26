package com.practicum.playlistmaker.player.domain.interfaces

import com.practicum.playlistmaker.player.domain.entities.Track

interface TrackRepository {
    fun getTrack(): Track

    fun preparePlayer(mediaPlayerPreparator:MediaPlayerPrepare)

    fun startPlayer():Int

    fun pausePlayer():Int

//    fun setOnCompletionListener(onCompletion: () -> Unit)

}