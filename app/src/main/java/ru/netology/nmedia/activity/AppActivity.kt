package ru.netology.nmedia.activity

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.TrackAdapter
import ru.netology.nmedia.databinding.ActivityAppBinding
import ru.netology.nmedia.model.Track
import ru.netology.nmedia.ui.MediaLifecycleObserver
import ru.netology.nmedia.viewModel.MusicViewModel

class AppActivity : AppCompatActivity(R.layout.activity_app) {
    private val mediaObserver = MediaLifecycleObserver()
    private val viewModel: MusicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = TrackAdapter(object : OnInteractionListener {
            override fun onClick(track: Track) {
                val url = "${BuildConfig.BASE_URL}${track.file}"

                if (viewModel.curTrackId.value != track.id) {
                    mediaObserver.onStop()
                }

                if (mediaObserver.player == null || mediaObserver.player?.isPlaying!!) {
                    mediaObserver.onPause()
                } else {
                    if (viewModel.curTrackId.value != track.id) {
                        mediaObserver.apply {
                            player?.setDataSource(url)
                            player?.prepare()
                        }.play()
                    } else
                        mediaObserver.play()
                }
                viewModel.play(track.id)
            }

        })
        binding.trackList.adapter = adapter
        viewModel.data.observe(this) { state ->
            adapter.submitList(state.album.tracks)
            binding.albumTitle.text = state.album.title
            binding.artist.text = state.album.artist
            binding.published.text = state.album.published
            binding.genre.text = state.album.genre
            binding.commonButton.icon = AppCompatResources.getDrawable(
                binding.root.context,
                if (viewModel.isPlaying())
                    R.drawable.ic_baseline_pause_circle_filled_24 else
                    R.drawable.ic_baseline_play_circle_filled_24
            )
        }

        binding.commonButton.setOnClickListener {
            if (mediaObserver.player != null) {
                if (mediaObserver.player!!.isPlaying) {
                    mediaObserver.onPause()
                } else {
                    mediaObserver.play()
                }
                viewModel.curTrackId.value?.let { curTrackId ->
                    viewModel.play(curTrackId)
                }
            }
        }

        mediaObserver.player?.setOnCompletionListener {
            mediaObserver.onStop()
            viewModel.data.value?.album?.tracks?.let { tracks ->
                tracks.indexOfFirst { track ->
                    viewModel.curTrackId.value === track.id
                }.let { index ->
                    val nextTrack = tracks[if (index == (tracks.size - 1)) 0 else index + 1]
                    mediaObserver.apply {
                        player?.setDataSource("${BuildConfig.BASE_URL}${nextTrack.file}")
                        player?.prepare()
                    }.play()
                    viewModel.play(nextTrack.id)
                }
            }
        }

/*
        findViewById<Button>(R.id.play).setOnClickListener {
            MediaPlayer.create(this, R.raw.ring).apply {
                setOnCompletionListener {
                    it.release()
                }
            }.start()
        }
*/

        lifecycle.addObserver(mediaObserver)

/*
        findViewById<Button>(R.id.play).setOnClickListener {
            mediaObserver.apply {
                resources.openRawResourceFd(R.raw.ring).use { afd ->
                    player?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                }
            }.play()
        }
*/
/*
        findViewById<Button>(R.id.play).setOnClickListener {
            mediaObserver.apply {
                player?.setDataSource(
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
                )
            }.play()
        }
*/

/*        findViewById<VideoView>(R.id.video).apply {
            setMediaController(MediaController(this@AppActivity))
            setVideoURI(
                Uri.parse("https://archive.org/download/BigBuckBunny1280x720Stereo/big_buck_bunny_720_stereo.mp4")
            )
            setOnPreparedListener {
                start()
            }
            setOnCompletionListener {
                stopPlayback()
            }
        }*/
    }
}



