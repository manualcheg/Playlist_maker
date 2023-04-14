package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

const val THEME_PREFS = "Theme prefs"

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
//        darkThemeCheck(switchDarkTheme)
        switchDarkTheme.isChecked = darkThemeCheck(this)


//        работа switch теперь тут
        switchDarkTheme.setOnCheckedChangeListener{  switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            val sharedPrefs = getSharedPreferences(THEME_PREFS, MODE_PRIVATE)
            sharedPrefs.edit()
                .putBoolean(DARK_THEME, darkTheme)
                .apply()
        }

//      Кнопка "Пользовательское соглашение" с переходом на страницу
        val userAgreementTextView = findViewById<TextView>(R.id.settings_screen_user_agreement_textview)
        userAgreementTextView.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_offer))))
        }

//        Кнопка "Поделится приложением"
        val shareApp = findViewById<TextView>(R.id.settings_screen_shareapp_textview)
        shareApp.setOnClickListener{
            Intent.createChooser(Intent().apply {
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
    companion object{
        fun darkThemeCheck(context: Context):Boolean{
            val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val isNight = when ((currentNightMode)) {
                Configuration.UI_MODE_NIGHT_YES -> true
                else -> false
            }
            return isNight
        }
    }


}