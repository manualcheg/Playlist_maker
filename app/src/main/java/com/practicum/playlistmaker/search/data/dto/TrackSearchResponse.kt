package com.practicum.playlistmaker.search.data.dto

import com.practicum.playlistmaker.search.domain.entities.Track

//class TrackSearchResponse(val results: List<Track>)
data class TrackSearchResponse(
    val searchType: String, val expression: String, val results: List<TrackDto>
) : Response()