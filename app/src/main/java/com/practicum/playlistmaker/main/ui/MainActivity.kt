package com.practicum.playlistmaker.main.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.mediateka.ui.MediaActivity
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.settings.SettingsActivity

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

        val buttonSearch = findViewById<Button>(R.id.button_search).apply {
            setOnClickListener {
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }
        }

        binding.buttonLibrary.setOnClickListener{
            startActivity(Intent(this, MediaActivity::class.java))
        }

        binding.buttonSettings.setOnClickListener{
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}