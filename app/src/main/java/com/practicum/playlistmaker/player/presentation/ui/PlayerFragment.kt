package com.practicum.playlistmaker.player.presentation.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.mediateka.playlists.domain.PlaylistsState
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.player.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.player.presentation.PlayerBottomSheetRecycleViewAdapter
import com.practicum.playlistmaker.player.presentation.PlayerViewModel
import com.practicum.playlistmaker.search.domain.entities.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment(), PlayerBottomSheetRecycleViewAdapter.PlaylistClickListener {
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var playbackCurrentTime: String
    private val playerViewModel: PlayerViewModel by viewModel()
    private var playerBottomSheetRecycleViewAdapter =
        PlayerBottomSheetRecycleViewAdapter(ArrayList(), this@PlayerFragment)
    val track by lazy { playerViewModel.getTrack() }
    val bottomSheetContainer by lazy { binding.bottomSheet.root }
    val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workWithBottomSheet()

        observeToResultOfAddToPlaylist()

        playbackCurrentTime = getString(R.string._00_00)

        playerViewModel.onActivityCreate() //  Запрос начального состояния

        observeToInFavouriteLiveData()

        observeToStateLiveData(track)

        observeToCurrentPositionLiveData()

        binding.playerActivityArrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        playerViewModel.preparePlayer(track)

        binding.playPauseButton.setOnClickListener {
            if (track.previewUrl != "") {
                playerViewModel.onPlayButtonClick()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.No_context_text),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.playerButtonLike.setOnClickListener {
            playerViewModel.onFavouriteClicked(track)
        }
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.onActivityPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.onActivityDestroy()
    }

    private fun observeToResultOfAddToPlaylist() {
        playerViewModel.resultOfAddTrack().observe(viewLifecycleOwner) { resultAddTrack ->
            Toast.makeText(
                requireContext(),
                resultAddTrack.message,
                Toast.LENGTH_LONG
            ).show()
            if (resultAddTrack.success) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
            playerViewModel.getPlaylists()
        }
    }

    private fun renderBottomSheet(state: PlaylistsState?) {
        when (state) {
            is PlaylistsState.Empty -> showPlaylistEmpty()
            is PlaylistsState.Content -> showPlaylistContent(state.playlists)
        }
    }

    private fun observeToCurrentPositionLiveData() {
        //    Подписка на изменение текущей позиции проигрывания трека
        playerViewModel.playbackTimeLiveData.observe(viewLifecycleOwner) {
            binding.playbackTime.text = it
            playbackCurrentTime = it ?: getString(R.string._00_00)
        }
    }

    private fun observeToStateLiveData(track: Track) {
        //  Подписка на изменение LiveData состояния плеера из ViewModel в ответ на действия пользователя
        playerViewModel.getPlayerStateLiveData().observe(viewLifecycleOwner) { playerState ->
            render(playerState, track)
        }
    }

    private fun observeToInFavouriteLiveData() {
        playerViewModel.inFavouriteLiveData.observe(viewLifecycleOwner) {
            val heart = if (it) {
                R.drawable.player_button_heart_like_red
            } else {
                R.drawable.player_button_heart_like
            }
            binding.playerButtonLike.setImageResource(heart)
        }
    }

    private fun onPrepared(track: Track) {
        binding.playPauseButton.isEnabled = true
        binding.playPauseButton.visibility = View.VISIBLE
        binding.playbackTime.text = playbackCurrentTime
        //покраска кнопки "избранное" по данным из трека
        binding.playPauseButton.setImageResource(R.drawable.play_button)
        val heart = if (track.inFavourite) {
            R.drawable.player_button_heart_like_red
        } else {
            R.drawable.player_button_heart_like
        }
        binding.playerButtonLike.setImageResource(heart)
    }

    private fun render(playerState: MediaPlayerState, track: Track) {
        when (playerState) {
            MediaPlayerState.STATE_DEFAULT -> {
                showActivity(track)
            }

            MediaPlayerState.STATE_PREPARED -> {
                showActivity(track)
                showPrepared(track)
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
        if (track.releaseDate.equals("")) {
            binding.playerTextValueYear.text = "-"
        } else {
            binding.playerTextValueYear.text =
                track.releaseDate?.substring(START_OF_DATA_EXPRESSION, FOUR_NUMBER_OF_YEAR) ?: "-"
        }

        binding.playerTextValueGenre.text = track.primaryGenreName ?: "-"
        binding.playerTextValueCountry.text = track.country ?: "-"
    }

    private fun showPrepared(track: Track) {
        onPrepared(track)
    }

    private fun showPlaying() {
        binding.playbackTime.visibility = View.VISIBLE
        binding.playPauseButton.setImageResource(R.drawable.image_pause_button)
    }

    private fun showPaused() {
        binding.playbackTime.visibility = View.VISIBLE
        binding.playPauseButton.setImageResource(R.drawable.play_button)
    }

    private fun workWithBottomSheet() {
        binding.bottomSheet.recyclerViewPlaylist.layoutManager =
            LinearLayoutManager(requireContext())
        binding.bottomSheet.recyclerViewPlaylist.adapter = playerBottomSheetRecycleViewAdapter

        playerViewModel.getPlaylists()
        playerViewModel.stateLiveData().observe(viewLifecycleOwner) { state ->
            renderBottomSheet(state)
        }

        binding.bottomSheet.fragmentFavouritesButtonCreatePlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_playlistCreateFragment)
            playerViewModel.onActivityDestroy()  //костыль
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.playerButtonAddToPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun showPlaylistContent(playlists: List<Playlist>) {
        playerBottomSheetRecycleViewAdapter = PlayerBottomSheetRecycleViewAdapter(
            playlists as MutableList<Playlist>,
            this@PlayerFragment
        )
        binding.bottomSheet.recyclerViewPlaylist.adapter = playerBottomSheetRecycleViewAdapter
        playerBottomSheetRecycleViewAdapter.notifyItemRangeChanged(0, playlists.lastIndex)
        binding.bottomSheet.recyclerViewPlaylist.visibility = View.VISIBLE
    }

    private fun showPlaylistEmpty() {
        binding.bottomSheet.recyclerViewPlaylist.visibility = View.GONE
    }

    companion object {
        private const val START_OF_DATA_EXPRESSION = 0
        private const val FOUR_NUMBER_OF_YEAR = 4
    }

    override fun playlistClick(playlist: Playlist) {
        playerViewModel.putTrackToPlaylist(playlist, track.trackId)
    }
}