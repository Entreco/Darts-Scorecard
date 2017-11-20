package nl.entreco.dartsscorecard.play.score

import android.databinding.BindingAdapter
import android.databinding.ObservableArrayList
import android.view.ViewGroup
import nl.entreco.dartsscorecard.CounterTextView
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.settings.ScoreSettings

/**
 * Created by Entreco on 20/11/2017.
 */
class ScoreBindings {
    companion object {
        @JvmStatic
        @BindingAdapter("teams", "scoreSettings")
        fun addTeams(group: ViewGroup, teams: Array<Team>, scoreSettings: ScoreSettings) {
            for (team in teams) {
                val teamScoreView = TeamScoreView(group.context)
                teamScoreView.bind(team, scoreSettings.score())
                group.addView(teamScoreView)
            }
        }

        @JvmStatic
        @BindingAdapter("scores")
        fun addScores(group: ViewGroup, scores: ObservableArrayList<Score>) {
            for(index in 0 until group.childCount)
            {
                val teamScoreView = group.getChildAt(index) as? TeamScoreView
                teamScoreView?.update(scores[index])
            }
        }

        @JvmStatic
        @BindingAdapter("score")
        fun showScore(view: CounterTextView, score: Int) {
            view.setTarget(score.toLong())
        }
    }
}