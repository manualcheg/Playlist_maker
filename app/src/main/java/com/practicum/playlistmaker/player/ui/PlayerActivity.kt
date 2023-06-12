package com.practicum.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.player.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.player.domain.interfaces.MediaPlayerPrepare
import com.practicum.playlistmaker.player.domain.usecases.TrackInteractorImlp
import com.practicum.playlistmaker.player.presentation.PlayerViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity(), MediaPlayerPrepare {
//    var mainThreadHandler: Handler = Handler(Looper.getMainLooper())
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

    private var playerState = MediaPlayerState.STATE_DEFAULT
    private val trackRepositoryImpl by lazy { TrackRepositoryImpl(intent) }

    private lateinit var playerViewModel: PlayerViewModel


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        var trackInteractorImlp = TrackInteractorImlp(trackRepository = trackRepositoryImpl, playerState = playerState)

        trackRepositoryImpl //Костыль для lazy - первый вызов для инициализации trackInteractorImpl
        val track = trackInteractorImlp.getTrack()
        setViews()

        playerViewModel = ViewModelProvider(this)[PlayerViewModel::class.java]

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

        trackRepositoryImpl.preparePlayer(this)

        buttonPlay.setOnClickListener {
            playerState = trackRepositoryImpl.playerState
            trackInteractorImlp = TrackInteractorImlp(
                trackRepository = trackRepositoryImpl,
                playerState = playerState
            )
            playerState = trackInteractorImlp.playbackControl()

            when (playerState) {
                MediaPlayerState.STATE_PLAYING -> {
                    playbackTime.visibility = View.VISIBLE
                    buttonPlay.setImageResource(R.drawable.image_pause_button)
                    mainThreadHandler.post(runPlaybackTime)
                }

                MediaPlayerState.STATE_PAUSED, MediaPlayerState.STATE_PREPARED -> {
                    playbackTime.visibility = View.VISIBLE
                    mainThreadHandler.removeCallbacks(runPlaybackTime)
                    buttonPlay.setImageResource(R.drawable.play_button)
                }

                else -> {}
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

    override fun onPause() {
        super.onPause()
        /*val trackInteractorImlp = TrackInteractorImlp(trackRepository = trackRepositoryImpl, playerState = playerState)
        trackInteractorImlp.pausePlayer()*/
        playerViewModel.onPause()
        buttonPlay.setImageResource(R.drawable.play_button)
        mainThreadHandler.removeCallbacks(runPlaybackTime)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler.removeCallbacks(runPlaybackTime)
        trackRepositoryImpl.playerRelease()
    }

    override fun onPrepared() {
//        mainThreadHandler.removeCallbacks(runPlaybackTime)
        buttonPlay.isEnabled = true
        buttonPlay.visibility = View.VISIBLE
        playbackTime.text = getString(R.string._00_00)
        buttonPlay.setImageResource(R.drawable.play_button)
    }

    override fun onCompletion() {
        mainThreadHandler.removeCallbacks(runPlaybackTime)
        playbackTime.text = getString(R.string._00_00)
        buttonPlay.setImageResource(R.drawable.play_button)
    }

    /*private val runPlaybackTime =
        object : Runnable {
            override fun run() {
                playbackTime.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(trackRepositoryImpl.playerGetCurrentPosition())
                mainThreadHandler.postDelayed(this, PLAYBACK_TIME_RENEW_DELAY_MS)
            }
        }*/

    companion object {
        private const val PLAYBACK_TIME_RENEW_DELAY_MS = 300L
        private const val START_OF_DATA_EXPRESSION = 0
        private const val FOUR_NUMBER_OF_YEAR = 4
    }
}
