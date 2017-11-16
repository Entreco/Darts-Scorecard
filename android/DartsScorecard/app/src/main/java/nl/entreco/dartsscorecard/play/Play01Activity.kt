package nl.entreco.dartsscorecard.play

import android.databinding.DataBindingUtil
import android.os.Bundle
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityPlay01Binding
import nl.entreco.domain.play.model.Arbiter
import nl.entreco.domain.play.model.Score

class Play01Activity : ViewModelActivity() {

    private val arbiter by lazy { Arbiter(Score(), 2) }
    private val viewModel by viewModelProvider {
        component.plus(Play01Module(arbiter)).viewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityPlay01Binding>(this, R.layout.activity_play_01)
        binding.viewModel = viewModel
    }
}
