package com.practicum.playlistmaker.search.data

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.mediateka.favourites.db.TracksDBFavourites
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchStorage: SearchStorage,
    private val context: Context,
    private val tracksDBFavourites: TracksDBFavourites
) : SearchRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к сети Интернет"))
            }

            200 -> {
                if ((response as TrackSearchResponse).results.isEmpty()) {
                    emit(
                        Resource.Error(
                            message = context.getString(R.string.nothing_found),
                            data = null
                        )
                    )
                } else {
                    val track = response.results.map {
                        Track(
                            trackName = it.trackName,
                            artistName = it.artistName,
                            trackTime = it.trackTime ?: "0",
                            artworkUrl100 = it.artworkUrl100 ?: "",
                            trackId = it.trackId ?: "0",
                            collectionName = it.collectionName ?: "",
                            releaseDate = it.releaseDate,
                            primaryGenreName = it.primaryGenreName ?: "",
                            country = it.country ?: "",
                            previewUrl = it.previewUrl ?: ""
                        )
                    }
                    // установка значений inFavourites для каждого трека из результатов поиска
                    val trackInFavourites = tracksDBFavourites.favouritesDao().getTracksId()
                    for (each in track) {
                        if (trackInFavourites.contains(each.trackId)){
                            each.inFavourite = true
                        }
                    }
                    emit(Resource.Success(track))
                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    override suspend fun getDataFromLocalStorage(): List<Track> {
        return searchStorage.getData()
    }

    override suspend fun saveDataToStorage(track: Track) {
        searchStorage.saveData(track)
    }

    override fun clearHistoryInStorage() {
        searchStorage.clearHistory()
    }

    override fun saveSearchHistoryList(historyList: ArrayList<Track>) {
        searchStorage.saveSearchHistoryList(historyList)
    }
}