package com.practicum.playlistmaker.player.data.repository

import android.content.Intent
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.data.intent.TrackIntentDAOImpl
import com.practicum.playlistmaker.player.domain.entities.Track
import com.practicum.playlistmaker.player.domain.interfaces.TrackRepository
import com.practicum.playlistmaker.player.presentation.mainThreadHandler
import java.text.SimpleDateFormat
import java.util.Locale

class TrackRepositoryImpl(val intent:Intent):TrackRepository {

    var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
//    private var url = ""

    override fun getTrack(): Track {
        return Gson().fromJson(intent.getStringExtra("track"), Track::class.java)
    }

    override fun preparePlayer():Int {
        val url = getTrack().previewUrl
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
//            buttonPlay.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
//            mainThreadHandler.removeCallbacks(runPlaybackTime)
//            playbackTime.text = getString(R.string._00_00)
//            buttonPlay.setImageResource(R.drawable.play_button)
        }
        return playerState
    }

    override fun startPlayer():Int {
        mediaPlayer.start()

//        mainThreadHandler.post(runPlaybackTime)
        playerState = STATE_PLAYING
        return playerState
    }

    override fun pausePlayer():Int {
        mediaPlayer.pause()
//        mainThreadHandler.removeCallbacks(runPlaybackTime)
        playerState = STATE_PAUSED
        return playerState
    }

/*
    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PAUSED, STATE_PREPARED -> startPlayer()
        }
    }
*/

 /*   fun onPause() {
        super.onPause()
        pausePlayer()
        mainThreadHandler.removeCallbacks(runPlaybackTime)
    }

     fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainThreadHandler.removeCallbacks(runPlaybackTime)
    }
*/
  /*  private val runPlaybackTime =
        object : Runnable {
            override fun run() {
                playbackTime.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(mediaPlayer.currentPosition)
                mainThreadHandler.postDelayed(this, PLAYBACK_TIME_RENEW_DELAY_MS)
            }
        }*/


    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val PLAYBACK_TIME_RENEW_DELAY_MS = 300L
    }
}