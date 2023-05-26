package com.practicum.playlistmaker.playerActivity.data.repository

import android.content.Intent
import android.media.MediaPlayer
import com.practicum.playlistmaker.playerActivity.data.intent.TrackIntentDAOImpl
import com.practicum.playlistmaker.playerActivity.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.playerActivity.domain.entities.Track
import com.practicum.playlistmaker.playerActivity.domain.interfaces.MediaPlayerPrepare
import com.practicum.playlistmaker.playerActivity.domain.interfaces.TrackRepository

class TrackRepositoryImpl(private val intent: Intent, val mediaPlayer: MediaPlayer) :
    TrackRepository {
    var playerState = MediaPlayerState.STATE_DEFAULT
    override fun getTrack(): Track {
        val trackIntentDAOImpl = TrackIntentDAOImpl(intent)
        return trackIntentDAOImpl.getTrack()
    }

    override fun preparePlayer(mediaPlayerPreparator:MediaPlayerPrepare) {
        val url = getTrack().previewUrl
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = MediaPlayerState.STATE_PREPARED
            mediaPlayerPreparator.onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = MediaPlayerState.STATE_PREPARED
            mediaPlayerPreparator.onCompletion()
        }
    }

    override fun startPlayer(): MediaPlayerState {
        mediaPlayer.start()
        playerState = MediaPlayerState.STATE_PLAYING
        return playerState
    }

    override fun pausePlayer(): MediaPlayerState {
        mediaPlayer.pause()
        playerState = MediaPlayerState.STATE_PAUSED
        return playerState
    }
}