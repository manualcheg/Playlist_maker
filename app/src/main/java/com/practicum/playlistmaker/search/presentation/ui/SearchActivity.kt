package com.practicum.playlistmaker.search.presentation.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.data.storage.SearchStorageImpl
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.search.presentation.SearchViewModel
import com.practicum.playlistmaker.search.presentation.SearchViewModel.Companion.getViewModelFactory
import com.practicum.playlistmaker.search.presentation.ui.models.SearchState
import com.practicum.playlistmaker.utils.Constants.Companion.SHARED_PREFS_SELECTED_TRACKS
import com.practicum.playlistmaker.utils.Constants.Companion.USERTEXT

class SearchActivity : AppCompatActivity() {

    private lateinit var searchViewModel: SearchViewModel

    private lateinit var editTextSearchActivity: EditText
    private lateinit var searchClearEdittextImageview: ImageView
    private lateinit var searchArrowBack: androidx.appcompat.widget.Toolbar
    private lateinit var recyclerViewSearch: RecyclerView
    private lateinit var recyclerViewListenedTracks: RecyclerView

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var searchHistoryClearButton: AppCompatButton
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var placeholderMessage: TextView

    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderButtonReload: AppCompatButton
    private lateinit var layoutOfListenedTracks: LinearLayout
    private lateinit var progressBar: ProgressBar
    private var userInputText: String = ""
    private var trackList = ArrayList<Track>()

    private var trackListAdapter = SearchAdapter(trackList)
    private var selectedTracks = ArrayList<Track>()
    private var selectedTracksAdapter =
        SearchAdapter(selectedTracks)      //адаптер для прослушанных треков


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        finderViewById()

//          создаем viewModel
        searchViewModel =
            ViewModelProvider(this, getViewModelFactory())[SearchViewModel::class.java]
//          подписываемся на изменение LiveData типа SearchState
        searchViewModel.observeState().observe(this) {
            render(it)
        }

        recyclerViewSearch.adapter = trackListAdapter
        sharedPrefs = getSharedPreferences(SHARED_PREFS_SELECTED_TRACKS, MODE_PRIVATE)
        buildRecycleViewListenedTracks()

        /* Вывод слоя с историей выбранных треков */
        editTextSearchActivity.setOnFocusChangeListener { view, hasFocus ->
            layoutOfListenedTracks.visibility =
                if (hasFocus && userInputText.isEmpty() && selectedTracks.isNotEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }

        /* Подписка на изменение SharedPreferences */
        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPrefs, key ->
            selectedTracks = SearchStorageImpl(sharedPrefs).getData()
            selectedTracksAdapter = SearchAdapter(selectedTracks)
            recyclerViewListenedTracks.adapter = selectedTracksAdapter
            selectedTracksAdapter.notifyItemRangeChanged(0, selectedTracks.lastIndex)
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)


