package com.practicum.playlistmaker.mediateka.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediateka.domain.FavouritesState
import com.practicum.playlistmaker.mediateka.domain.interfaces.TracksDBInteractor
import kotlinx.coroutines.launch

class FavouritesFragmentViewModel(private val tracksDBInteractor: TracksDBInteractor) :
    ViewModel() {

    private val stateLiveData = MutableLiveData<FavouritesState>()
    fun observeState(): LiveData<FavouritesState> = stateLiveData

    fun getFavourites() {
        viewModelScope.launch {
            tracksDBInteractor.getFavourites().collect { listOfTracks ->
                if (listOfTracks.isEmpty()) {
                    stateLiveData.postValue(FavouritesState.Empty())
                } else {
                    stateLiveData.postValue(FavouritesState.Content(listOfTracks))
                }
            }
        }
    }
}