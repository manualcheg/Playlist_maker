package com.practicum.playlistmaker.main.data

import android.content.Context
import com.practicum.playlistmaker.main.domain.MainRepository

class MainRepositoryImpl(val context: Context):MainRepository {
    override fun openSearch() {
        context.applicationContext
    }

    override fun openMediateka() {
        TODO("Not yet implemented")
    }

    override fun openSettings() {
        TODO("Not yet implemented")
    }
}