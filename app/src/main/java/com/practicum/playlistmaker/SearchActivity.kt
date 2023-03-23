package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        const val USERTEXT =
            "USER_INPUT"   //константа-ключ для поиска в Bundle сохраненного состояния
    }

    private val baseUrl = "https://itunes.apple.com/"

    private lateinit var editTextSearchActivity: EditText
    private lateinit var searchClearEdittextImageview: ImageView
    private lateinit var settingsArrowBack: androidx.appcompat.widget.Toolbar
    private lateinit var recyclerViewSearch: RecyclerView
    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderButtonReload: AppCompatButton

    private var userInputText: String = ""

    private var retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val itunesService = retrofit.create(itunesApi::class.java)
    private var trackList = ArrayList<Track>()
    private val trackListAdapter = TrackItemAdapter(trackList)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerViewSearch = findViewById(R.id.recyclerViewSearch)
        settingsArrowBack = findViewById(R.id.search_activity_toolbar)
        searchClearEdittextImageview = findViewById(R.id.search_clear_edittext_imageview)
        editTextSearchActivity = findViewById(R.id.search_activity_edittext)
        placeholderMessage = findViewById(R.id.placeholder_search_screen_text)
        placeholderImage = findViewById(R.id.placeholder_search_screen_image)
        placeholderButtonReload = findViewById(R.id.placeholder_search_button)

//        trackListAdapter.trackList = trackList  // ? масло масленное - выше инициализирован с trackList
        recyclerViewSearch.adapter = trackListAdapter
        //эмуляция кнопки для поиска. Изменяет тип кнопки ввода на клавиатуре:

        editTextSearchActivity.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ
                if (editTextSearchActivity.text.isNotEmpty()) {
                    search()
                }
                true //в чём смысл?
            }
            false
        }

//      Крестик очистки поля ввода
        searchClearEdittextImageview.setOnClickListener {
            editTextSearchActivity.setText("")
            trackList.clear()
            trackListAdapter.notifyDataSetChanged()
            placeholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            placeholderButtonReload.visibility = View.GONE

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

        placeholderButtonReload.setOnClickListener{
            search()
        }
    }

    private fun search() {
        itunesService.search(userInputText)
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>, response: Response<TrackResponse>
                ) {
                    when (response.code()) {
                        200 -> {        //success
                            if (response.body()?.results?.isNotEmpty() == true) {
                                placeholderMessage.visibility = View.GONE
                                placeholderImage.visibility = View.GONE
                                placeholderButtonReload.visibility = View.GONE

                                trackList.clear()
                                trackList.addAll(response.body()?.results!!)
                                trackListAdapter.notifyDataSetChanged()
                                showMessage("", "")
                            } else {
                                showMessage(getString(R.string.nothing_found), "1")
                            }
                        }
                        else -> {       //error with server answer
                            showMessage(getString(R.string.something_went_wrong), "2")
                        }
                    }
                }

                override fun onFailure( //error without server answer
                    call: Call<TrackResponse>, t: Throwable
                ) {
                    showMessage(getString(R.string.something_went_wrong), "2")
                }

            })
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            trackList.clear()
            trackListAdapter.notifyDataSetChanged()
            placeholderMessage.text = text

            if (additionalMessage=="1") {
                placeholderImage.setImageResource(R.drawable.placeholder_nothing_found)
                placeholderButtonReload.visibility = View.GONE
            } else if (additionalMessage=="2") {
                placeholderImage.setImageResource(R.drawable.placeholder_no_network)
                placeholderButtonReload.visibility = View.VISIBLE
            }
            placeholderMessage.visibility = View.VISIBLE
            placeholderImage.visibility = View.VISIBLE
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
        search()
    }
}

private fun clearButtonVisibility(s: CharSequence?): Int {
    return if (s.isNullOrEmpty()) {
        View.GONE
    } else {
        View.VISIBLE
    }
}




