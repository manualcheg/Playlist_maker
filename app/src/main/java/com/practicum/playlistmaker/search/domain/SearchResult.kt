package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.entities.Track

data class SearchResult(val list: List<Track>?, val message: String?)