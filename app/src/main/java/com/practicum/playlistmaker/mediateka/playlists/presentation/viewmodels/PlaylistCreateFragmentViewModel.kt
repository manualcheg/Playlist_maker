package com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.domain.interfaces.PlaylistDBInteractor
import kotlinx.coroutines.launch

class PlaylistCreateFragmentViewModel(private val playlistDBInteractor: PlaylistDBInteractor):ViewModel() {

    fun putPlaylist(playlist: Playlist){
        viewModelScope.launch {
            playlistDBInteractor.putPlaylist(playlist)
        }
    }

    fun pickMediaLaunch(pickMedia: ActivityResultLauncher<PickVisualMediaRequest>) {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


}