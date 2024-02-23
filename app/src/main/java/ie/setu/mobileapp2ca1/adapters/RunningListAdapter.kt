package ie.setu.mobileapp2ca1.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.mobileapp2ca1.databinding.CardRunningBinding
import ie.setu.mobileapp2ca1.models.RunningModel

class RunningAdapter constructor(private var runningTracks: List<RunningModel>) : RecyclerView.Adapter<RunningAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardRunningBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val tracks = runningTracks[holder.adapterPosition]
        holder.bind(tracks)
    }



    override fun getItemCount(): Int = runningTracks.size

    class MainHolder(private val binding : CardRunningBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(running: RunningModel) {
            binding.runningTitle.text = running.title
            binding.runningDescription.text = running.description
        }
    }
}