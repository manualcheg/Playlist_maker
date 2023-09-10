package com.practicum.playlistmaker.mediateka.playlists.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistWorkBinding

class PlaylistWorkFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistWorkBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistWorkBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(binding.playlistWorkPlaylistCover)
            .load("https://ya.ru/img.jpg")
            .placeholder(R.drawable.placeholder_no_cover)
            .centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.dp8)))
            .into(binding.playlistWorkPlaylistCover)

//        binding.playlistWorkPlaylistCover = pla
        binding.playlistWorkArrowBack.setOnClickListener{
            findNavController().popBackStack()
        }
    }
}