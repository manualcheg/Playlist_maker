package com.practicum.playlistmaker.player.domain.usecases

import com.practicum.playlistmaker.player.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.player.domain.interfaces.MediaPlayerPrepare
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.player.domain.interfaces.TrackInteractor
import com.practicum.playlistmaker.player.domain.interfaces.TrackRepository

class TrackInteractorImpl(
    private val trackRepository: TrackRepository,
//    override var playerState: MediaPlayerState
) : TrackInteractor {
    override lateinit var playerState: MediaPlayerState

    init {
        playerState = trackRepository.playerState
    }
    override fun playbackControl(): MediaPlayerState {
        when (playerState) {
            MediaPlayerState.STATE_PLAYING -> playerState = pausePlayer()
            MediaPlayerState.STATE_PAUSED, MediaPlayerState.STATE_PREPARED -> playerState =
                startPlayer()
            else -> {}
        }
        return playerState
    }

    override fun getTrack(): Track {
        return trackRepository.getTrack()
    }

    override fun startPlayer(): MediaPlayerState {
        playerState = trackRepository.startPlayer()
        return playerState
    }

    override fun pausePlayer(): MediaPlayerState {
        playerState = trackRepository.pausePlayer()
        return playerState
    }

    override fun preparePlayer(mediaPlayerPreparator: MediaPlayerPrepare){
        trackRepository.preparePlayer(mediaPlayerPreparator)
    }

    override fun playerRelease(){
        trackRepository.playerRelease()
    }

    override fun playerGetCurrentPosition():Int{
        return trackRepository.playerGetCurrentPosition()
    }

/*    override fun putPlayerState(inputPlayerState: MediaPlayerState){
        playerState = inputPlayerState
    }*/
    override fun returnPlayerState():MediaPlayerState{
        playerState = trackRepository.playerState
        return playerState
    }
}