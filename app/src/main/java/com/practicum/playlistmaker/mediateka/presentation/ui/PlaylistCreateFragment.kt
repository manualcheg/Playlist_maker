package com.practicum.playlistmaker.mediateka.presentation.ui

import android.content.Intent
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
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylistBinding
import java.io.File
import java.io.FileOutputStream

class PlaylistCreateFragment() : Fragment() {
    private lateinit var binding: FragmentCreatePlaylistBinding
    private var playlistName = ""
    private var playlistDescription = ""
    private var isImageSet = false
    private var imageUri: Uri? = null

    val dialogExit by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { dialog, which -> }
            .setPositiveButton("Завершить") { dialog, which ->
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
                    binding.imageviewAddPlaylistCover.setImageURI(uri)
                    imageUri = uri
                    isImageSet = true
                } else {
                    Log.d("PhotoPicker", "No media selected")
                    isImageSet = false
                }
            }

        /*val dialogExit = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { dialog, which -> }
            .setPositiveButton("Завершить") { dialog, which ->
                findNavController().popBackStack()
            }.create()
*/
        binding.imageviewAddPlaylistCover.setOnClickListener {
            Toast.makeText(requireContext(), "Нажали на картинку", Toast.LENGTH_LONG).show()
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.searchActivityToolbar.setNavigationOnClickListener {
            workWithDialogExit()
            /*            if (!(playlistName == "") ||
                    !playlistDescription.equals("") ||
                    (isImageSet)
                ) {
                    dialogExit.show()
                } else {
                    findNavController().popBackStack()
                }*/
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
            if (!isImageSet) {
                imageUri = Uri.parse(R.drawable.placeholder_no_cover.toString())
            }
            saveImageToPrivateStorage(imageUri!!)

            findNavController().popBackStack()
            Toast.makeText(requireContext(), "Плейлист ${playlistName} создан", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "first_cover.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun workWithDialogExit() {
        if (playlistName != "" ||
            playlistDescription != "" ||
            isImageSet
        ) {
            dialogExit.show()
        } else {
            findNavController().popBackStack()
        }
    }
}