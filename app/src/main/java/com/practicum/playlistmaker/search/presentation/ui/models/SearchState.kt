package com.practicum.playlistmaker.search.presentation.ui.models

import com.practicum.playlistmaker.search.domain.entities.Track

interface SearchState {
    object Loading: SearchState
    data class Content(val tracks:List<Track>): SearchState
    data class Error(val errorMessage:String): SearchState
    data class Empty(val message:String): SearchState
}