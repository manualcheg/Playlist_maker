package com.practicum.playlistmaker.mediateka.playlists.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistWorkBinding
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.presentation.PlaylistWorkAdapter
import com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels.PlaylistWorkFragmentViewModel
import com.practicum.playlistmaker.search.domain.entities.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistWorkFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistWorkBinding
    private val playlistWorkFragmentViewModel: PlaylistWorkFragmentViewModel by viewModel()
    private var playlistInViewModule: Playlist? = null
    private var listOfTracksId = listOf<String>()
    private var listOfTracks = listOf<Track>()
    private var playlistId:Long = 0
    private var playlistWorkAdapter = PlaylistWorkAdapter(listOfTracks, playlistId)

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

        binding.playlistWorkRecyclerView.adapter = playlistWorkAdapter
        val playlistId = requireArguments().getLong("playlistId")
//        Запрос плейлиста
        playlistWorkFragmentViewModel.getPlaylist(playlistId)

//      Подписка на получение плейлиста
        playlistWorkFragmentViewModel.playlist.observe(viewLifecycleOwner) { playlist ->
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
                playlistWorkFragmentViewModel.defineWord(
                    playlist.countOfTracks
                )
//          Формируем список List<Int> id треков
            listOfTracksId = java.util.ArrayList(playlistInViewModule?.listOfTracksId?.split(",")!!)
            playlistWorkFragmentViewModel.getTracksOfPlaylist(listOfTracksId)
        }

//      Подписка на получение списка треков
        playlistWorkFragmentViewModel.listOfTracks.observe(viewLifecycleOwner) {
            listOfTracks = it
            fillingRecyclerView(it, playlistId)
        }

//        Подписка на получение общей длительности треков в плейлисте
        playlistWorkFragmentViewModel.totalDuration.observe(viewLifecycleOwner) {
            binding.playlistWorkTotalDuration.text =
                resources.getQuantityString(R.plurals.minutes, it.toInt(), it.toInt())
        }

        playlistWorkFragmentViewModel.deletedTrack.observe(viewLifecycleOwner){
            playlistWorkFragmentViewModel.getPlaylist(playlistId)
            fillingRecyclerView(listOfTracks, playlistId)
        }

        binding.playlistWorkArrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun fillingRecyclerView(listOfTracks: List<Track>, playlistId: Long) {
        playlistWorkAdapter = PlaylistWorkAdapter(listOfTracks, playlistId)
        binding.playlistWorkRecyclerView.adapter = playlistWorkAdapter
        playlistWorkAdapter.notifyItemRangeChanged(0, listOfTracks.lastIndex)
        binding.playlistWorkRecyclerView.visibility = View.VISIBLE
        Log.d("MyLog", "Вызван метод fillingRecyclerView")
    }

    private fun setImage(playlistCover: String?) {
        Glide.with(binding.playlistWorkPlaylistCover)
            .load(playlistCover)
            .placeholder(R.drawable.placeholder_no_cover)
            .into(binding.playlistWorkPlaylistCover)
    }
}