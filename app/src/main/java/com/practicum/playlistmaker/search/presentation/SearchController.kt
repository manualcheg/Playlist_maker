package com.practicum.playlistmaker.search.presentation

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.utils.Creator

class SearchController {
    companion object {
        const val NOTHING_FOUND = "1"
        const val SOMETHING_WENT_WRONG = "2"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val searchInteractor = Creator.provideSearchInteractor()

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search() }

    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderButtonReload: AppCompatButton
    private lateinit var layoutOfListenedTracks: LinearLayout
    private lateinit var progressBar: ProgressBar

    private var userInputText: String = ""

    private fun search() {
        if (userInputText.isNotEmpty()) {

            placeholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            placeholderButtonReload.visibility = View.GONE
            layoutOfListenedTracks.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            searchInteractor.searchTracks(userInputText, object: SearchInteractor.SearchConsumer{
                override fun consume(foundTracks: List<Track>){
                    handler.post {
                        progressBar.visibility = View.GONE
                        trackList.clear()
                        trackList.addAll(foundTracks)
                        recyclerViewSearch.visibility = View.VISIBLE
                        trackListAdapter.notifyDataSetChanged()

                        if (trackList.isNotEmpty()) {
                            showMessage("", "")
                            placeholderMessage.visibility = View.GONE
                            placeholderImage.visibility = View.GONE
                            placeholderButtonReload.visibility = View.GONE
                        } else {
                            showMessage(getString(R.string.nothing_found), NOTHING_FOUND)
                        }
                    }
                }
            })

            /*            itunesService.search(userInputText)
                            .enqueue(object : Callback<TrackSearchResponse> {
                                override fun onResponse(
                                    call: Call<TrackSearchResponse>, response: Response<TrackSearchResponse>
                                ) {
                                    progressBar.visibility = View.GONE
                                    when (response.code()) {

                                        200 -> {        //success
                                            if (response.body()?.results?.isNotEmpty() == true) {
                                                placeholderMessage.visibility = View.GONE
                                                placeholderImage.visibility = View.GONE
                                                placeholderButtonReload.visibility = View.GONE
                                                trackListAdapter.setTracks(
                                                    response.body()?.results!!
                                                )
                                                showMessage("", "")
                                            } else {
                                                showMessage(getString(R.string.nothing_found), NOTHING_FOUND)
                                            }
                                        }

                                        else -> {
                                            //error with server answer
                                            showMessage(
                                                getString(R.string.something_went_wrong),
                                                SOMETHING_WENT_WRONG
                                            )
                                        }
                                    }
                                }

                                override fun onFailure( //error without server answer
                                    call: Call<TrackSearchResponse>, t: Throwable
                                ) {
                                    progressBar.visibility = View.GONE
                                    showMessage(getString(R.string.something_went_wrong), SOMETHING_WENT_WRONG)
                                }
                            })*/
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun showMessage(text: String, myErrorCode: String) {
        if (text.isNotEmpty()) {
            trackListAdapter.setTracks(trackList)
            placeholderMessage.text = text
            if (myErrorCode == NOTHING_FOUND) {
                placeholderImage.setImageResource(R.drawable.placeholder_nothing_found)
                placeholderButtonReload.visibility = View.GONE
            } else if (myErrorCode == SOMETHING_WENT_WRONG) {
                placeholderImage.setImageResource(R.drawable.placeholder_no_network)
                placeholderButtonReload.visibility = View.VISIBLE
            }
            placeholderMessage.visibility = View.VISIBLE
            placeholderImage.visibility = View.VISIBLE
        }
    }


}