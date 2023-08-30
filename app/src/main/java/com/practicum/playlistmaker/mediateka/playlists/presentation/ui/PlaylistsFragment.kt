package com.practicum.playlistmaker.mediateka.playlists.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private val playlistsFragmentViewModel: PlaylistsFragmentViewModel by viewModel()
    private val playlists: List<Playlist> = ArrayList()

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

        playlistsFragmentViewModel.getPlaylists()

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