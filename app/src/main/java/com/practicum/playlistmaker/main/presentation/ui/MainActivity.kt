package com.practicum.playlistmaker.main.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.mediateka.presentation.ui.MediaActivity
import com.practicum.playlistmaker.search.presentation.ui.SearchActivity
import com.practicum.playlistmaker.settings.presentation.ui.SettingsActivity

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonSearch.setOnClickListener{
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }

        binding.buttonLibrary.setOnClickListener{
            startActivity(Intent(this, MediaActivity::class.java))
        }

        binding.buttonSettings.setOnClickListener{
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}