        /* эмуляция кнопки для поиска. Изменяет тип кнопки ввода на клавиатуре: */
        editTextSearchActivity.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ
                if (userInputText.isNotEmpty()) {
                    searchViewModel.searchRequest(userInputText)
                }
                true
            }
            false
        }

        /* Крестик очистки поля ввода */
        searchClearEdittextImageview.setOnClickListener {
            editTextSearchActivity.setText("")
            hideUnnecessary()

            /* Скрытие клавиатуры после ввода */
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

                if (userInputText.isNotEmpty()) {
                    searchViewModel.searchDebounce(userInputText)            //поиск с задержкой
                }

                // Скрытие слоя с историей выбранных треков, если есть ввод
                if (editTextSearchActivity.hasFocus() && userInputText.isNotEmpty()) {
                    layoutOfListenedTracks.visibility = View.GONE
                }
                if (editTextSearchActivity.hasFocus() && userInputText.isEmpty() && selectedTracks.isNotEmpty()) {
                    layoutOfListenedTracks.visibility = View.VISIBLE
                }

                if (userInputText.isEmpty() && selectedTracks.isNotEmpty()) {
                    trackList.clear()
                    trackListAdapter.setTracks(trackList)
                    trackListAdapter.notifyItemRangeChanged(0, trackList.lastIndex)
                    hideUnnecessary()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        editTextSearchActivity.addTextChangedListener(simpleTextWatcher)

        searchArrowBack.setNavigationOnClickListener {
            this.finish()
        }

        placeholderButtonReload.setOnClickListener {
            searchViewModel.searchRequest(userInputText)
        }

        /* Кнопка очистки прослушанных треков */
        searchHistoryClearButton.setOnClickListener {
//            SearchHistory(sharedPrefs).clear()
            SearchStorageImpl(sharedPrefs).clearHistory()
//            selectedTracks = SearchHistory(sharedPrefs).read()
            selectedTracks = SearchStorageImpl(sharedPrefs).getData()
            selectedTracksAdapter = SearchAdapter(selectedTracks)
            recyclerViewListenedTracks.adapter = selectedTracksAdapter
            selectedTracksAdapter.notifyItemRangeChanged(0, selectedTracks.lastIndex)
            hideUnnecessary()

            Toast.makeText(this, "История очищена", Toast.LENGTH_SHORT).show()
        }
        //Конец OnCreate()
    }


    private fun buildRecycleViewListenedTracks() {
        // тут нужна подписка на LiveData
        // точнее метод должен уехать в ViewModel и что-то возвращать сюда
        selectedTracks = SearchStorageImpl(sharedPrefs).getData()
        selectedTracksAdapter = SearchAdapter(selectedTracks)
        recyclerViewListenedTracks.adapter = selectedTracksAdapter
        selectedTracksAdapter.notifyItemRangeChanged(0, selectedTracks.lastIndex)
    }


    // Смена состояний экрана в ответ на изменение LiveData
    fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Error -> showError(state.errorMessage)
            is SearchState.Empty -> showEmpty(state.message)
            is SearchState.Content -> showContent(state.tracks)
        }
    }

    fun showLoading() {
        recyclerViewSearch.visibility = View.GONE
        layoutOfListenedTracks.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        placeholderImage.visibility = View.GONE
        placeholderButtonReload.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    fun showError(errorMessage: String) {
        recyclerViewSearch.visibility = View.GONE
        layoutOfListenedTracks.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        placeholderImage.visibility = View.VISIBLE
        placeholderButtonReload.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        placeholderMessage.text = errorMessage
        placeholderImage.setImageResource(R.drawable.placeholder_no_network)
    }

    fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
        placeholderImage.setImageResource(R.drawable.placeholder_nothing_found)
        placeholderButtonReload.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        placeholderImage.visibility = View.VISIBLE
    }

    fun showContent(trackList: List<Track>) {
        recyclerViewSearch.visibility = View.VISIBLE
        layoutOfListenedTracks.visibility = View.GONE
        placeholderImage.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        placeholderButtonReload.visibility = View.GONE
        progressBar.visibility = View.GONE
        trackListAdapter.setTracks(trackList)
    }


    private fun finderViewById() {
        recyclerViewSearch = findViewById(R.id.recyclerViewSearch)
        recyclerViewListenedTracks = findViewById(R.id.recyclerViewListenedTracks)
        searchArrowBack = findViewById(R.id.search_activity_toolbar)
        searchClearEdittextImageview = findViewById(R.id.search_clear_edittext_imageview)
        editTextSearchActivity = findViewById(R.id.search_activity_edittext)
        placeholderMessage = findViewById(R.id.placeholder_search_screen_text)
        placeholderImage = findViewById(R.id.placeholder_search_screen_image)
        placeholderButtonReload = findViewById(R.id.placeholder_search_button)
        layoutOfListenedTracks = findViewById(R.id.layout_of_listened_tracks)
        searchHistoryClearButton = findViewById(R.id.search_history_clear_button)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun hideUnnecessary() {
        trackList.clear()
        trackListAdapter.setTracks(trackList)
        trackListAdapter.notifyItemRangeChanged(0, trackList.lastIndex)
        placeholderMessage.visibility = View.GONE
        placeholderImage.visibility = View.GONE
        placeholderButtonReload.visibility = View.GONE
        layoutOfListenedTracks.visibility = View.GONE
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USERTEXT, userInputText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        userInputText = savedInstanceState.getString(USERTEXT, "")
        editTextSearchActivity.setText(userInputText)
        searchViewModel.searchRequest(userInputText)
    }

}

private fun clearButtonVisibility(s: CharSequence?): Int {
    return if (s.isNullOrEmpty()) {
        View.GONE
    } else {
        View.VISIBLE
    }
}






