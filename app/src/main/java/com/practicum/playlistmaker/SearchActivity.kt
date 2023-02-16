package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val editTextSearchActivity = findViewById<EditText>(R.id.search_activity_edittext)
        val searchClearEdittextImageview =
            findViewById<ImageView>(R.id.search_clear_edittext_imageview)
        val settingsArrowBack = findViewById<androidx.appcompat.widget.Toolbar>(R.id.search_activity_toolbar)

        searchClearEdittextImageview.setOnClickListener {
            editTextSearchActivity.setText("")
            val view: View? = this.currentFocus
            if (view != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchClearEdittextImageview.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        editTextSearchActivity.addTextChangedListener(simpleTextWatcher)
        settingsArrowBack.setNavigationOnClickListener{
            this.finish()
        }
    }
}

private fun clearButtonVisibility(s: CharSequence?): Int {
    return if (s.isNullOrEmpty()) {
        View.GONE
    } else {
        View.VISIBLE
    }
}




