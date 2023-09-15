package com.practicum.playlistmaker.mediateka.playlists.presentation.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels.PlaylistEditFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistEditFragment : PlaylistCreateFragment() {
    override val playlistCreateViewModel: PlaylistEditFragmentViewModel by viewModel()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistId = requireArguments().getLong("playlistId")
        var oldPlaylist: Playlist? = null
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
            if (isImageSet) {
                val nameOfFile = if (oldPlaylist?.playlistCover.toString()=="null"){
                    playlistName
                } else {
                    (oldPlaylist?.playlistCover.toString().substringAfterLast("/")).substringBefore(".jpg")+"_"
                }
                playlistCreateViewModel.saveImageToPrivateStorage(
                    imageUri!!,
                    nameOfFile,
                    requireContext()
                )
                playlistCreateViewModel.delPlaylistCover(oldPlaylist?.playlistCover.toString())
            } else {
                imageUri = oldPlaylist?.playlistCover?.toUri()
            }

            val playlist = Playlist(
                playlistId,
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

    override fun workWithDialogExit() {
        findNavController().popBackStack()
    }
}