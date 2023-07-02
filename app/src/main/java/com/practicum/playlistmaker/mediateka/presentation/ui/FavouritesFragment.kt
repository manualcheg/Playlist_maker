package com.practicum.playlistmaker.mediateka.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentFavouritesBinding
import com.practicum.playlistmaker.mediateka.presentation.viewmodels.FavouritesFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavouritesFragment : Fragment() {

    companion object {
        private const val SOME = "some"

        fun newInstance(some: String) = FavouritesFragment().apply {
            arguments = Bundle().apply {
                putString(SOME, some)
            }
        }
    }

    private val favouritesFragmentViewModel: FavouritesFragmentViewModel by viewModel {
        parametersOf(
            requireArguments().getString("some")
        )
    }

    private lateinit var binding: FragmentFavouritesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}