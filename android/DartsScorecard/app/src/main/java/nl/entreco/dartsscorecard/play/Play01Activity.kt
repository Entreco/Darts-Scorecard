package nl.entreco.dartsscorecard.play

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityPlay01Binding
import nl.entreco.dartsscorecard.play.input.InputViewModel
import nl.entreco.dartsscorecard.play.score.ScoreViewModel
import nl.entreco.dartsscorecard.play.score.TeamScoreAdapter
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.settings.ScoreSettings

class Play01Activity : ViewModelActivity() {

    private val settings = ScoreSettings()
    private val play01Module by lazy { Play01Module(settings, arrayOf(Team(Player("remco")), Team(Player("co")))) }
    private val viewModel : Play01ViewModel by viewModelProvider {
        component.plus(play01Module).viewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityPlay01Binding>(this, R.layout.activity_play_01)
        binding.viewModel = viewModel
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.play, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menu_play_settings -> swapStyle()
        }
        return super.onOptionsItemSelected(item)
    }
}
