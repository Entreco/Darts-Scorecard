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
import nl.entreco.dartsscorecard.di.play.Play01Component
import nl.entreco.dartsscorecard.di.play.Play01Module
import nl.entreco.dartsscorecard.play.input.InputViewModel
import nl.entreco.dartsscorecard.play.main.Play01Animator
import nl.entreco.dartsscorecard.play.score.ScoreViewModel
import nl.entreco.domain.repository.TeamIdsString
import nl.entreco.domain.play.usecase.GetFinishUsecase
import nl.entreco.domain.repository.RetrieveGameRequest

class Play01Activity : ViewModelActivity() {

    private val component: Play01Component by componentProvider { it.plus(Play01Module()) }
    private val viewModel: Play01ViewModel by viewModelProvider { component.viewModel() }
    private val scoreViewModel: ScoreViewModel by viewModelProvider { component.scoreViewModel() }
    private val inputViewModel: InputViewModel by viewModelProvider { component.inputViewModel() }
    private val finishUsecase: GetFinishUsecase by componentProvider { component.finishUsecase() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityPlay01Binding>(this, R.layout.activity_play_01)
        binding.viewModel = viewModel
        binding.inputViewModel = inputViewModel
        binding.scoreViewModel = scoreViewModel
        binding.finishUsecase = finishUsecase
        binding.animator = Play01Animator(binding)

        if (savedInstanceState == null) {
            initGame()
        }

        resumeGame()
    }

    private fun initGame() {
        viewModel.load(retrieveSetup(), scoreViewModel)
    }

    private fun resumeGame() {
        viewModel.addScoreListener(scoreViewModel)
        viewModel.addPlayerListener(scoreViewModel)
        viewModel.addPlayerListener(inputViewModel)
        viewModel.addSpecialEventListener(inputViewModel)
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

    private fun retrieveSetup(): RetrieveGameRequest {
        return RetrieveGameRequest(intent.getLongExtra("gameId", -1),
                TeamIdsString(intent.getStringExtra("teamIds")),
                intent.getParcelableExtra("create"))
    }

    companion object {
        @JvmStatic
        fun startGame(context: Context, retrieve: RetrieveGameRequest) {
            val intent = Intent(context, Play01Activity::class.java)
            intent.putExtra("gameId", retrieve.gameId)
            intent.putExtra("teamIds", retrieve.teamIds.toString())
            intent.putExtra("create", retrieve.create)
            context.startActivity(intent)
        }
    }
}
