package com.practicum.playlistmaker.mediateka.playlists.presentation.ui


import android.content.Intent
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
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels.PlaylistCreateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

open class PlaylistCreateFragment : Fragment() {
    lateinit var binding: FragmentCreatePlaylistBinding
    var playlistName = ""
    var playlistDescription = ""
    var isImageSet = false
    var imageUri: Uri? = null
    var listOfTrackIds: String = ""
    var countOfTracks: Int = 0
    open val playlistCreateViewModel: PlaylistCreateViewModel by viewModel()

    private val dialogExit by lazy {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
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
    ): View {
        binding = FragmentCreatePlaylistBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    // выдача прав приложению права чтения на uri
                    requireContext().contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    setImage(uri)
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
            playlistCreateViewModel.pickMediaLaunch(pickMedia)
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
            clickCreateButtonFun()
        }
    }

    open fun clickCreateButtonFun() {
        var file: Uri?
        if (isImageSet) {
            playlistCreateViewModel.saveImageToPrivateStorage(
                imageUri!!,
                playlistName,
                requireContext()
            )
        }
        if (imageUri != null) {
            val filePath =
                File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),"playlists")
            file = File(filePath, "$playlistName.jpg").toUri()
        } else {
            file = null
        }

        val playlist = Playlist(
            0,
            playlistName,
            playlistDescription,
            file.toString(),
            listOfTrackIds,
            countOfTracks
        )
        playlistCreateViewModel.putPlaylist(playlist)
        findNavController().popBackStack()
        Toast.makeText(requireContext(), "Плейлист $playlistName создан", Toast.LENGTH_LONG)
            .show()
    }

    private fun setImage(uri: Uri) {
        Glide.with(binding.imageviewAddPlaylistCover)
            .load(uri)
            .placeholder(R.drawable.placeholder_no_cover)
            .centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.dp8)))
            .into(binding.imageviewAddPlaylistCover)
    }

    open fun workWithDialogExit() {
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