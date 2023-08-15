package com.practicum.playlistmaker.mediateka.domain

import com.practicum.playlistmaker.search.domain.entities.Track

interface FavouritesState {
    data class Content(val tracks:List<Track>): FavouritesState

    class Empty: FavouritesState
}
