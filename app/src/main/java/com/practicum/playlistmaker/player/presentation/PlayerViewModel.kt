package com.practicum.playlistmaker.player.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.player.domain.interfaces.MediaPlayerPrepare
import com.practicum.playlistmaker.player.domain.interfaces.TrackInteractor
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.utils.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val trackInteractorImpl: TrackInteractor) : ViewModel(),
    MediaPlayerPrepare {

    private var playerState = MediaPlayerState.STATE_DEFAULT

    private var playerStateLiveData = MutableLiveData<MediaPlayerState>()
    fun getPlayerStateLiveData(): LiveData<MediaPlayerState> = playerStateLiveData

    private val playbackTimeLiveData = MutableLiveData<String?>()
    val playbackTimeLive: LiveData<String?> = playbackTimeLiveData

    private var timerJob: Job? = null

    fun onActivityCreate() {
        // сообщение начального состояния
        playerStateLiveData.postValue(trackInteractorImpl.returnPlayerState())
    }

    fun preparePlayer() {
        trackInteractorImpl.preparePlayer(this)
        playerStateLiveData.postValue(trackInteractorImpl.returnPlayerState())
    }

    fun onActivityPause() {
        trackInteractorImpl.pausePlayer()
        playerState = MediaPlayerState.STATE_PAUSED
        playerStateLiveData.postValue(playerState)
        timerJob?.cancel()
    }

    override fun onPrepared() {
        playerStateLiveData.postValue(MediaPlayerState.STATE_PREPARED)
    }

    override fun onCompletion() {
        timerJob?.cancel()
        playerStateLiveData.postValue(MediaPlayerState.STATE_PREPARED)
        playbackTimeLiveData.postValue(Constants._00_00)
    }

    fun onActivityDestroy() {
        trackInteractorImpl.playerRelease()
    }

    fun onPlayButtonClick() {
        trackInteractorImpl.returnPlayerState()
        playerState = trackInteractorImpl.playbackControl()
        playerStateLiveData.postValue(playerState)

        when (playerState) {
            MediaPlayerState.STATE_PLAYING -> {
                startTimer()
            }

            MediaPlayerState.STATE_PAUSED, MediaPlayerState.STATE_PREPARED -> {
                timerJob?.cancel()
            }

            else -> {}
        }
    }

    fun getTrack(): Track {
        return trackInteractorImpl.getTrack()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerState == MediaPlayerState.STATE_PLAYING) {

                delay(Constants.PLAYBACK_TIME_RENEW_DELAY_MILLIS)
                playbackTimeLiveData.postValue(
                    SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(trackInteractorImpl.playerGetCurrentPosition())
                )
            }
        }
    }
}