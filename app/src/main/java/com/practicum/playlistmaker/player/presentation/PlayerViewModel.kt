package com.practicum.playlistmaker.player.presentation

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.player.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.player.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.player.domain.interfaces.MediaPlayerPrepare
import com.practicum.playlistmaker.player.domain.usecases.TrackInteractorImlp
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.utils.Constants
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerViewModel(val trackRepositoryImpl: TrackRepositoryImpl) : ViewModel(),
    MediaPlayerPrepare {

    private var playerState = MediaPlayerState.STATE_DEFAULT
    var mainThreadHandler: Handler = Handler(Looper.getMainLooper())

    private var playerStateLiveData = MutableLiveData<MediaPlayerState>()
    fun getPlayerStateLiveData(): LiveData<MediaPlayerState> = playerStateLiveData

    private val playbackTimeLiveData = MutableLiveData<String?>()
    val playbackTimeLive: LiveData<String?> = playbackTimeLiveData

//    private val currentPositionInMsec = trackRepositoryImpl.playerGetCurrentPosition()

    fun onActivityCreate() {
//        сообщение начального состояния
        playerStateLiveData.postValue(trackRepositoryImpl.playerState)
    }

    fun preparePlayer() {
        trackRepositoryImpl.preparePlayer(this)
    }

    fun onActivityPause() {
        val trackInteractorImlp =
            TrackInteractorImlp(trackRepository = trackRepositoryImpl, playerState = playerState)
        trackInteractorImlp.pausePlayer()
        playerState = MediaPlayerState.STATE_PAUSED
        playerStateLiveData.postValue(playerState)
        mainThreadHandler.removeCallbacks(runPlaybackTime)
    }

    override fun onPrepared() {
        mainThreadHandler.removeCallbacks(runPlaybackTime)
        playerStateLiveData.postValue(MediaPlayerState.STATE_PREPARED)
    }

    override fun onCompletion() {
        mainThreadHandler.removeCallbacks(runPlaybackTime)
        playerStateLiveData.postValue(MediaPlayerState.STATE_PREPARED)
        playbackTimeLiveData.postValue(Constants._00_00)
    }

    fun onActivityDestroy(){
        mainThreadHandler.removeCallbacks(runPlaybackTime)
        trackRepositoryImpl.playerRelease()
    }

    fun onPlayButtonClick(){
        playerState = trackRepositoryImpl.playerState
        val trackInteractorImlp = TrackInteractorImlp(
            trackRepository = trackRepositoryImpl,
            playerState = playerState
        )
        playerState = trackInteractorImlp.playbackControl()
        playerStateLiveData.postValue(playerState)

        when (playerState) {
            MediaPlayerState.STATE_PLAYING -> {
                mainThreadHandler.post(runPlaybackTime)
            }

            MediaPlayerState.STATE_PAUSED, MediaPlayerState.STATE_PREPARED -> {
                mainThreadHandler.removeCallbacks(runPlaybackTime)
            }
            else -> {}
        }
    }

    fun getTrack(): Track {
        return trackRepositoryImpl.getTrack()
    }

    private val runPlaybackTime =
        object : Runnable {
            override fun run() {
                playbackTimeLiveData.postValue(
                    SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(trackRepositoryImpl.playerGetCurrentPosition())
                )

                val postTime = SystemClock.uptimeMillis() + Constants.PLAYBACK_TIME_RENEW_DELAY_MS
                mainThreadHandler.postAtTime(this, Constants.PLAYER_TIMER_TOKEN, postTime)
            }
        }

    companion object {
        fun getViewModelFactory(trackRepositoryImpl: TrackRepositoryImpl): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel(
                        trackRepositoryImpl
                    ) as T
                }
            }
    }
}
