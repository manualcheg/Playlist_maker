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
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.player.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.player.domain.interfaces.MediaPlayerPrepare
import com.practicum.playlistmaker.player.domain.usecases.TrackInteractorImlp
import com.practicum.playlistmaker.player.presentation.PlayerViewModel
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.search.ui.models.SearchState
import com.practicum.playlistmaker.utils.Constants
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private val binding: ActivityPlayerBinding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    //        var mainThreadHandler: Handler = Handler(Looper.getMainLooper())
//    private lateinit var playerArrowBack: ImageView
//    private lateinit var imageCover: ImageView
//    private lateinit var trackNameView: TextView
    private lateinit var artistNameView: TextView
    private lateinit var trackTimeView: TextView
    private lateinit var collectionNameView: TextView
    private lateinit var releaseDateView: TextView
    private lateinit var primaryGenreNameView: TextView
    private lateinit var countryView: TextView
    private lateinit var group: Group
    private lateinit var buttonPlay: FloatingActionButton
    private lateinit var playbackTime: TextView

    private var playerStateDefault = MediaPlayerState.STATE_DEFAULT
    private val trackRepositoryImpl by lazy { TrackRepositoryImpl(intent) }

    private lateinit var playerViewModel: PlayerViewModel


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /*var trackInteractorImlp =
            TrackInteractorImlp(
                trackRepository = trackRepositoryImpl,
                playerState = playerStateDefault
            )*/

        trackRepositoryImpl //Костыль для lazy - первый вызов для инициализации trackInteractorImpl
//        val track = trackInteractorImlp.getTrack()
        val track = trackRepositoryImpl.getTrack()
        setViews()

//  Создание ViewModel для PlayerActivity
        playerViewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(trackRepositoryImpl)
        )[PlayerViewModel::class.java]

//        Сообщение начального состояния:
        playerViewModel.onCreate()

//  Подписка на изменение LiveData состояния плеера из ViewModel в ответ на действия пользователя
        playerViewModel.getPlayerStateLiveData().observe(this) { playerState ->
            render(playerState, track)
        }

//    Подписка на изменение текущей позиции проигрывания трека
        playerViewModel.playbackTimeLive.observe(this) {
            binding.playbackTime.text = it
        }


        binding.playerActivityArrowBack.setOnClickListener {
            this.finish()
        }

        playerViewModel.preparePlayer()

        buttonPlay.setOnClickListener {
            playerViewModel.onPlayButtonClick()
        }
    }

    private fun setViews() {
//        playerArrowBack = findViewById(R.id.player_activity_arrow_back)
//        imageCover = findViewById(R.id.player_image_cover)
//        trackNameView = findViewById(R.id.player_track_name)
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
//        val trackInteractorImlp = TrackInteractorImlp(trackRepository = trackRepositoryImpl, playerState = playerState)
//        trackInteractorImlp.pausePlayer()
        playerViewModel.onPause()
//        buttonPlay.setImageResource(R.drawable.play_button)
//        mainThreadHandler.removeCallbacks(runPlaybackTime)
    }

    override fun onDestroy() {
        super.onDestroy()
//        mainThreadHandler.removeCallbacks(runPlaybackTime)
//        trackRepositoryImpl.playerRelease()
//        заменил на:
        playerViewModel.onDestroy()
    }

    fun onPrepared() {
//        mainThreadHandler.removeCallbacks(runPlaybackTime)
        buttonPlay.isEnabled = true
        buttonPlay.visibility = View.VISIBLE
        playbackTime.text = getString(R.string._00_00)
        buttonPlay.setImageResource(R.drawable.play_button)
    }

    fun onCompletion() {
//        mainThreadHandler.removeCallbacks(Constants.PLAYER_TIMER_TOKEN)
        playbackTime.text = getString(R.string._00_00)
        buttonPlay.setImageResource(R.drawable.play_button)
    }


    fun render(playerState: MediaPlayerState, track: Track) {
        when (playerState) {
            MediaPlayerState.STATE_DEFAULT -> {
                showActivity(track)
            }

            MediaPlayerState.STATE_PREPARED -> {
                showPrepared()
            }

            MediaPlayerState.STATE_PLAYING -> {
                showPlaying()
            }

            MediaPlayerState.STATE_PAUSED -> {
                showPaused()
            }
        }
    }

    private fun showActivity(track: Track) {
        //        Glide.with(imageCover)
        Glide.with(binding.playerImageCover)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder_album_cover)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.dp4)))
            .into(binding.playerImageCover)
//        trackNameView.text = track.trackName
        binding.playerTrackName.text = track.trackName
        binding.playerTrackName.isSelected = true
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

    }

    private fun showPrepared() {
        onPrepared()
    }

    private fun showPlaying() {
        playbackTime.visibility = View.VISIBLE
        buttonPlay.setImageResource(R.drawable.image_pause_button)
    }

    private fun showPaused() {
        playbackTime.visibility = View.VISIBLE
        buttonPlay.setImageResource(R.drawable.play_button)
    }

    companion object {
        private const val START_OF_DATA_EXPRESSION = 0
        private const val FOUR_NUMBER_OF_YEAR = 4
    }
}
