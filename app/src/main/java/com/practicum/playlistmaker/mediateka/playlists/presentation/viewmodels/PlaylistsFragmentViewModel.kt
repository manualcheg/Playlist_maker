package com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediateka.playlists.domain.PlaylistsState
import com.practicum.playlistmaker.mediateka.playlists.domain.interfaces.PlaylistDBInteractor
import kotlinx.coroutines.launch

class PlaylistsFragmentViewModel(private val playlistsDBInteractor: PlaylistDBInteractor) :
    ViewModel() {

    private val _stateLiveData = MutableLiveData<PlaylistsState>()
    fun stateLiveData(): LiveData<PlaylistsState> = _stateLiveData

    fun getPlaylists() {
        viewModelScope.launch {
            playlistsDBInteractor.getPlaylist().collect { listOfPlaylists ->
                if (listOfPlaylists.isEmpty()){
                    _stateLiveData.postValue(PlaylistsState.Empty())
                } else {
                    _stateLiveData.postValue(PlaylistsState.Content(listOfPlaylists))
                }
            }
        }
    }
}