package com.practicum.playlistmaker.mediateka.playlists.presentation.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistWorkBinding
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.presentation.PlaylistWorkAdapter
import com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels.PlaylistWorkFragmentViewModel
import com.practicum.playlistmaker.search.domain.entities.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistWorkFragment : Fragment(), PlaylistWorkAdapter.LongClickListener {
    private lateinit var binding: FragmentPlaylistWorkBinding
    private val playlistWorkFragmentViewModel: PlaylistWorkFragmentViewModel by viewModel()
    private var playlistFromViewModule: Playlist? = null
    private var listOfTracksId = listOf<String>()
    private var listOfTracks = listOf<Track>()
    private var playlistId: Long = 0
    private var playlistWorkAdapter = PlaylistWorkAdapter(listOfTracks, this)
    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistWorkBinding.inflate(inflater, container, false)
        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //блокировка поворота
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screenInit()

        settingObserversForData()

        settingOnClickListenersInPlaylistWorkScreen()

        workWithOverridingBottomSheetBehaviorOnStateChanged()

        settingOnClickListenersInBottomSheet()
    }

    private fun screenInit() {
        binding.playlistWorkRecyclerView.adapter = playlistWorkAdapter
        playlistId = requireArguments().getLong("playlistId")
//        Запрос плейлиста
        playlistWorkFragmentViewModel.getPlaylist(playlistId)

        bottomSheetBehavior = binding.bottomSheetPlaylistWorkMenu?.let { it1 ->
            BottomSheetBehavior.from(it1.root).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    private fun settingOnClickListenersInPlaylistWorkScreen() {
        binding.playlistWorkArrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.playlistWorkShareButton.setOnClickListener {
            workWithSharePlaylist()
        }

        binding.playlistWorkMenuButton?.setOnClickListener {
//          Заполнение инфы о плейлисте в меню
            Glide.with(binding.bottomSheetPlaylistWorkMenu!!.bottomSheetPlaylistWorkPlaylistCover)
                .load(playlistFromViewModule?.playlistCover)
                .placeholder(R.drawable.placeholder_album_cover)
                .centerCrop()
                .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.dp4)))
                .into(binding.bottomSheetPlaylistWorkMenu!!.bottomSheetPlaylistWorkPlaylistCover)
            binding.bottomSheetPlaylistWorkMenu!!.bottomSheetPlaylistWorkPlaylistName.text =
                playlistFromViewModule?.playlistName
            binding.bottomSheetPlaylistWorkMenu!!.bottomSheetPlaylistWorkCountTracks.text =
                countOfTracksWithWord()
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun settingObserversForData() {
        //      Подписка на получение плейлиста
        playlistWorkFragmentViewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            workWithReceivedPlaylist(playlist)
        }

//      Подписка на получение списка треков
        playlistWorkFragmentViewModel.listOfTracks.observe(viewLifecycleOwner) {
            listOfTracks = it
            fillingRecyclerView(it)
        }

//        Подписка на получение общей длительности треков в плейлисте
        playlistWorkFragmentViewModel.totalDuration.observe(viewLifecycleOwner) {
            binding.playlistWorkTotalDuration.text =
                resources.getQuantityString(R.plurals.minutes, it.toInt(), it.toInt())
        }
    }

    private fun settingOnClickListenersInBottomSheet() {
        binding.bottomSheetPlaylistWorkMenu?.bottomSheetPlaylistWorkButtonShare?.setOnClickListener {
            workWithSharePlaylist()
        }

        binding.bottomSheetPlaylistWorkMenu?.bottomSheetPlaylistWorkButtonEdit?.setOnClickListener {
            bundle.putLong(PlaylistsFragment.PLAYLISTID, playlistId)
            findNavController().navigate(
                R.id.action_playlistWorkFragment_to_playlistEditFragment,
                bundle
            )
        }

        binding.bottomSheetPlaylistWorkMenu?.bottomSheetPlaylistWorkButtonDelete?.setOnClickListener {
            workWithDeletePlaylist()
        }
    }

    private fun workWithOverridingBottomSheetBehaviorOnStateChanged() {
        bottomSheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay?.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay?.visibility = View.VISIBLE
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun workWithReceivedPlaylist(playlist: Playlist) {
        playlistFromViewModule = playlist

        binding.playlistWorkPlaylistName.text = playlist.playlistName
        if (playlist.playlistDescription != "") {
            binding.playlistWorkPlaylistDescription.visibility = View.VISIBLE
            binding.playlistWorkPlaylistDescription.text = playlist.playlistDescription
        } else {
            binding.playlistWorkPlaylistDescription.visibility = View.GONE
        }
        setImage(playlist.playlistCover)
        binding.playlistWorkTracksCount?.text =
            playlistWorkFragmentViewModel.defineWord(
                playlist.countOfTracks
            )
//          Формируем список List<Int> id треков
        listOfTracksId =
            java.util.ArrayList(playlistFromViewModule?.listOfTracksId?.split(",")!!)
        playlistWorkFragmentViewModel.getTracksOfPlaylist(listOfTracksId)
    }

    private fun workWithDeletePlaylist() {
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Удалить плейлист")
            .setMessage("Хотите удалить плейлист?")
            .setPositiveButton("Удалить") { _, _ ->
                Toast.makeText(
                    requireContext(),
                    "Плейлист ${playlistFromViewModule?.playlistName} удалён!",
                    Toast.LENGTH_SHORT
                ).show()
//                playlistWorkFragmentViewModel.delTrack(currentTrackId, playlistId)
                playlistWorkFragmentViewModel.delPlaylist(playlistFromViewModule!!)
                findNavController().popBackStack()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun workWithSharePlaylist() {
        if (playlistFromViewModule?.listOfTracksId == "") {
            showToastAboutEmpty()
        } else {
            sharePlaylist()
        }
    }

    private fun sharePlaylist() {
        playlistWorkFragmentViewModel.makeTextFromListOfTracks(listOfTracks, playlistFromViewModule)
        playlistWorkFragmentViewModel.playlistTextForShare.observe(viewLifecycleOwner) { playlistText ->
            useIntent(playlistText)
        }
    }

    private fun useIntent(playlistText: String) {
        Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                playlistText
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            requireContext().startActivity(this)
        }, "Share APK")
    }

    private fun showToastAboutEmpty() {
        val customToast = LayoutInflater.from(requireContext()).inflate(
            R.layout.custom_toast,
            requireActivity().findViewById(R.id.custom_toast_layout_id)
        )
        customToast.findViewById<TextView>(R.id.text).text =
            getString(R.string.text_there_isnt_playlist)
        val toast = Toast.makeText(
            requireContext(),
            getString(R.string.text_there_isnt_playlist),
            Toast.LENGTH_SHORT
        )
        toast.setView(customToast)
        toast.show()
    }

    private fun fillingRecyclerView(listOfTracks: List<Track>) {
        if (listOfTracks.isEmpty()) {
            binding.bottomsheetPlaylistworkTextviewThereIsntAnyTracks?.visibility = View.VISIBLE
            binding.playlistWorkRecyclerView.visibility = View.GONE
        } else {
            playlistWorkAdapter = PlaylistWorkAdapter(listOfTracks, this)
            binding.playlistWorkRecyclerView.adapter = playlistWorkAdapter
            playlistWorkAdapter.notifyItemRangeChanged(0, listOfTracks.lastIndex)
            binding.playlistWorkRecyclerView.visibility = View.VISIBLE
            binding.bottomsheetPlaylistworkTextviewThereIsntAnyTracks?.visibility = View.GONE
        }
    }

    private fun setImage(playlistCover: String?) {
        Glide.with(binding.playlistWorkPlaylistCover)
            .load(playlistCover)
            .placeholder(R.drawable.placeholder_no_cover)
            .into(binding.playlistWorkPlaylistCover)
    }

    override fun trackInPlaylistClick(currentTrackId: String) {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(requireContext().getString(R.string.playlist_work_fragment_dialog_text_deltrack))
            .setMessage(requireContext().getString(R.string.playlist_work_fragment_dialog_text_areyousure))
            .setNegativeButton(
                requireContext().getString(R.string.playlist_work_fragment_dialog_text_cancel),
                null
            )
            .setPositiveButton(requireContext().getString(R.string.playlist_work_fragment_dialog_text_delete)) { _, _ ->
                playlistWorkFragmentViewModel.delTrack(currentTrackId, playlistId)
            }.show()
    }

    private fun countOfTracksWithWord(): String {
        val countOfTracksInt = playlistFromViewModule?.countOfTracks!!
        return resources.getQuantityString(
            R.plurals.tracks,
            countOfTracksInt,
            countOfTracksInt
        )
    }
}