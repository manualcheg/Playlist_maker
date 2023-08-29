package com.practicum.playlistmaker.mediateka.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentFavouritesBinding
import com.practicum.playlistmaker.mediateka.domain.FavouritesState
import com.practicum.playlistmaker.mediateka.presentation.viewmodels.FavouritesFragmentViewModel
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.search.presentation.ui.SearchAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment() {

    private val favouritesFragmentViewModel: FavouritesFragmentViewModel by viewModel()

    private var favouritesTracks: ArrayList<Track>? = ArrayList()
    private var favouritesTracksAdapter = favouritesTracks?.let { SearchAdapter(it) }

    private var binding: FragmentFavouritesBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.recyclerViewFavouritesTracks?.adapter = favouritesTracksAdapter

        getFavouritesAndObserveToLiveData()
    }

    private fun getFavouritesAndObserveToLiveData() {
        favouritesFragmentViewModel.getFavourites()
        favouritesFragmentViewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: FavouritesState) {
        when (state) {
            is FavouritesState.Empty -> showEmpty()
            is FavouritesState.Content -> showContent(state.tracks)
        }
    }

    private fun showContent(tracks: List<Track>) {
        favouritesTracksAdapter = SearchAdapter(tracks as MutableList<Track>)
        binding?.recyclerViewFavouritesTracks?.adapter = favouritesTracksAdapter
        favouritesTracksAdapter!!.notifyItemRangeChanged(0, favouritesTracks!!.lastIndex)

        binding?.recyclerViewFavouritesTracks?.visibility = View.VISIBLE
        binding?.placeholderFragmentFavouritesThereIsNothing?.visibility = View.GONE
    }

    private fun showEmpty() {
        binding?.recyclerViewFavouritesTracks?.visibility = View.GONE
        binding?.placeholderFragmentFavouritesThereIsNothing?.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        getFavouritesAndObserveToLiveData()
    }
}