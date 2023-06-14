package com.practicum.playlistmaker.player.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.player.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.player.presentation.PlayerViewModel
import com.practicum.playlistmaker.search.domain.entities.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private val binding: ActivityPlayerBinding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

//    private val trackRepositoryImpl by lazy { TrackRepositoryImpl(intent) }
    private lateinit var playbackCurrentTime: String
    private lateinit var playerViewModel: PlayerViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        playbackCurrentTime = getString(R.string._00_00)

//        trackRepositoryImpl  //  Костыль для lazy - первый вызов для инициализации trackInteractorImpl

        createPlayerViewModel()

        val track = playerViewModel.getTrack()

        playerViewModel.onActivityCreate() //  Запрос начального состояния

        observeToStateLiveData(track)

        observeToCurrentPositionLiveData()

        binding.playerActivityArrowBack.setOnClickListener {
            this.finish()
        }

        playerViewModel.preparePlayer()

        binding.playPauseButton.setOnClickListener {
            playerViewModel.onPlayButtonClick()
        }
    }

    private fun observeToCurrentPositionLiveData() {
        //    Подписка на изменение текущей позиции проигрывания трека
        playerViewModel.playbackTimeLive.observe(this) {
            binding.playbackTime.text = it
            playbackCurrentTime = it ?: getString(R.string._00_00)
        }
    }

    private fun observeToStateLiveData(track: Track) {
        //  Подписка на изменение LiveData состояния плеера из ViewModel в ответ на действия пользователя
        playerViewModel.getPlayerStateLiveData().observe(this) { playerState ->
            render(playerState, track)
        }
    }

    private fun createPlayerViewModel() {
        //  Создание ViewModel для PlayerActivity
        playerViewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(intent)
        )[PlayerViewModel::class.java]
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.onActivityPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.onActivityDestroy()
    }

    private fun onPrepared() {
        binding.playPauseButton.isEnabled = true
        binding.playPauseButton.visibility = View.VISIBLE
        binding.playbackTime.text = playbackCurrentTime
        binding.playPauseButton.setImageResource(R.drawable.play_button)
    }

    private fun render(playerState: MediaPlayerState, track: Track) {
        when (playerState) {
            MediaPlayerState.STATE_DEFAULT -> {
                showActivity(track)
            }

            MediaPlayerState.STATE_PREPARED -> {
                showActivity(track)
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
        Glide.with(binding.playerImageCover)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder_album_cover)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.dp4)))
            .into(binding.playerImageCover)
        binding.playerTrackName.text = track.trackName
        binding.playerTrackName.isSelected = true
        binding.playerArtist.text = track.artistName
        binding.playerArtist.isSelected = true
        binding.playerTextValueTracktime.text = track.trackTime?.let {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.toInt())
        }
        binding.playerTextValueAlbum.text = track.collectionName?.let {
            binding.playerGroupAlbumVisibility.visibility = View.VISIBLE
            track.collectionName
        }
        binding.playerTextValueYear.text =
            track.releaseDate?.substring(START_OF_DATA_EXPRESSION, FOUR_NUMBER_OF_YEAR) ?: "-"
        binding.playerTextValueGenre.text = track.primaryGenreName ?: "-"
        binding.playerTextValueCountry.text = track.country ?: "-"
    }

    private fun showPrepared() {
        onPrepared()
    }

    private fun showPlaying() {
        binding.playbackTime.visibility = View.VISIBLE
        binding.playPauseButton.setImageResource(R.drawable.image_pause_button)
    }

    private fun showPaused() {
        binding.playbackTime.visibility = View.VISIBLE
        binding.playPauseButton.setImageResource(R.drawable.play_button)
    }

    companion object {
        private const val START_OF_DATA_EXPRESSION = 0
        private const val FOUR_NUMBER_OF_YEAR = 4
    }
}