package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

const val SELECTED_TRACKS = "Selected_tracks"

class SearchActivity : AppCompatActivity() {
    companion object {
        const val USERTEXT =
            "USER_INPUT"   //константа-ключ для поиска в Bundle сохраненного состояния
        const val NOTHING_FOUND = "1"
        const val SOMETHING_WENT_WRONG = "2"
    }

    private val baseUrl = "https://itunes.apple.com/"
    private lateinit var editTextSearchActivity: EditText
    private lateinit var searchClearEdittextImageview: ImageView
    private lateinit var settingsArrowBack: androidx.appcompat.widget.Toolbar
    private lateinit var recyclerViewSearch: RecyclerView
    private lateinit var recyclerViewListenedTracks: RecyclerView
    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderButtonReload: AppCompatButton
    private lateinit var listOfFound: LinearLayout
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var searchHistoryClearButton: AppCompatButton
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

    private var userInputText: String = ""
    private var retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    private val itunesService = retrofit.create(itunesApi::class.java)
    private var trackList = ArrayList<Track>()
    private val trackListAdapter = TrackItemAdapter(trackList)
    private var selectedTracks = ArrayList<Track>()
    private var selectedTracksAdapter = TrackItemAdapter(selectedTracks)      //адаптер для прослушанных треков

  /*  private var testTracks:ArrayList<Track> = arrayListOf(
        Track("песенка1","Артист", "223093","https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg", "123456"),
        Track("песенка2","Артист", "223093","https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg", "123456"),
        Track("песенка3","Артист", "223093","https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg", "123456"),
        Track("песенка4","Артист", "223093","https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg", "123456"),
    )
    private val selectedTracksAdapter = TrackItemAdapter(testTracks)*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        finderViewById()

        sharedPrefs = getSharedPreferences(SHARED_PREFS_SELECTED_TRACKS, MODE_PRIVATE)
        buildRecycleViewListenedTracks()

        recyclerViewListenedTracks.adapter = selectedTracksAdapter
        selectedTracksAdapter.notifyItemRangeChanged(0, selectedTracks.size)

        /* Вывод слоя с историей выбранных треков */
        editTextSearchActivity.setOnFocusChangeListener { view, hasFocus ->
            listOfFound.visibility =
                if (hasFocus && editTextSearchActivity.text.isEmpty()) View.VISIBLE else View.GONE
        }

        /* Подписка на изменение SharedPreferences */
        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPrefs, key ->
            selectedTracks = SearchHistory(sharedPrefs).read()
            selectedTracksAdapter = TrackItemAdapter(selectedTracks)
            recyclerViewListenedTracks.adapter = selectedTracksAdapter
            selectedTracksAdapter.notifyItemRangeChanged(0, selectedTracks.size)
            Log.d("MyLog", "Подписка сработала")
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)

//        trackListAdapter.trackList = trackList  // ? масло масленное - выше инициализирован с trackList
        recyclerViewSearch.adapter = trackListAdapter

        /* эмуляция кнопки для поиска. Изменяет тип кнопки ввода на клавиатуре: */
        editTextSearchActivity.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ
//                if (editTextSearchActivity.text.isNotEmpty()) {
                if (userInputText.isNotEmpty()) {
                    search()
                }
                true
            }
            false
        }

//      Крестик очистки поля ввода
        searchClearEdittextImageview.setOnClickListener {
            editTextSearchActivity.setText("")
            trackListAdapter.setTracks(trackList, trackList)
            selectedTracksAdapter = TrackItemAdapter(selectedTracks)
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
                    clearButtonVisibility(s)    //если строка ввода пуста, то спрятать крестик очистки и наоборот
                userInputText = s.toString()
                // Скрытие слоя с историей выбранных треков, если есть ввод
                listOfFound.visibility =
                    if (editTextSearchActivity.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        editTextSearchActivity.addTextChangedListener(simpleTextWatcher)

        settingsArrowBack.setNavigationOnClickListener {
            this.finish()
        }

        placeholderButtonReload.setOnClickListener {
            search()
        }

        /* Кнопка очистки прослушанных треков */
        searchHistoryClearButton.setOnClickListener {
            SearchHistory(sharedPrefs).clear()
            selectedTracks = SearchHistory(sharedPrefs).read()
            selectedTracksAdapter = TrackItemAdapter(selectedTracks)
            recyclerViewListenedTracks.adapter = selectedTracksAdapter
            selectedTracksAdapter.notifyItemRangeChanged(0, selectedTracks.size)

            Toast.makeText(this@SearchActivity,"История очищена",Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildRecycleViewListenedTracks() {
        selectedTracks = SearchHistory(sharedPrefs).read()
        selectedTracksAdapter = TrackItemAdapter(selectedTracks)
        selectedTracksAdapter.notifyItemRangeChanged(0, selectedTracks.size)
    }


    private fun finderViewById() {
        recyclerViewSearch = findViewById(R.id.recyclerViewSearch)
        recyclerViewListenedTracks = findViewById(R.id.recyclerViewListenedTracks)
        settingsArrowBack = findViewById(R.id.search_activity_toolbar)
        searchClearEdittextImageview = findViewById(R.id.search_clear_edittext_imageview)
        editTextSearchActivity = findViewById(R.id.search_activity_edittext)
        placeholderMessage = findViewById(R.id.placeholder_search_screen_text)
        placeholderImage = findViewById(R.id.placeholder_search_screen_image)
        placeholderButtonReload = findViewById(R.id.placeholder_search_button)
        listOfFound = findViewById(R.id.list_of_found)
        searchHistoryClearButton = findViewById(R.id.search_history_clear_button)
    }

    private fun search() {
        if (userInputText.isNotEmpty()){
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
                                    trackListAdapter.setTracks(
                                        trackList,
                                        response.body()?.results!!
                                    )
                                    showMessage("", "")
                                } else {
                                    showMessage(getString(R.string.nothing_found), NOTHING_FOUND)
                                }
                            }
                            else -> {       //error with server answer
                                showMessage(
                                    getString(R.string.something_went_wrong),
                                    SOMETHING_WENT_WRONG
                                )
                            }
                        }
                    }

                    override fun onFailure( //error without server answer
                        call: Call<TrackResponse>, t: Throwable
                    ) {
                        showMessage(getString(R.string.something_went_wrong), SOMETHING_WENT_WRONG)
                    }
                })
    }
    }

    private fun showMessage(text: String, myErrorCode: String) {
        if (text.isNotEmpty()) {
            trackListAdapter.setTracks(trackList, trackList)
            placeholderMessage.text = text
            if (myErrorCode == NOTHING_FOUND) {
                placeholderImage.setImageResource(R.drawable.placeholder_nothing_found)
                placeholderButtonReload.visibility = View.GONE
            } else if (myErrorCode == SOMETHING_WENT_WRONG) {
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






