package com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels

import android.app.Application
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.domain.interfaces.PlaylistDBInteractor
import com.practicum.playlistmaker.search.domain.entities.Track
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistWorkFragmentViewModel(
    private val playlistDBInteractor: PlaylistDBInteractor,
    private val application: Application
) :
    ViewModel() {
    var localPlaylistId: Long = 0
    var localPlaylist: Playlist? = null

    private var _playlist = MutableLiveData<Playlist>()
    var playlist: LiveData<Playlist> = _playlist

    private var _listOfTracks = MutableLiveData<List<Track>>()
    var listOfTracks: LiveData<List<Track>> = _listOfTracks

    private var _totalDuration = MutableLiveData<String>()
    var totalDuration: LiveData<String> = _totalDuration

    private var _playlistTextForShare = MutableLiveData<String>()
    var playlistTextForShare: LiveData<String> = _playlistTextForShare

    fun getPlaylist(playlistId: Long) {
        viewModelScope.launch {
            localPlaylistId = playlistId
            localPlaylist = playlistDBInteractor.getPlaylist(playlistId)
            _playlist.postValue(localPlaylist)
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
            playlistDBInteractor.getTracksFromPlaylist(listTracksId)
                .collect { receivedListOfTracks ->
                    _listOfTracks.postValue(receivedListOfTracks)
                    // запрос на отправку общей длительности треков плейлиста
                    getTotalDuration(receivedListOfTracks)
                }
        }
    }

    private fun getTotalDuration(listOfTracks: List<Track>) {
        var totalTime = 0
        for (track in listOfTracks) {
            totalTime += track.trackTime?.toInt() ?: 0
        }
        _totalDuration.postValue(
            SimpleDateFormat("mm", Locale.getDefault()).format(totalTime).toString()
        )
    }

    fun delTrack(trackId: String, playlist: Playlist) {
        viewModelScope.launch {
            async { playlistDBInteractor.delTrack(trackId, playlist) }.await()
            getPlaylist(playlist.playlistId)
        }
    }

    fun makeTextFromListOfTracks(localListOfTracks: List<Track>, playlist: Playlist?) {
        var plainTextTracks = ""
        localListOfTracks.forEachIndexed { index, track ->
            plainTextTracks += "\n${index + 1}. ${track.artistName} - ${track.trackName} (${
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(track.trackTime?.toInt())
            })"
        }
        _playlistTextForShare.postValue(
            "${playlist?.playlistName}\n" +
                    "${playlist?.playlistDescription}\n" +
                    countOfTracksWithWord(playlist) +
                    plainTextTracks
        )
    }

    private fun countOfTracksWithWord(playlist: Playlist?): String {
        val countOfTracksInt = playlist?.countOfTracks!!
        return application.resources.getQuantityString(
            R.plurals.tracks,
            countOfTracksInt,
            countOfTracksInt
        )
    }

    fun delPlaylist(playlist: Playlist, onResultListener: () -> Unit) {

        /*        val job = viewModelScope.launch {
                    delEveryTrackFromTable(playlist)
                }
                job.invokeOnCompletion {
                    viewModelScope.launch {
                        playlistDBInteractor.delPlaylist(playlist)
                    }
                }*/

        /*        viewModelScope.launch {
                    async{ playlistDBInteractor.delPlaylist(playlist) }.await()
                    async { playlistDBInteractor.delEveryTrack(playlist) }.await()
                }*/

        val job =
            viewModelScope.launch { async { playlistDBInteractor.delPlaylist(playlist) }.await() }
        job.invokeOnCompletion {
            viewModelScope.launch {
                async { playlistDBInteractor.delEveryTrack(playlist) }.await()
                onResultListener()  //тут он на своем месте. Закрытие экрана теперь происходит вовремя
                                    // и корутины не отменяются
            }
        }

        // Удаление обложки
        val file = playlist.playlistCover?.toUri()?.path?.let { File(it) }
        file?.delete()
    }
}