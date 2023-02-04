package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val arrowSettingsBack = findViewById<ImageView>(R.id.settings_arrow_back_like_button)
        arrowSettingsBack.setOnClickListener{
                this.finish()
        }
    }
}