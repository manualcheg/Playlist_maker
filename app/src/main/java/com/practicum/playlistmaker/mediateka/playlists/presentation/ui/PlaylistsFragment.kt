package com.practicum.playlistmaker.mediateka.playlists.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.mediateka.playlists.domain.PlaylistsState
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.presentation.PlaylistAdapter
import com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment(), PlaylistAdapter.PlaylistWorkPlaylistClickListener {
    private lateinit var binding: FragmentPlaylistsBinding
    private val playlistsFragmentViewModel: PlaylistsFragmentViewModel by viewModel()
    private val playlists: ArrayList<Playlist> = ArrayList()
    private var playlistAdapter = PlaylistAdapter(playlists,this)
    private val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewPlaylist.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewPlaylist.adapter = playlistAdapter

        playlistsFragmentViewModel.getPlaylists()
        playlistsFragmentViewModel.stateLiveData().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        binding.fragmentFavouritesButtonCreatePlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediatekaFragment_to_playlistCreateFragment)
        }
    }

    private fun render(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Empty -> showEmpty()
            is PlaylistsState.Content -> showContent(state.playlists)
        }
    }

    private fun showContent(playlists: List<Playlist>) {
        playlistAdapter = PlaylistAdapter(playlists as MutableList<Playlist>, this)
        binding.recyclerViewPlaylist.adapter = playlistAdapter
        playlistAdapter.notifyItemRangeChanged(0, playlists.lastIndex)

        binding.recyclerViewPlaylist.visibility = View.VISIBLE
        binding.placeholderFragmentPlaylistThereIsNothing.visibility = View.GONE
    }

    private fun showEmpty() {
        binding.recyclerViewPlaylist.visibility = View.GONE
        binding.placeholderFragmentPlaylistThereIsNothing.visibility = View.VISIBLE
    }

    override fun playlistClick(playlist: Playlist) {
        bundle.putLong(PLAYLISTID,playlist.playlistId)
        findNavController().navigate(R.id.action_mediatekaFragment_to_playlistWorkFragment, bundle)
    }

    companion object{
        const val PLAYLISTID = "playlistId"
    }
}