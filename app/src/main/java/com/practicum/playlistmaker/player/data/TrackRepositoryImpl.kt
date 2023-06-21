package com.practicum.playlistmaker.player.data

import android.content.SharedPreferences
import android.media.MediaPlayer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.player.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.player.domain.interfaces.MediaPlayerPrepare
import com.practicum.playlistmaker.player.domain.interfaces.TrackRepository
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.utils.Constants

class TrackRepositoryImpl(private val sharedPrefs:SharedPreferences) :
    TrackRepository {

    private var mediaPlayer = MediaPlayer()
    override var playerState = MediaPlayerState.STATE_DEFAULT
    private var currentPositionInMsec: Int = 0
    override fun getTrack(): Track {

        // костыль "[]" - null по умолчанию быть не должно
        val json = sharedPrefs.getString(Constants.SELECTED_TRACKS, "[]")
        val typeToken = object : TypeToken<ArrayList<Track>>() {}.type
        val selectedTracks: ArrayList<Track> = Gson().fromJson(json, typeToken)
        val track = selectedTracks[0]
        return track
    }

    override fun preparePlayer(mediaPlayerPreparator: MediaPlayerPrepare) {
        val url = getTrack().previewUrl
        if (!(url == "" || url == null)) {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
        }

        mediaPlayer.setOnPreparedListener {
            playerState = MediaPlayerState.STATE_PREPARED
            mediaPlayerPreparator.onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            currentPositionInMsec = 0
            playerState = MediaPlayerState.STATE_PREPARED
            mediaPlayerPreparator.onCompletion()
        }
    }

    override fun startPlayer(): MediaPlayerState {
        mediaPlayer.start()
        if (currentPositionInMsec != 0) {
            mediaPlayer.seekTo(currentPositionInMsec)
        }
        playerState = MediaPlayerState.STATE_PLAYING
        return playerState
    }

    override fun pausePlayer(): MediaPlayerState {
        mediaPlayer.pause()
        currentPositionInMsec = mediaPlayer.currentPosition
        playerState = MediaPlayerState.STATE_PAUSED
        return playerState
    }

    override fun playerRelease() {
//        внедрил костыль mediaPlayer = MediaPlayer() из-за ошибки настройки
//        плеера при setDataSource при повороте и последующей фатальной ошибки экрана
//        плюс сохраняю текущий прогресс проигрывания для перестроения экрана при повороте
        currentPositionInMsec = mediaPlayer.currentPosition
        mediaPlayer.release()
        mediaPlayer = MediaPlayer()
    }

    override fun playerGetCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }
}