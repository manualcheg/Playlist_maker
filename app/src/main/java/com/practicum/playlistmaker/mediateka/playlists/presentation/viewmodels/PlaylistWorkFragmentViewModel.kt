package com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.domain.interfaces.PlaylistDBInteractor
import com.practicum.playlistmaker.search.domain.entities.Track
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistWorkFragmentViewModel(private val playlistDBInteractor: PlaylistDBInteractor) :
    ViewModel() {

    private var _playlist = MutableLiveData<Playlist>()
    var playlist: LiveData<Playlist> = _playlist

    private var _listOfTracks = MutableLiveData<List<Track>>()
    var listOfTracks: LiveData<List<Track>> = _listOfTracks

    private var _totalDuration = MutableLiveData<String>()
    var totalDuration :LiveData<String> = _totalDuration

    fun getPlaylist(playlistId: Long) {
        viewModelScope.launch {
            _playlist.postValue(playlistDBInteractor.getPlaylist(playlistId))
        }
    }

    fun defineWord(count: Int): String {
        val predPosCifra = count % 100 / 10
        if (predPosCifra == 1) {
            return "$count треков"
        }
        return when (count % 10) {
            0, in 5..9 -> {
                "$count треков"
            }

            1 -> {
                "$count трек"
            }

            else -> {
                "$count трека"
            }
        }
    }

    fun getTracksOfPlaylist(listTracksId: List<String>) {
        viewModelScope.launch {
            playlistDBInteractor.getTracksFromPlaylist(listTracksId).collect { receivedListOfTracks ->
                _listOfTracks.postValue(receivedListOfTracks)
                // запрос на отправку общей длительности треков плейлиста
                getTotalDuration(receivedListOfTracks)
            }
        }
    }

    private fun getTotalDuration(listOfTracks: List<Track>){
        var totalTime = 0
        for (track in listOfTracks){
            totalTime += track.trackTime?.toInt() ?: 0
        }
        _totalDuration.postValue(SimpleDateFormat("mm", Locale.getDefault()).format(totalTime).toString())
    }
}