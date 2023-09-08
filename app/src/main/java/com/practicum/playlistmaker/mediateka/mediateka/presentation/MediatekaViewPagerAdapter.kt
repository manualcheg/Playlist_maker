package com.practicum.playlistmaker.mediateka.mediateka.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.mediateka.favourites.presentation.ui.FavouritesFragment
import com.practicum.playlistmaker.mediateka.playlists.presentation.ui.PlaylistsFragment

class MediatekaViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavouritesFragment()
            else -> PlaylistsFragment()
        }
    }
}