package com.practicum.playlistmaker.mediateka.favourites.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediateka.favourites.domain.FavouritesState
import com.practicum.playlistmaker.mediateka.favourites.domain.interfaces.TracksDBInteractor
import kotlinx.coroutines.launch

class FavouritesFragmentViewModel(private val tracksDBInteractor: TracksDBInteractor) :
    ViewModel() {

    private val _stateLiveData = MutableLiveData<FavouritesState>()
    fun stateLiveData(): LiveData<FavouritesState> = _stateLiveData

    fun getFavourites() {
        viewModelScope.launch {
            tracksDBInteractor.getFavourites().collect { listOfTracks ->
                if (listOfTracks.isEmpty()) {
                    _stateLiveData.postValue(FavouritesState.Empty())
                } else {
                    _stateLiveData.postValue(FavouritesState.Content(listOfTracks))
                }
            }
        }
    }
}