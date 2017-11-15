package nl.entreco.dartsscorecard.play

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.NonNull
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityPlay01Binding
import nl.entreco.dartsscorecard.di.viewmodel.ViewModelComponent
import nl.entreco.domain.play.Arbiter
import nl.entreco.domain.play.Score
import javax.inject.Inject

class Play01Activity : ViewModelActivity() {

    @Inject @NonNull lateinit var viewModel: Play01ViewModel

    private val arbiter by lazy { Arbiter(Score(), 2) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityPlay01Binding>(this, R.layout.activity_play_01)
        binding.viewModel = viewModel
    }

    override fun inject(component: ViewModelComponent) {
        component.plus(Play01Module(arbiter)).inject(this)
    }
}
