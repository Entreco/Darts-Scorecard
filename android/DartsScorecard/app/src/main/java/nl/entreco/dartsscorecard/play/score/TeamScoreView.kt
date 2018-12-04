package nl.entreco.dartsscorecard.play.score

import androidx.recyclerview.widget.RecyclerView
import nl.entreco.dartsscorecard.databinding.TeamScoreViewBinding

/**
 * Created by Entreco on 18/11/2017.
 */
class TeamScoreView(private val binding: TeamScoreViewBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
    fun bind(teamScoreViewModel: TeamScoreViewModel) {
        binding.viewModel = teamScoreViewModel
        binding.executePendingBindings()
    }
}