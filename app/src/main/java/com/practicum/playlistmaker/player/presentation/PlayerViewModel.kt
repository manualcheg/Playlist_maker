package com.practicum.playlistmaker.player.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediateka.favourites.domain.interfaces.TracksDBInteractor
import com.practicum.playlistmaker.mediateka.playlists.domain.PlaylistsState
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.domain.interfaces.PlaylistDBInteractor
import com.practicum.playlistmaker.player.domain.ResultAddTrack
import com.practicum.playlistmaker.player.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.player.domain.interfaces.MediaPlayerPrepare
import com.practicum.playlistmaker.player.domain.interfaces.TrackInteractor
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.search.presentation.SingleLiveEvent
import com.practicum.playlistmaker.utils.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val trackInteractorImpl: TrackInteractor,
    private val tracksDBInteractorImpl: TracksDBInteractor,
    private val playlistDBInteractorImpl: PlaylistDBInteractor
) : ViewModel(),
    MediaPlayerPrepare {

    private var playerState = MediaPlayerState.STATE_DEFAULT

    private var _playerStateLiveData = MutableLiveData<MediaPlayerState>()
    fun getPlayerStateLiveData(): LiveData<MediaPlayerState> = _playerStateLiveData

    private val _playbackTimeLiveData = MutableLiveData<String?>()
    val playbackTimeLiveData: LiveData<String?> = _playbackTimeLiveData

    private val _inFavouriteLiveData = MutableLiveData<Boolean>()
    val inFavouriteLiveData: LiveData<Boolean> = _inFavouriteLiveData

    private val _stateLiveData = MutableLiveData<PlaylistsState>()
    fun stateLiveData(): LiveData<PlaylistsState> = _stateLiveData

    private val _resultOfAddTrack = SingleLiveEvent<ResultAddTrack>()
    fun resultOfAddTrack(): LiveData<ResultAddTrack> = _resultOfAddTrack

    private var timerJob: Job? = null

    var bottomSheetMustBeCollapsed = false

    fun onActivityCreate() {
        // сообщение начального состояния
        _playerStateLiveData.postValue(trackInteractorImpl.returnPlayerState())
    }

    fun preparePlayer(track: Track) {
        checkTrackInFavourites(track)

        trackInteractorImpl.preparePlayer(this)
        _playerStateLiveData.postValue(trackInteractorImpl.returnPlayerState())
    }

    fun onActivityPause() {
        if (playerState == MediaPlayerState.STATE_PLAYING) {
            trackInteractorImpl.pausePlayer()
        }
        _playerStateLiveData.postValue(playerState)
        timerJob?.cancel()
    }

    override fun onPrepared() {
        _playerStateLiveData.postValue(MediaPlayerState.STATE_PREPARED)
    }

    override fun onCompletion() {
        timerJob?.cancel()
        _playerStateLiveData.postValue(MediaPlayerState.STATE_PREPARED)
        _playbackTimeLiveData.postValue(Constants.time_00_00)
    }

    fun onActivityDestroy() {
        trackInteractorImpl.playerRelease()
    }

    fun onPlayButtonClick() {
        trackInteractorImpl.returnPlayerState()
        playerState = trackInteractorImpl.playbackControl()
        _playerStateLiveData.postValue(playerState)

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
                _playbackTimeLiveData.postValue(
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
                tracksDBInteractorImpl.delTrack(track)
            }
        } else {
            viewModelScope.launch {
                tracksDBInteractorImpl.putTrack(track)
            }
        }
        track.inFavourite = !track.inFavourite
        _inFavouriteLiveData.postValue(track.inFavourite)
    }

    private fun checkTrackInFavourites(track: Track) {
        viewModelScope.launch {
            val favouritesTracksIds = tracksDBInteractorImpl.getFavouritesTracksIds()
            track.inFavourite = favouritesTracksIds.contains(track.trackId)
            _inFavouriteLiveData.postValue(track.inFavourite)
        }
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playlistDBInteractorImpl.getPlaylists().collect { listOfPlaylists ->
                if (listOfPlaylists.isEmpty()) {
                    _stateLiveData.postValue(PlaylistsState.Empty())
                } else {
                    _stateLiveData.postValue(PlaylistsState.Content(listOfPlaylists))
                }
            }
        }
    }

    fun putTrackToPlaylist(playlist: Playlist, track: Track) {
        val listTracksId = java.util.ArrayList(playlist.listOfTracksId?.split(",")!!)
        if (listTracksId.contains(track.trackId)) {
            _resultOfAddTrack.postValue(
                ResultAddTrack(
                    "Трек уже добавлен в плейлист ${playlist.playlistName}",
                    false
                )
            )
        } else {
            addTrackToPlaylistDB(listTracksId, track, playlist)
        }
    }

    private fun addTrackToPlaylistDB(
        listTracksId: List<String>?,
        track: Track,
        playlist: Playlist
    ) {
        val arrayListTracksId: List<String>? = listTracksId
        (arrayListTracksId as ArrayList<String>).add(track.trackId)
        arrayListTracksId.remove("")
        val newListTracksId = arrayListTracksId.joinToString(separator = ",")

        playlist.listOfTracksId = newListTracksId
        playlist.countOfTracks = arrayListTracksId.size
        viewModelScope.launch {
            playlistDBInteractorImpl.putPlaylist(playlist)
            playlistDBInteractorImpl.addTrackToDB(track)
        }
        _resultOfAddTrack.postValue(
            ResultAddTrack(
                "Добавлено в плейлист ${playlist.playlistName}",
                true
            )
        )
    }
}