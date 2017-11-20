package nl.entreco.dartsscorecard.play.score

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayMap
import android.util.ArrayMap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.TeamScoreViewBinding
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.players.Team

/**
 * Created by Entreco on 18/11/2017.
 */
class TeamScoreView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr : Int = 0, defStyleRes : Int = 0) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val inflater by lazy { LayoutInflater.from(context) }
    private val binding = DataBindingUtil.inflate<TeamScoreViewBinding>(inflater, R.layout.team_score_view, this, true)

    init {
        orientation = HORIZONTAL
    }

    fun bind(team: Team, score: Score){
        binding.team = team
        binding.score = score
    }

    fun update(score: Score) {
        binding.score = score
    }
}