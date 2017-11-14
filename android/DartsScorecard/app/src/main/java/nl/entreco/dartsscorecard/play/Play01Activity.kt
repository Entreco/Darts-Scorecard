package nl.entreco.dartsscorecard.play

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.NonNull
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityPlay01Binding
import nl.entreco.domain.play.Arbiter
import nl.entreco.domain.play.Score
import javax.inject.Inject

class Play01Activity : ViewModelActivity() {

    @Inject @NonNull lateinit var vm : Play01ViewModel

    private val arbiter by lazy { Arbiter(Score(), 2) }
    private val component by lazy { viewmodelComponent.plus(Play01Module(arbiter)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityPlay01Binding>(this, R.layout.activity_play_01)
        binding.viewModel = vm
    }
}
