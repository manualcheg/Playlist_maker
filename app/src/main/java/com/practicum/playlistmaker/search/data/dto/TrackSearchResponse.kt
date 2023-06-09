package com.practicum.playlistmaker.search.data.dto

import com.practicum.playlistmaker.search.domain.entities.Track

//class TrackSearchResponse(val results: List<Track>)
class TrackSearchResponse(
    val searchType: String,
    val expression: String,
//    val results: List<TrackDto>
    val results: List<Track>
) : Response()