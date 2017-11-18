package nl.entreco.dartsscorecard.play

import android.databinding.DataBindingUtil
import android.os.Bundle
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityPlay01Binding
import nl.entreco.dartsscorecard.play.input.InputViewModel
import nl.entreco.dartsscorecard.play.score.ScoreViewModel
import nl.entreco.domain.settings.ScoreSettings

class Play01Activity : ViewModelActivity() {

    private val numPlayers = 2
    private val settings = ScoreSettings()
    private val viewModel : Play01ViewModel by viewModelProvider {
        component.plus(Play01Module(2, settings)).viewModel()
    }
    private val scoreViewModel : ScoreViewModel by viewModelProvider {
        component.plus(Play01Module(numPlayers, settings)).scoreViewModel()
    }
    private val inputViewModel : InputViewModel by viewModelProvider {
        component.plus(Play01Module(numPlayers, settings)).inputViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        theme.applyStyle(R.style.Pdc, true)
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityPlay01Binding>(this, R.layout.activity_play_01)
        binding.viewModel = viewModel
        binding.scoreViewModel = scoreViewModel
        binding.inputViewModel = inputViewModel

        viewModel.scoreKeeper = scoreViewModel
    }
}
