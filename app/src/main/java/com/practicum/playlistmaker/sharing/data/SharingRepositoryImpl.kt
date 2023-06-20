package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.interfaces.SharingRepository

class SharingRepositoryImpl(val context: Context) : SharingRepository {
    override fun sendEmail() {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.email_address)))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.text_mail_subject))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.text_mail_body))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }
    }

    override fun openLink() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.url_offer)))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun shareApp() {
        Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.url_course))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }, null)
    }
}