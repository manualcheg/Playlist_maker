package com.practicum.playlistmaker.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.mediateka.MediaActivity
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch: View = findViewById<Button>(R.id.button_search)
//        Способ 1: анонимный класс
        val buttonSearchClickListener: View.OnClickListener = View.OnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }
        buttonSearch.setOnClickListener(buttonSearchClickListener)

//        Способ 2: лямбда-выражение
        val buttonLibrary: View = findViewById(R.id.button_library)
        buttonLibrary.setOnClickListener {
//            Toast.makeText(this@MainActivity, "Нажали на кнопку медиатеки", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MediaActivity::class.java))
        }

//        Способ 1: анонимный класс
        val buttonSettings: View = findViewById(R.id.button_settings)
        val buttonSettingsClicklistener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
//                Toast.makeText(this@MainActivity, "Нажали на кнопку настроек", Toast.LENGTH_SHORT).show()
                val settingsActivity = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(settingsActivity)
            }
        }
        buttonSettings.setOnClickListener(buttonSettingsClicklistener)
    }
}