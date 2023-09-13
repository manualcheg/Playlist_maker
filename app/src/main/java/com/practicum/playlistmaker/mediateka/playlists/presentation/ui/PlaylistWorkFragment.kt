package com.practicum.playlistmaker.mediateka.playlists.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistWorkBinding
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.presentation.PlaylistWorkAdapter
import com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels.PlaylistWorkFragmentViewModel
import com.practicum.playlistmaker.search.domain.entities.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistWorkFragment : Fragment(), PlaylistWorkAdapter.LongClickListener {
    private lateinit var binding: FragmentPlaylistWorkBinding
    private val playlistWorkFragmentViewModel: PlaylistWorkFragmentViewModel by viewModel()
    private var playlistFromViewModule: Playlist? = null
    private var listOfTracksId = listOf<String>()
    private var listOfTracks = listOf<Track>()
    private var playlistId: Long = 0
    private var playlistWorkAdapter = PlaylistWorkAdapter(listOfTracks, this)

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
        playlistId = requireArguments().getLong("playlistId")
//        Запрос плейлиста
        playlistWorkFragmentViewModel.getPlaylist(playlistId)

//      Подписка на получение плейлиста
        playlistWorkFragmentViewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            playlistFromViewModule = playlist

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
            listOfTracksId =
                java.util.ArrayList(playlistFromViewModule?.listOfTracksId?.split(",")!!)
            playlistWorkFragmentViewModel.getTracksOfPlaylist(listOfTracksId)
        }

//      Подписка на получение списка треков
        playlistWorkFragmentViewModel.listOfTracks.observe(viewLifecycleOwner) {
            listOfTracks = it
            fillingRecyclerView(it)
        }

//        Подписка на получение общей длительности треков в плейлисте
        playlistWorkFragmentViewModel.totalDuration.observe(viewLifecycleOwner) {
            binding.playlistWorkTotalDuration.text =
                resources.getQuantityString(R.plurals.minutes, it.toInt(), it.toInt())
        }

        binding.playlistWorkArrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val customToast = LayoutInflater.from(requireContext()).inflate(
            R.layout.custom_toast,
            requireActivity().findViewById(R.id.custom_toast_layout_id)
        )

        binding.playlistWorkShareButton.setOnClickListener {
            // можно вынести во ViewModel?
            if (playlistFromViewModule?.listOfTracksId == "") {
                customToast.findViewById<TextView>(R.id.text).text =
                    "В этом плейлисте нет списка треков, которым можно поделиться"
                val toast = Toast.makeText(
                    requireContext(),
                    "В этом плейлисте нет списка треков, которым можно поделиться",
                    Toast.LENGTH_SHORT
                )
                toast.setView(customToast)
                toast.show()
            } else {
                var plainTextTracks = ""
                listOfTracks.forEachIndexed { index, track ->
                    plainTextTracks += "\n${index+1}. ${track.artistName} - ${track.trackName} (${
                        SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(track.trackTime?.toInt())
                    })"
                }
                val countOfTracksInt = playlistFromViewModule?.countOfTracks!!
                val countOfTrackWithWord = resources.getQuantityString(
                    R.plurals.tracks,
                    countOfTracksInt,
                    countOfTracksInt
                )
                Intent.createChooser(Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "${playlistFromViewModule?.playlistName}\n" +
                                "${playlistFromViewModule?.playlistDescription}\n" +
                                countOfTrackWithWord +
                                plainTextTracks
                    )
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    requireContext().startActivity(this)
                }, "Share APK")
            }

            binding.playlistWorkMenuButton?.setOnClickListener{
                Toast.makeText(requireContext(),"Нажали на меню",Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun fillingRecyclerView(listOfTracks: List<Track>) {
        playlistWorkAdapter = PlaylistWorkAdapter(listOfTracks, this)
        binding.playlistWorkRecyclerView.adapter = playlistWorkAdapter
        playlistWorkAdapter.notifyItemRangeChanged(0, listOfTracks.lastIndex)
        binding.playlistWorkRecyclerView.visibility = View.VISIBLE
    }

    private fun setImage(playlistCover: String?) {
        Glide.with(binding.playlistWorkPlaylistCover)
            .load(playlistCover)
            .placeholder(R.drawable.placeholder_no_cover)
            .into(binding.playlistWorkPlaylistCover)
    }

    override fun trackInPlaylistClick(currentTrackId: String) {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(requireContext().getString(R.string.playlist_work_fragment_dialog_text_deltrack))
            .setMessage(requireContext().getString(R.string.playlist_work_fragment_dialog_text_areyousure))
            .setPositiveButton(
                requireContext().getString(R.string.playlist_work_fragment_dialog_text_cancel),
                null
            )
            .setNegativeButton(requireContext().getString(R.string.playlist_work_fragment_dialog_text_delete)) { _, _ ->
                playlistWorkFragmentViewModel.delTrack(currentTrackId, playlistId)
            }.show()
    }
}