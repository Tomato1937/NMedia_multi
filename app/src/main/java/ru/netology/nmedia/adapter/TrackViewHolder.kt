package ru.netology.nmedia.adapter

import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.TrackBinding
import ru.netology.nmedia.model.Track

class TrackViewHolder(
    private val onInteractionListener: OnInteractionListener,
    private val binding: TrackBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(track: Track) {
        binding.apply {
            trackName.text = track.file
            stateButton.icon =
                AppCompatResources.getDrawable(
                    binding.root.context,
                    if (track.isPlaying == true)
                        R.drawable.ic_baseline_pause_circle_filled_24 else
                        R.drawable.ic_baseline_play_circle_filled_24
                )
            stateButton.setOnClickListener {
                onInteractionListener.onClick(track)
            }
        }
    }

}