package com.practicum.playlistmaker

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val arrowSettingsBack = findViewById<ImageView>(R.id.settings_screen_arrow_back_like_button)
        arrowSettingsBack.setOnClickListener{
                this.finish()
        }

        val switch_night_theme = findViewById<Switch>(R.id.switch1)
//        Попытка проверки на включенность темной темы
/*        if (getResources().getConfiguration().isNightModeActive){
            switch_night_theme.isChecked
        }*/

        when ((resources.configuration.uiMode)) {
            Configuration.UI_MODE_NIGHT_YES -> switch_night_theme.isChecked
            Configuration.UI_MODE_NIGHT_NO -> return
        }
        switch_night_theme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

//      Кнопка "Пользовательское соглашение" с переходом на страницу
        val userAgreementTextView = findViewById<TextView>(R.id.settings_screen_user_agreement_textview)
        userAgreementTextView.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/")))
        }

//        Кнопка "Поделится приложением"
        val shareApp = findViewById<TextView>(R.id.settings_screen_shareapp_textview)
        shareApp.setOnClickListener{
            val shareIntent = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/android-developer/")
            }, null)
            startActivity(shareIntent)
        }

//        Кнопка "Написать в поддержку"
        val emailToSupport = findViewById<TextView>(R.id.settings_screen_send_mail_support_textview)
        emailToSupport.setOnClickListener{
            val sendEmail = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("manualcheg@yandex.ru"))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.text_mail_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.text_mail_body)) }
            startActivity(sendEmail)
        }
    }

    //попытка создания метода, который проверяет включенность тёмной темы
    @RequiresApi(Build.VERSION_CODES.R)
    fun nightThemeCheck(switch:Switch){
        if (getResources().getConfiguration().isNightModeActive){
            switch.isChecked
        }
    }
}