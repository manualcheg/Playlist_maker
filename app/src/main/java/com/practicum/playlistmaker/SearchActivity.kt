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
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    private var userInputText: String = ""
    private lateinit var editTextSearchActivity: EditText
    private lateinit var searchClearEdittextImageview: ImageView
    private lateinit var settingsArrowBack: androidx.appcompat.widget.Toolbar
    private var trackList: ArrayList<Track> = ArrayList()
    private lateinit var recyclerViewSearch: RecyclerView

    companion object {
        const val USERTEXT =
            "USER_INPUT"   //константа-ключ для поиска в Bundle сохраненного состояния
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        trackList.add(
            Track(
                getString(R.string.trackName1),
                getString(R.string.artistName1),
                getString(R.string.trackTime1),
                getString(R.string.coverUrl1)
            )
        )
        trackList.add(
            Track(
                getString(R.string.trackName2),
                getString(R.string.artistName2),
                getString(R.string.trackTime2),
                getString(R.string.coverUrl2)
            )
        )
        trackList.add(
            Track(
                getString(R.string.trackName3),
                getString(R.string.artistName3),
                getString(R.string.trackTime3),
                getString(R.string.coverUrl3)
            )
        )
        trackList.add(
            Track(
                getString(R.string.trackName4),
                getString(R.string.artistName4),
                getString(R.string.trackTime4),
                getString(R.string.coverUrl4)
            )
        )
        trackList.add(
            Track(
                getString(R.string.trackName5),
                getString(R.string.artistName5),
                getString(R.string.trackTime5),
                getString(R.string.coverUrl5)
            )
        )


        recyclerViewSearch = findViewById(R.id.recyclerViewSearch)
        val trackListAdapter = TrackItemAdapter(trackList)
        recyclerViewSearch.adapter = trackListAdapter

        settingsArrowBack =
            findViewById<androidx.appcompat.widget.Toolbar>(R.id.search_activity_toolbar)
        searchClearEdittextImageview =
            findViewById<ImageView>(R.id.search_clear_edittext_imageview)
        editTextSearchActivity = findViewById<EditText>(R.id.search_activity_edittext)

//      Крестик очистки поля ввода
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
                searchClearEdittextImageview.visibility =
                    clearButtonVisibility(s)  //если строка ввода пуста, то спрятать крестик очистки и наоборот
                userInputText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        editTextSearchActivity.addTextChangedListener(simpleTextWatcher)
        settingsArrowBack.setNavigationOnClickListener {
            this.finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USERTEXT, userInputText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        userInputText = savedInstanceState.getString(USERTEXT, "")
        editTextSearchActivity.setText(userInputText)
    }
}

private fun clearButtonVisibility(s: CharSequence?): Int {
    return if (s.isNullOrEmpty()) {
        View.GONE
    } else {
        View.VISIBLE
    }
}




