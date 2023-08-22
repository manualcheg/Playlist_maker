package com.practicum.playlistmaker.mediateka.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding

class PlaylistsFragment : Fragment() {
    companion object {

        fun newInstance(some: String) = PlaylistsFragment().apply {
            arguments = Bundle().apply {
                putString("some", some)
            }
        }
    }

    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }
}