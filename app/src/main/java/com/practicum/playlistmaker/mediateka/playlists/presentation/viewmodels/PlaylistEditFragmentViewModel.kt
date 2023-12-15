package com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.domain.interfaces.PlaylistDBInteractor
import kotlinx.coroutines.launch
import java.io.File

class PlaylistEditFragmentViewModel(private val playlistDBInteractor: PlaylistDBInteractor) :
    PlaylistCreateViewModel(
        playlistDBInteractor
    ) {
    var localPlaylist: Playlist? = null

    private var _playlist = MutableLiveData<Playlist>()
    var playlist: LiveData<Playlist> = _playlist

    fun getPlaylist(playlistId: Long) {
        viewModelScope.launch {
            localPlaylist = playlistDBInteractor.getPlaylist(playlistId)
            _playlist.postValue(localPlaylist)
        }
    }

    fun delPlaylistCover(oldPlaylistCover: String) {
        val file = oldPlaylistCover.toUri().path?.let { File(it) }
        file?.delete()
    }
}