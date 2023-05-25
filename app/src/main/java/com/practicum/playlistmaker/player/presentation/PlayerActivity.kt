package com.practicum.playlistmaker.player.presentation

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.player.domain.usecases.TrackInteractorImlp
import java.text.SimpleDateFormat
import java.util.Locale

var mainThreadHandler: Handler = Handler(Looper.getMainLooper())

class PlayerActivity : AppCompatActivity() {
    private lateinit var playerArrowBack: ImageView
    private lateinit var imageCover: ImageView
    private lateinit var trackNameView: TextView
    private lateinit var artistNameView: TextView
    private lateinit var trackTimeView: TextView
    private lateinit var collectionNameView: TextView
    private lateinit var releaseDateView: TextView
    private lateinit var primaryGenreNameView: TextView
    private lateinit var countryView: TextView
    private lateinit var group: Group
    private lateinit var buttonPlay: FloatingActionButton
    private lateinit var playbackTime: TextView

    //    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var url = ""

    //    private val trackIntentDAOImpl = TrackIntentDAOImpl(intent)
//    private val getTrackUseCase = GetTrackUseCase(trackIntentDAOImpl)
//    private val trackRepositoryImpl = TrackRepositoryImpl(trackIntentDAOImpl)
    private val trackRepositoryImpl = TrackRepositoryImpl(intent)
    private val trackInteractorImlp = TrackInteractorImlp(trackRepository = trackRepositoryImpl)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        setViews()

        //получение трека теперь через Data Access Object в data слое
        // через UseCase в слое domain
//        val track = trackRepositoryImpl.getTrack(intent)
        val track = trackInteractorImlp.getTrack()

        Glide.with(imageCover)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder_album_cover)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.dp4)))
            .into(imageCover)
        trackNameView.text = track.trackName
        trackNameView.isSelected = true
        artistNameView.text = track.artistName
        artistNameView.isSelected = true
        trackTimeView.text = track.trackTime?.let {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.toInt())
        }
        collectionNameView.text = track.collectionName?.let {
            group.visibility = View.VISIBLE
            track.collectionName
        }
        releaseDateView.text =
            track.releaseDate?.substring(START_OF_DATA_EXPRESSION, FOUR_NUMBER_OF_YEAR) ?: "-"
        primaryGenreNameView.text = track.primaryGenreName ?: "-"
        countryView.text = track.country ?: "-"

        playerArrowBack.setOnClickListener {
            this.finish()
        }

        url = track.previewUrl

        trackInteractorImlp.preparePlayer()
        prepareViewsAfterPreparePlayer()

        buttonPlay.setOnClickListener {
            playerState = trackInteractorImlp.playbackControl()
            when (playerState) {
                STATE_PLAYING -> {
                    buttonPlay.setImageResource(R.drawable.image_pause_button)
                    mainThreadHandler.post(runPlaybackTime)
                }

                STATE_PAUSED, STATE_PREPARED -> {
                    buttonPlay.setImageResource(R.drawable.play_button)
                    mainThreadHandler.removeCallbacks(runPlaybackTime)
                }
            }
        }
    }

    private fun setViews() {
        playerArrowBack = findViewById(R.id.player_activity_arrow_back)
        imageCover = findViewById(R.id.player_image_cover)
        trackNameView = findViewById(R.id.player_track_name)
        artistNameView = findViewById(R.id.player_artist)
        trackTimeView = findViewById(R.id.player_text_value_tracktime)
        collectionNameView = findViewById(R.id.player_text_value_album)
        releaseDateView = findViewById(R.id.player_text_value_year)
        primaryGenreNameView = findViewById(R.id.player_text_value_genre)
        countryView = findViewById(R.id.player_text_value_country)
        group = findViewById(R.id.player_group_album_visibility)
        buttonPlay = findViewById(R.id.play_pause_button)
        playbackTime = findViewById(R.id.playback_time)
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val PLAYBACK_TIME_RENEW_DELAY_MS = 300L
        private const val START_OF_DATA_EXPRESSION = 0
        private const val FOUR_NUMBER_OF_YEAR = 4
    }

    /*
        private fun preparePlayer() {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState = STATE_PREPARED
            }
            mediaPlayer.setOnCompletionListener {
                playerState = STATE_PREPARED
                mainThreadHandler.removeCallbacks(runPlaybackTime)
            }
        }

        private fun startPlayer() {
            mediaPlayer.start()
            buttonPlay.setImageResource(R.drawable.image_pause_button)
            playerState = STATE_PLAYING
            mainThreadHandler.post(runPlaybackTime)
        }

        private fun pausePlayer() {
            mediaPlayer.pause()
            buttonPlay.setImageResource(R.drawable.play_button)
            playerState = STATE_PAUSED
            mainThreadHandler.removeCallbacks(runPlaybackTime)
        }

        private fun playbackControl() {
            when (playerState) {
                STATE_PLAYING -> pausePlayer()
                STATE_PAUSED, STATE_PREPARED -> startPlayer()
            }
        }
    */

    private fun prepareViewsAfterPreparePlayer() {
        buttonPlay.isEnabled = true
        playbackTime.text = getString(R.string._00_00)
        buttonPlay.setImageResource(R.drawable.play_button)
    }

    override fun onPause() {
        super.onPause()
        trackInteractorImlp.pausePlayer()
        mainThreadHandler.removeCallbacks(runPlaybackTime)
    }

    override fun onDestroy() {
        super.onDestroy()
        trackRepositoryImpl.mediaPlayer.release()
        mainThreadHandler.removeCallbacks(runPlaybackTime)
    }

    private val runPlaybackTime =
        object : Runnable {
            override fun run() {
                playbackTime.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(trackRepositoryImpl.mediaPlayer.currentPosition)
                mainThreadHandler.postDelayed(this, PLAYBACK_TIME_RENEW_DELAY_MS)
            }
        }

}