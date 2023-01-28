package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.netology.nmedia.databinding.TrackBinding
import ru.netology.nmedia.model.Track

interface OnInteractionListener {
    fun onClick(track: Track) {}
}

class TrackAdapter(private val onInteractionListener: OnInteractionListener) :
    ListAdapter<Track, TrackViewHolder>(TrackDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = TrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding = binding, onInteractionListener = onInteractionListener)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track)
    }

    override fun getItemViewType(position: Int): Int {
        return position;
    }
}