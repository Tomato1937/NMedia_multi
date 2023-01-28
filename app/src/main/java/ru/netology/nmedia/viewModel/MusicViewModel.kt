package ru.netology.nmedia.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.api.Api
import ru.netology.nmedia.model.FeedModel

class MusicViewModel : ViewModel() {
    private val _data = MutableLiveData<FeedModel>()
    val data: LiveData<FeedModel>
        get() = _data

    private val _curTrackId = MutableLiveData<String>()
    val curTrackId: LiveData<String>
        get() = _curTrackId

    init {
        loadTracks()
    }

    private fun loadTracks() = viewModelScope.launch {
        try {
            val response = Api.service.loadAlbum()
            if (response.isSuccessful) {
                _data.value = FeedModel(
                    album = response.body() ?: throw Exception("Не удалось загрузить альбом"),
                )
            }
        } catch (e: Exception) {
            Log.d("MUSIC", "loadTracks: " + e.message)
        }
    }

    fun isPlaying(): Boolean {
        return data.value?.album?.tracks?.filter { track ->
            track.isPlaying ?: false
        }?.size ?: 0 > 0
    }

    fun play(trackId: String) {
        _data.value?.let {
            _data.value = FeedModel(album = it.album.copy(tracks = it.album.tracks.map { curTrack ->
                if (trackId == curTrack.id) curTrack.copy(isPlaying = !(curTrack.isPlaying?: false)) else curTrack.copy(isPlaying = false)
            }))
        }
        if (trackId !== _curTrackId.value) {
            _curTrackId.value = trackId
        }
    }
}