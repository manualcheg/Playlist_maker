package com.practicum.playlistmaker.mediateka.playlists.presentation.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels.PlaylistCreateFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class PlaylistCreateFragment : Fragment() {
    private lateinit var binding: FragmentCreatePlaylistBinding
    private var playlistName = ""
    private var playlistDescription = ""
    private var isImageSet = false
    private var imageUri: Uri? = null
    private var listOfTrackIds: String = ""
    private var countOfTracks: Int = 0
    private val playlistCreateFragmentViewModel: PlaylistCreateFragmentViewModel by viewModel()

    private val dialogExit by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialogExitTitle))
            .setMessage(getString(R.string.dialogExitMessage))
            .setNeutralButton(getString(R.string.dialogExitCancelButton)) { _, _ -> }
            .setPositiveButton(getString(R.string.dialogExitAgreeButton)) { _, _ ->
                findNavController().popBackStack()
            }.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePlaylistBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    setImage(uri)
//                    binding.imageviewAddPlaylistCover.setImageURI(uri)
                    imageUri = uri
                    isImageSet = true
                } else {
                    Log.d(getString(R.string.photopicker), getString(R.string.no_media_selected))
                    isImageSet = false
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, //привязка колбека диспетчера к этому фрагменту
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    workWithDialogExit()
                }
            })

        binding.imageviewAddPlaylistCover.setOnClickListener {
            playlistCreateFragmentViewModel.pickMediaLaunch(pickMedia)
//            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.searchActivityToolbar.setNavigationOnClickListener {
            workWithDialogExit()
        }

        binding.edittextPlaylistName.doOnTextChanged { input, _, _, _ ->
            // управление доступностью кнопки "Добавить"
            binding.textViewCreatePlaylistButton.isEnabled = input?.isEmpty() != true
            playlistName = input.toString()
        }

        binding.edittextPlaylistDescription.doOnTextChanged { input, _, _, _ ->
            playlistDescription = input.toString()
        }

        binding.textViewCreatePlaylistButton.setOnClickListener {
            if (isImageSet) {
                saveImageToPrivateStorage(imageUri!!)
            }
            val playlist = Playlist(
                0,
                playlistName,
                playlistDescription,
                imageUri.toString(),
                listOfTrackIds,
                countOfTracks
            )
            playlistCreateFragmentViewModel.putPlaylist(playlist)
            findNavController().popBackStack()
            Toast.makeText(requireContext(), "Плейлист $playlistName создан", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun setImage(uri: Uri) {
        Glide.with(binding.imageviewAddPlaylistCover)
            .load(uri)
            .placeholder(R.drawable.placeholder_no_cover)
            .centerCrop()
            .transform(RoundedCorners(this@PlaylistCreateFragment.resources.getDimensionPixelSize(R.dimen.dp8)))
            .into(binding.imageviewAddPlaylistCover)
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlists")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "$playlistName.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun workWithDialogExit() {
        if (
            playlistName != "" ||
            playlistDescription != "" ||
            isImageSet
        ) {
            dialogExit.show()
        } else {
            findNavController().popBackStack()
        }
    }
}