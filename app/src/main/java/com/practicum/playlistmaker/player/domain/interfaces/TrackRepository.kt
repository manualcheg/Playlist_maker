package com.practicum.playlistmaker.player.domain.interfaces

import android.provider.MediaStore.Audio.Media
import com.practicum.playlistmaker.player.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.search.domain.entities.Track

interface TrackRepository {
    var playerState: MediaPlayerState
    fun getTrack(): Track

    fun preparePlayer(mediaPlayerPreparator:MediaPlayerPrepare)

    fun startPlayer():MediaPlayerState

    fun pausePlayer():MediaPlayerState

    fun playerRelease()

    fun playerGetCurrentPosition():Int
}