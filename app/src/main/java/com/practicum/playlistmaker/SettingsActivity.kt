package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val arrowSettingsBack = findViewById<ImageView>(R.id.settings_screen_arrow_back_like_button)
        arrowSettingsBack.setOnClickListener{
                this.finish()
        }

        val switchDarkTheme = findViewById<SwitchCompat>(R.id.switch_dark_theme)
//        Проверка на включенность темной темы и переключение switch
        darkThemeCheck(switchDarkTheme)

//        работа switch
        switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

//      Кнопка "Пользовательское соглашение" с переходом на страницу
        val userAgreementTextView = findViewById<TextView>(R.id.settings_screen_user_agreement_textview)
        userAgreementTextView.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_offer))))
        }

//        Кнопка "Поделится приложением"
        val shareApp = findViewById<TextView>(R.id.settings_screen_shareapp_textview)
        shareApp.setOnClickListener{
            val shareIntent = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.url_course))
                startActivity(this)
            }, null)
        }

//        Кнопка "Написать в поддержку"
        val emailToSupport = findViewById<TextView>(R.id.settings_screen_send_mail_support_textview)
        emailToSupport.setOnClickListener{
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_address)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.text_mail_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.text_mail_body))
                startActivity(this)
            }
        }
    }

//     Метод, который проверяет включенность тёмной темы
    fun darkThemeCheck(switch:SwitchCompat){
        /*if (resources.configuration.isNightModeActive){ //требует api level 30
            switch.isChecked = true
        }*/
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when ((currentNightMode)) {
            Configuration.UI_MODE_NIGHT_YES -> switch.isChecked=true
            Configuration.UI_MODE_NIGHT_NO -> switch.isChecked=false
        }
    }
}