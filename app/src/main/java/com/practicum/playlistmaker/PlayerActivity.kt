package com.practicum.playlistmaker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        imageCover = findViewById(R.id.player_image_cover)
        trackNameView = findViewById(R.id.player_track_name)
        artistNameView = findViewById(R.id.player_artist)
        trackTimeView = findViewById(R.id.player_text_value_tracktime)
        collectionNameView = findViewById(R.id.player_text_value_album)
        releaseDateView = findViewById(R.id.player_text_value_year)
        primaryGenreNameView = findViewById(R.id.player_text_value_genre)
        countryView = findViewById(R.id.player_text_value_country)
        group = findViewById(R.id.player_group_album_visibility)

        val intent = intent
        val trackName = intent.getStringExtra("trackName")
        val artistName = intent.getStringExtra("artistName")
        val trackTime = intent.getStringExtra("trackTime")
        val artworkUrl500 = intent.getStringExtra("artworkUrl500")
        val collectionName = intent.getStringExtra("collectionName")
        val releaseDate = intent.getStringExtra("releaseDate")
        val primaryGenreName = intent.getStringExtra("primaryGenreName")
        val country = intent.getStringExtra("country")


        Glide.with(imageCover)
            .load(artworkUrl500)
            .placeholder(R.drawable.placeholder_album_cover)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.dp4)))
            .into(imageCover)
        trackNameView.text = trackName
        trackNameView.isSelected = true
        artistNameView.text = artistName
        artistNameView.isSelected = true
        trackTimeView.text = trackTime?.let {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.toInt())
        }

        collectionNameView.text = collectionName?.let {
            group.visibility = View.VISIBLE
            collectionName
        }

        releaseDateView.text = releaseDate?.substring(0, 4) ?: "-"
        primaryGenreNameView.text = primaryGenreName ?: "-"
        countryView.text = country ?: "-"

        playerArrowBack = findViewById(R.id.player_activity_arrow_back)
        playerArrowBack.setOnClickListener {
            this.finish()
        }

    }
}