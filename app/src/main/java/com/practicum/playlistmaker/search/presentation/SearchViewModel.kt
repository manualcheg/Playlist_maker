package com.practicum.playlistmaker.search.presentation

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.search.presentation.ui.models.SearchState
import com.practicum.playlistmaker.utils.Constants.Companion.SEARCH_DEBOUNCE_DELAY
import org.koin.java.KoinJavaComponent.getKoin

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    //class SearchViewModel(context: Context) : ViewModel() {
    private val tracks = ArrayList<Track>()

    //    private val searchInteractor = Creator.provideSearchInteractor(application)
    private val searchInteractor: SearchInteractor = getKoin().get()

    private var latestSearchText: String? = ""

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchRequest(latestSearchText ?: "") }

    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)
            searchInteractor.searchTracks(
                newSearchText,
                object : SearchInteractor.SearchConsumer {
                    override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
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
                })
        }
    }

    fun searchDebounce(newSearchText: String) {
        handler.removeCallbacks(searchRunnable)
        latestSearchText = newSearchText
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun renderState(state: SearchState) {
        //кладём значение в LiveData состояний экрана поиска
        stateLiveData.postValue(state)
        //метод postValue можно выполнять не только в главном потоке
    }

/*    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }*/
}