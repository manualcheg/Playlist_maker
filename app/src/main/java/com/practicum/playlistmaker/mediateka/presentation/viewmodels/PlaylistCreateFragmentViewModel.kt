package com.practicum.playlistmaker.mediateka.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediateka.data.db.entity.Playlist
import com.practicum.playlistmaker.mediateka.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.mediateka.domain.interfaces.PlaylistDBInteractor
import kotlinx.coroutines.launch

class PlaylistCreateFragmentViewModel(private val playlistDBInteractor: PlaylistDBInteractor):ViewModel() {

    fun putPlaylist(playlist: PlaylistEntity){
        viewModelScope.launch {
            playlistDBInteractor.putPlaylist(playlist)
        }
    }




}