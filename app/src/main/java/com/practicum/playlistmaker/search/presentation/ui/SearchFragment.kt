package com.practicum.playlistmaker.search.presentation.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.search.presentation.SearchViewModel
import com.practicum.playlistmaker.search.presentation.ui.models.SearchState
import com.practicum.playlistmaker.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment:Fragment() {

    lateinit var binding: FragmentSearchBinding

    private val searchViewModel: SearchViewModel by viewModel()

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

    private var userInputText: String = ""
    private var trackList = ArrayList<Track>()

    private var trackListAdapter = SearchAdapter(trackList)
    private var selectedTracks = ArrayList<Track>()
    private var selectedTracksAdapter =
        SearchAdapter(selectedTracks)      //адаптер для прослушанных треков


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        finderViewById()

        createViewModelAndObserveToLiveData()

        binding.recyclerViewSearch.adapter = trackListAdapter
        sharedPrefs = requireContext().getSharedPreferences(
            Constants.PLAYLISTMAKER_SHAREDPREFS,
            AppCompatActivity.MODE_PRIVATE
        )

        buildRecycleViewListenedTracks()

        workWithVisibilityOfListenedTracks()

        subscribeToChangingSharedPrefs()

        emulationSearchButtonInKeyboard()

        workWithInput()

        settingListenersOnButtons()

        workWithButtonClearHistory()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.USERTEXT, userInputText)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        userInputText = savedInstanceState?.getString(Constants.USERTEXT) ?: ""
        binding.searchActivityEdittext.setText(userInputText)
        searchViewModel.searchRequest(userInputText)
    }

    private fun settingListenersOnButtons() {
//        searchArrowBack.setNavigationOnClickListener {
//            this.finish()
//        }
        binding.placeholderSearchButton.setOnClickListener {
            searchViewModel.searchRequest(userInputText)
        }
    }

    private fun emulationSearchButtonInKeyboard() {
        /* эмуляция кнопки для поиска. Изменяет тип кнопки ввода на клавиатуре: */
        binding.searchActivityEdittext.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ
                if (userInputText.isNotEmpty()) {
                    searchViewModel.searchRequest(userInputText)
                }
                true
            }
            false
        }
    }

    private fun subscribeToChangingSharedPrefs() {
        /* Подписка на изменение SharedPreferences  */
        listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            selectedTracks = searchViewModel.getData()
            selectedTracksAdapter = SearchAdapter(selectedTracks)
            binding.recyclerViewListenedTracks.adapter = selectedTracksAdapter
            selectedTracksAdapter.notifyItemRangeChanged(0, selectedTracks.lastIndex)
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)
    }

    private fun workWithVisibilityOfListenedTracks() {
        /* Вывод слоя с историей выбранных треков */
        binding.searchActivityEdittext.setOnFocusChangeListener { _, hasFocus ->
            binding.layoutOfListenedTracks.visibility =
                if (hasFocus && userInputText.isEmpty() && selectedTracks.isNotEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
    }

    private fun createViewModelAndObserveToLiveData() {
//          подписываемся на изменение LiveData типа SearchState
        searchViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun workWithButtonClearHistory() {
        /* Кнопка очистки прослушанных треков */
        binding.searchHistoryClearButton.setOnClickListener {
            searchViewModel.clearHistory()
            selectedTracks = searchViewModel.getData()
            selectedTracksAdapter = SearchAdapter(selectedTracks)
            binding.recyclerViewListenedTracks.adapter = selectedTracksAdapter
            selectedTracksAdapter.notifyItemRangeChanged(0, selectedTracks.lastIndex)
            hideUnnecessary()
            Toast.makeText(requireContext(), "История очищена", Toast.LENGTH_SHORT).show()
        }
    }

    private fun workWithInput() {
        /* Крестик очистки поля ввода */
        binding.searchClearEdittextImageview.setOnClickListener {
            binding.searchActivityEdittext.setText("")
            hideUnnecessary()

            /* Скрытие клавиатуры после ввода */
            val view: View? = requireActivity().currentFocus
            if (view != null) {
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchClearEdittextImageview.visibility =
                    clearButtonVisibility(s)    //если строка ввода пуста, то спрятать крестик очистки и наоборот
                userInputText = s.toString()

                if (userInputText.isNotEmpty()) {
                    searchViewModel.searchDebounce(userInputText)            //поиск с задержкой
                }

                // Скрытие слоя с историей выбранных треков, если есть ввод
                if (binding.searchActivityEdittext.hasFocus() && userInputText.isNotEmpty()) {
                    binding.layoutOfListenedTracks.visibility = View.GONE
                }
                if (binding.searchActivityEdittext.hasFocus() && userInputText.isEmpty() && selectedTracks.isNotEmpty()) {
                    binding.layoutOfListenedTracks.visibility = View.VISIBLE
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
        binding.searchActivityEdittext.addTextChangedListener(simpleTextWatcher)
    }

    private fun buildRecycleViewListenedTracks() {
        selectedTracks = searchViewModel.getData()
        selectedTracksAdapter = SearchAdapter(selectedTracks)
        binding.recyclerViewListenedTracks.adapter = selectedTracksAdapter
        selectedTracksAdapter.notifyItemRangeChanged(0, selectedTracks.lastIndex)
    }

    // Смена состояний экрана в ответ на изменение LiveData
    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Error -> showError(state.errorMessage)
            is SearchState.Empty -> showEmpty(state.message)
            is SearchState.Content -> showContent(state.tracks)
        }
    }

    private fun showLoading() {
        binding.recyclerViewSearch.visibility = View.GONE
        binding.layoutOfListenedTracks.visibility = View.GONE
        binding.placeholderSearchScreenText.visibility = View.GONE
        binding.placeholderSearchScreenImage.visibility = View.GONE
        binding.placeholderSearchButton.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError(errorMessage: String) {
        binding.recyclerViewSearch.visibility = View.GONE
        binding.layoutOfListenedTracks.visibility = View.GONE
        binding.placeholderSearchScreenText.visibility = View.VISIBLE
        binding.placeholderSearchScreenImage.visibility = View.VISIBLE
        binding.placeholderSearchButton.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.placeholderSearchScreenText.text = errorMessage
        binding.placeholderSearchScreenImage.setImageResource(R.drawable.placeholder_no_network)
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
        binding.placeholderSearchScreenImage.setImageResource(R.drawable.placeholder_nothing_found)
        binding.placeholderSearchButton.visibility = View.GONE
        binding.placeholderSearchScreenText.visibility = View.VISIBLE
        binding.placeholderSearchScreenImage.visibility = View.VISIBLE
    }

    private fun showContent(trackList: List<Track>) {
        binding.recyclerViewSearch.visibility = View.VISIBLE
        binding.layoutOfListenedTracks.visibility = View.GONE
        binding.placeholderSearchScreenImage.visibility = View.GONE
        binding.placeholderSearchScreenText.visibility = View.GONE
        binding.placeholderSearchButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        trackListAdapter.setTracks(trackList)
    }

    private fun finderViewById() {
//        recyclerViewSearch = findViewById(R.id.recyclerViewSearch)
//        recyclerViewListenedTracks = findViewById(R.id.recyclerViewListenedTracks)
//        searchArrowBack = findViewById(R.id.search_activity_toolbar)
//        searchClearEdittextImageview = findViewById(R.id.search_clear_edittext_imageview)
//        editTextSearchActivity = findViewById(R.id.search_activity_edittext)
//        placeholderMessage = findViewById(R.id.placeholder_search_screen_text)
//        placeholderImage = findViewById(R.id.placeholder_search_screen_image)
//        placeholderButtonReload = findViewById(R.id.placeholder_search_button)
//        layoutOfListenedTracks = findViewById(R.id.layout_of_listened_tracks)
//        searchHistoryClearButton = findViewById(R.id.search_history_clear_button)
//        progressBar = findViewById(R.id.progressBar)
    }

    private fun hideUnnecessary() {
        trackList.clear()
        trackListAdapter.setTracks(trackList)
        trackListAdapter.notifyItemRangeChanged(0, trackList.lastIndex)
        binding.placeholderSearchScreenText.visibility = View.GONE
        binding.placeholderSearchScreenImage.visibility = View.GONE
        binding.placeholderSearchButton.visibility = View.GONE
        binding.layoutOfListenedTracks.visibility = View.GONE
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}