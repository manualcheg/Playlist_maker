package com.practicum.playlistmaker.player.presentation

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.player.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.player.domain.interfaces.MediaPlayerPrepare
import com.practicum.playlistmaker.player.domain.usecases.TrackInteractorImlp
import com.practicum.playlistmaker.player.ui.PlayerActivity
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerViewModel(val intent: Intent): ViewModel(), MediaPlayerPrepare {
    private val trackRepositoryImpl by lazy { TrackRepositoryImpl(intent) }
    private var playerState = MediaPlayerState.STATE_DEFAULT
    var mainThreadHandler: Handler = Handler(Looper.getMainLooper())

    private var playerStateLiveData = MutableLiveData<MediaPlayerState>()
    fun getPlayerStateLiveData(): LiveData<MediaPlayerState> = playerStateLiveData

    fun onCreate(){
//        сообщение начального состояния
        playerStateLiveData.postValue(playerState)
    }

    fun preparePlayer(){
        trackRepositoryImpl.preparePlayer(this)
    }

    fun onPause(){
        val trackInteractorImlp = TrackInteractorImlp(trackRepository = trackRepositoryImpl, playerState = playerState)
        trackInteractorImlp.pausePlayer()
        playerState = MediaPlayerState.STATE_PAUSED
//        playerStateLiveData.postValue()
    }

    override fun onPrepared() {
        mainThreadHandler.removeCallbacks(runPlaybackTime)
    }

    override fun onCompletion() {
        mainThreadHandler.removeCallbacks(runPlaybackTime)
    }

    private val runPlaybackTime =
        object : Runnable {
            override fun run() {
                playbackTime.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(trackRepositoryImpl.playerGetCurrentPosition())
                mainThreadHandler.postDelayed(this, SEARCH_REQUEST_TOKEN, PlayerActivity.PLAYBACK_TIME_RENEW_DELAY_MS)
            }
        }
}