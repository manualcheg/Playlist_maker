package com.practicum.playlistmaker.player.data.repository

import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.data.intent.TrackIntentDAOImpl
import com.practicum.playlistmaker.player.domain.entities.Track
import com.practicum.playlistmaker.player.domain.interfaces.MediaPlayerPrepare
import com.practicum.playlistmaker.player.domain.interfaces.TrackRepository
import com.practicum.playlistmaker.player.presentation.PlayerActivity
import com.practicum.playlistmaker.player.presentation.mainThreadHandler
import java.text.SimpleDateFormat
import java.util.Locale

class TrackRepositoryImpl(private val intent: Intent, val mediaPlayer: MediaPlayer, val activity: PlayerActivity) :
    TrackRepository {
    //    var mediaPlayer = MediaPlayer()
    var playerState = STATE_DEFAULT
    override fun getTrack(): Track {
        val trackIntentDAOImpl = TrackIntentDAOImpl(intent)
        return trackIntentDAOImpl.getTrack()
    }

    override fun preparePlayer(mediaPlayerPreparator:MediaPlayerPrepare) {
        val url = getTrack().previewUrl
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            mediaPlayerPreparator.onPrepared()
            Log.d("myLog", "hello from OnPreparedListener. State is $playerState")
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            mediaPlayerPreparator.onCompletion()
            Log.d("myLog", "hello from OnCompletionListener. State is $playerState")
//            mainThreadHandler.removeCallbacks(runPlaybackTime)
        }
        Log.d("myLog", "hello from preparePlayer() after OnPreparedListener. State is $playerState")
    }

    override fun startPlayer(): Int {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        Log.d("myLog", "hello from startPlayer(). State is $playerState")
        return playerState
    }

    override fun pausePlayer(): Int {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        Log.d("myLog", "hello from startPlayer(). State is $playerState")
        return playerState
    }

//    override fun setOnCompletionListener(onCompletion: () -> Unit){
    /*override fun setOnCompletionListener{
    mediaPlayer.setOnCompletionListener{
            playerState = STATE_PREPARED
        }
    }
*/
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val PLAYBACK_TIME_RENEW_DELAY_MS = 300L
    }
}