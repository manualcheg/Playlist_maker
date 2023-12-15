package com.practicum.playlistmaker.mediateka.playlists.presentation.ui

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels.PlaylistEditFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistEditFragment : PlaylistCreateFragment() {
    override val playlistCreateViewModel: PlaylistEditFragmentViewModel by viewModel()
    var oldPlaylist: Playlist? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistId = requireArguments().getLong("playlistId")
        playlistCreateViewModel.getPlaylist(playlistId)

        playlistCreateViewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            oldPlaylist = playlist
            binding.edittextPlaylistName.setText(playlist.playlistName)
            binding.edittextPlaylistDescription.setText(playlist.playlistDescription)
            Glide.with(binding.imageviewAddPlaylistCover)
                .load(playlist.playlistCover)
                .placeholder(R.drawable.placeholder_no_cover)
                .into(binding.imageviewAddPlaylistCover)

            binding.fragmentCreateTextviewTitle.text =
                getString(R.string.playlist_edit_fragment_text_title)
            binding.textViewCreatePlaylistButton.text =
                getString(R.string.playlist_edit_fragment_text_button_save)
        }

        binding.textViewCreatePlaylistButton.setOnClickListener {
            clickCreateButtonFun()
        }
    }

    override fun workWithDialogExit() {
        findNavController().popBackStack()
    }

    override fun clickCreateButtonFun() {
        if (isImageSet) {
            val newNameOfFile = if (oldPlaylist?.playlistCover.toString() == "null") {
                playlistName
            } else {
                (oldPlaylist?.playlistCover.toString()
                    .substringAfterLast("/")).substringBefore(".jpg") + "_"
            }
            playlistCreateViewModel.saveImageToPrivateStorage(
                imageUri!!,
                newNameOfFile,
                requireContext()
            )

            val filePath =
                File(
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "playlists"
                )
            imageUri = File(filePath, "$newNameOfFile.jpg").toUri()

            playlistCreateViewModel.delPlaylistCover(oldPlaylist?.playlistCover.toString())
        } else {
            if (oldPlaylist?.playlistCover != null) {
                imageUri = oldPlaylist?.playlistCover?.toUri()
            } else {
                imageUri = null
            }

        }

        val playlist = Playlist(
            oldPlaylist?.playlistId ?: 0,
            playlistName,
            playlistDescription,
            imageUri.toString(),
            oldPlaylist!!.listOfTracksId,
            oldPlaylist!!.countOfTracks
        )
        playlistCreateViewModel.putPlaylist(playlist)
        findNavController().popBackStack()
    }
}