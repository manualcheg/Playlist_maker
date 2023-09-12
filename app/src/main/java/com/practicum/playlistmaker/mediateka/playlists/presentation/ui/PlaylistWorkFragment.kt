package com.practicum.playlistmaker.mediateka.playlists.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistWorkBinding
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels.PlaylistWorkFragmentViewModel
import com.practicum.playlistmaker.search.domain.entities.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistWorkFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistWorkBinding
    private val playlistFragmentWorkFragmentViewModel: PlaylistWorkFragmentViewModel by viewModel()
    private var playlistInViewModule : Playlist? = null
    private var listOfTracksId = listOf<String>()
    private var listOfTracks= listOf<Track>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = requireArguments().getLong("playlistId")
        playlistFragmentWorkFragmentViewModel.getPlaylist(playlistId)

//      Подписка на получение плейлиста
        playlistFragmentWorkFragmentViewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            playlistInViewModule = playlist

            binding.playlistWorkPlaylistName.text = playlist.playlistName
            if (playlist.playlistDescription != "") {
                binding.playlistWorkPlaylistDescription.visibility = View.VISIBLE
                binding.playlistWorkPlaylistDescription.text = playlist.playlistDescription
            } else {
                binding.playlistWorkPlaylistDescription.visibility = View.GONE
            }
            setImage(playlist.playlistCover)
            binding.playlistWorkTracksCount?.text =
                playlistFragmentWorkFragmentViewModel.defineWord(
                    playlist.countOfTracks
                )
//          Формируем список List<Int> id треков
            listOfTracksId = java.util.ArrayList(playlistInViewModule?.listOfTracksId?.split(",")!!)
            playlistFragmentWorkFragmentViewModel.getTracksOfPlaylist(listOfTracksId)
        }

//        Подписка на список треков
        playlistFragmentWorkFragmentViewModel.listOfTracks.observe(viewLifecycleOwner){
            listOfTracks = it
        }

//        Подписка на получение общей длительности треков в плейлисте
        playlistFragmentWorkFragmentViewModel.totalDuration.observe(viewLifecycleOwner){
            binding.playlistWorkTotalDuration.text = resources.getQuantityString(R.plurals.minutes,it.toInt(),it.toInt())
        }

        binding.playlistWorkArrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setImage(playlistCover: String?) {
        Glide.with(binding.playlistWorkPlaylistCover)
            .load(playlistCover)
            .placeholder(R.drawable.placeholder_no_cover)
            .into(binding.playlistWorkPlaylistCover)
    }
}