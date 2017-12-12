package nl.entreco.dartsscorecard.play

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityPlay01Binding
import nl.entreco.dartsscorecard.di.viewmodel.ViewModelComponent
import nl.entreco.dartsscorecard.play.input.InputViewModel
import nl.entreco.dartsscorecard.play.main.Play01Animator
import nl.entreco.dartsscorecard.play.score.ScoreViewModel
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.usecase.GetFinishUsecase
import nl.entreco.domain.play.usecase.SetupModel
import javax.inject.Inject

class Play01Activity : ViewModelActivity() {

    @Inject lateinit var viewModel: Play01ViewModel
    @Inject lateinit var scoreViewModel: ScoreViewModel
    @Inject lateinit var inputViewModel: InputViewModel
    @Inject lateinit var finishUsecase: GetFinishUsecase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityPlay01Binding>(this, R.layout.activity_play_01)
        binding.viewModel = viewModel
        binding.inputViewModel = inputViewModel
        binding.scoreViewModel = scoreViewModel
        binding.finishUsecase = finishUsecase
        binding.animator = Play01Animator(binding)

        viewModel.retrieveGame("", retrieveSetup(), scoreViewModel)

        viewModel.addScoreListener(scoreViewModel)
        viewModel.addPlayerListener(scoreViewModel)
        viewModel.addPlayerListener(inputViewModel)
        viewModel.addSpecialEventListener(inputViewModel)
    }

    override fun inject(component: ViewModelComponent) {
        component.plus(Play01Module()).inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.play, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_play_settings -> swapStyle()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun retrieveSetup(): SetupModel {
        return SetupModel(intent.getIntExtra("1", -1),intent.getIntExtra("2", -1),intent.getIntExtra("3", -1),intent.getIntExtra("4", -1))
    }

    companion object {
        @JvmStatic
        fun startGame(context: Context, game: Game, setup: SetupModel) {
            val intent = Intent(context, Play01Activity::class.java)
            intent.putExtra("1", setup.startIndex)
            intent.putExtra("2", setup.startScore)
            intent.putExtra("3", setup.numLegs)
            intent.putExtra("4", setup.numSets)
            context.startActivity(intent)
        }
    }
}
