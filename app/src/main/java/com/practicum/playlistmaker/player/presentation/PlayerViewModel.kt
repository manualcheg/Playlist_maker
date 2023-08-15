package com.practicum.playlistmaker.player.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediateka.data.db.TrackDBConvertor
import com.practicum.playlistmaker.mediateka.domain.interfaces.TracksDBInteractor
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

class PlayerViewModel(
    private val trackInteractorImpl: TrackInteractor,
    private val tracksDBInteractorImpl: TracksDBInteractor,
    private val dbConvertor: TrackDBConvertor
) : ViewModel(),
    MediaPlayerPrepare {

    private var playerState = MediaPlayerState.STATE_DEFAULT

    private var playerStateLiveData = MutableLiveData<MediaPlayerState>()
    fun getPlayerStateLiveData(): LiveData<MediaPlayerState> = playerStateLiveData

    private val playbackTimeLiveData = MutableLiveData<String?>()
    val playbackTimeLive: LiveData<String?> = playbackTimeLiveData

    private val inFavouriteLiveData = MutableLiveData<Boolean>()
    val inFavouriteLive: LiveData<Boolean> = inFavouriteLiveData

    private var timerJob: Job? = null

    fun onActivityCreate() {
        // сообщение начального состояния
        playerStateLiveData.postValue(trackInteractorImpl.returnPlayerState())
    }

    fun preparePlayer(track:Track) {
        checkTrackInFavourites(track)

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
        playbackTimeLiveData.postValue(Constants.time_00_00)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun onFavouriteClicked(track: Track) {
        if (track.inFavourite) {
            viewModelScope.launch {
                tracksDBInteractorImpl.delTrack(dbConvertor.map(track))
            }
        } else {
            viewModelScope.launch {
                tracksDBInteractorImpl.putTrack(dbConvertor.map(track))
            }
        }
        track.inFavourite = !track.inFavourite
        inFavouriteLiveData.postValue(track.inFavourite)
    }

    private fun checkTrackInFavourites(track:Track){
        viewModelScope.launch {
            val favouritesTracksIds = tracksDBInteractorImpl.getFavouritesTracksIds()
            track.inFavourite = favouritesTracksIds.contains(track.trackId)
            inFavouriteLiveData.postValue(track.inFavourite)
        }
    }
}