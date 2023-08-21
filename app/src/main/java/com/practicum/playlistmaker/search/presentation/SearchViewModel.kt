package com.practicum.playlistmaker.search.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.search.presentation.ui.models.SearchState
import com.practicum.playlistmaker.utils.Constants.Companion.SEARCH_DEBOUNCE_DELAY_MILLIS
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(application: Application, private val searchInteractor: SearchInteractor) :
    AndroidViewModel(application) {
    private val tracks = ArrayList<Track>()

    private var latestSearchText: String? = ""

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeStateLiveData(): LiveData<SearchState> = stateLiveData

    private var searchDebounce: Job? = null

    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)

            viewModelScope.launch {
                //метод searchTracks возвращает Flow:
                searchInteractor.searchTracks(newSearchText)
                    .collect { pair ->
                        val foundTracks = pair.first
                        val errorMessage = pair.second

                        if (foundTracks != null) {
                            tracks.clear()
                            tracks.addAll(foundTracks)
                        }
                        when {
                            errorMessage != null -> {
                                if (errorMessage == getApplication<Application>().getString(
                                        R.string.nothing_found
                                    )
                                ) {
                                    renderState(
                                        SearchState.Empty(
                                            getApplication<Application>().getString(
                                                R.string.nothing_found
                                            )
                                        )
                                    )
                                } else {
                                    renderState(
                                        SearchState.Error(
                                            getApplication<Application>().getString(
                                                R.string.something_went_wrong
                                            )
                                        )
                                    )
                                }
                            }

                            else -> {
                                renderState(SearchState.Content(tracks = tracks))
                            }
                        }
                    }
            }
        }
    }

    fun searchDebounce(newSearchText: String) {
        searchDebounce?.cancel()
        searchDebounce = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            latestSearchText = newSearchText
            searchRequest(newSearchText)
        }
    }

    private fun renderState(state: SearchState) {
        //кладём значение в LiveData состояний экрана поиска
        stateLiveData.postValue(state)
        //метод postValue можно выполнять не только в главном потоке
    }

    suspend fun getData(): List<Track> {
        return searchInteractor.getHistoryList()
    }

    fun clearHistory() {
        searchInteractor.clearHistory()
    }
}