package com.practicum.playlistmaker.player.data.repository

import android.content.Intent
import android.media.MediaPlayer
import com.practicum.playlistmaker.player.data.intent.TrackIntentDAOImpl
import com.practicum.playlistmaker.player.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.player.domain.interfaces.MediaPlayerPrepare
import com.practicum.playlistmaker.player.domain.interfaces.TrackRepository
import com.practicum.playlistmaker.search.domain.entities.Track

class TrackRepositoryImpl(private val intent: Intent) :
    TrackRepository {

    var mediaPlayer = MediaPlayer()
    var playerState = MediaPlayerState.STATE_DEFAULT
    var currentPositionInMsec:Int = 0
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
            currentPositionInMsec = 0
            playerState = MediaPlayerState.STATE_PREPARED
            mediaPlayerPreparator.onCompletion()
        }
    }

    override fun startPlayer(): MediaPlayerState {
        mediaPlayer.start()
        if (currentPositionInMsec!=0){
            mediaPlayer.seekTo(currentPositionInMsec)
        }
        playerState = MediaPlayerState.STATE_PLAYING
        return playerState
    }

    override fun pausePlayer(): MediaPlayerState {
        mediaPlayer.pause()
        currentPositionInMsec = mediaPlayer.currentPosition
        playerState = MediaPlayerState.STATE_PAUSED
        return playerState
    }

    fun playerRelease(){
//        внедрил костыль mediaPlayer = MediaPlayer() из-за ошибки настройки
//        плеера при setDataSource при повороте и последующей фатальной ошибки экрана
//        плюс сохраняю текущий прогресс проигрывания для перестроения экрана при повороте
                currentPositionInMsec = mediaPlayer.currentPosition
                mediaPlayer.release()
                mediaPlayer = MediaPlayer()
    }

    fun playerGetCurrentPosition():Int {
        return mediaPlayer.currentPosition
    }
}