package com.practicum.playlistmaker.player.domain.entities

sealed class MediaPlayerState2(val isPlayButtonenabled:Boolean) {
    class Default: MediaPlayerState2(false)
    class Prepared: MediaPlayerState2(true)
    class Playing: MediaPlayerState2(true)
    class Paused: MediaPlayerState2(true)
}