package ie.setu.mobileapp2ca1.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.mobileapp2ca1.databinding.CardRunningBinding
import ie.setu.mobileapp2ca1.models.RunningModel

interface RunningListener {
    fun onTrackClick(track: RunningModel, position: Int)
}

class RunningAdapter constructor(
    private var runningTracks: List<RunningModel>,
    private val listener: RunningListener) : RecyclerView.Adapter<RunningAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardRunningBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val tracks = runningTracks[holder.adapterPosition]
        holder.bind(tracks, listener)
    }

    override fun getItemCount(): Int = runningTracks.size

    class MainHolder(private val binding : CardRunningBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(running: RunningModel, listener: RunningListener) {
            binding.runningTitle.text = running.title
            binding.runningDescription.text = running.description
            binding.runningDifficulty.text = running.difficulty.toString() + " ★\t"
            binding.runningWeather.text = running.weatherCondition
            Picasso.get().load(running.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onTrackClick(running,adapterPosition) }
        }
    }
}