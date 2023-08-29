package com.practicum.playlistmaker.mediateka.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentFavouritesButtonCreatePlaylist.setOnClickListener {
/*            parentFragment?.parentFragmentManager?.commit {
                setReorderingAllowed(true)
                replace(R.id.rootFragmentContainerView, PlaylistCreateFragment())
                addToBackStack(null)
            }*/
            findNavController().navigate(R.id.action_mediatekaFragment_to_playlistCreateFragment)
        }
    }
}