package com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.domain.interfaces.PlaylistDBInteractor
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

open class PlaylistCreateViewModel(private val playlistDBInteractor: PlaylistDBInteractor):ViewModel() {

    private var _uri = MutableLiveData<Uri>()
    var uri : LiveData<Uri> = _uri

    fun putPlaylist(playlist: Playlist){
        viewModelScope.launch {
            playlistDBInteractor.putPlaylist(playlist)
        }
    }

    fun pickMediaLaunch(pickMedia: ActivityResultLauncher<PickVisualMediaRequest>) {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    fun saveImageToPrivateStorage(uri: Uri, playlistName:String, context: Context) {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlists")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "$playlistName.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        _uri.postValue(file.toUri())
    }
}