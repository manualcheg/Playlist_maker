package com.practicum.playlistmaker.search.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.search.ui.models.SearchState
import com.practicum.playlistmaker.utils.Creator

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val tracks = ArrayList<Track>()

    private val searchInteractor = Creator.provideSearchInteractor(application)

    private var latestSearchText: String? = null

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData
    /*    private val toastState = MutableLiveData<String>()
        fun observeToastState(): LiveData<String> = toastState*/
    private val showToast = SingleLiveEvent<String>()
    fun observeShowToast(): LiveData<String> = showToast


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
                                renderState(
                                    SearchState.Error(
                                        getApplication<Application>().getString(
                                            R.string.something_went_wrong
                                        )
                                    )
                                )
                                showToast(errorMessage)
                            }

                            tracks.isEmpty() -> {
                                renderState(
                                    SearchState.Empty(
                                        getApplication<Application>().getString(
                                            R.string.nothing_found
                                        )
                                    )
                                )
                            }

                            else -> {
                                renderState(SearchState.Content(movies = tracks))
                            }
                        }
                    }
                })
        }
    }

    private fun renderState(state: SearchState) {
        //кладём значение в LiveData состояний экрана поиска
        stateLiveData.postValue(state)
        //метод postValue можно выполнять не только в главном потоке
    }

    fun showToast(message:String){
        showToast.postValue(message)
    }



/*    companion object {
        fun getViewModelFactory(searchExpression: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(
                        searchExpression
                    ) as T
                }
            }
    }*/

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }
}