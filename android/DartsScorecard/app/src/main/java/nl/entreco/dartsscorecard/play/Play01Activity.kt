package nl.entreco.dartsscorecard.play

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityPlay01Binding
import nl.entreco.dartsscorecard.di.play.Play01Component
import nl.entreco.dartsscorecard.di.play.Play01Module
import nl.entreco.dartsscorecard.dynamic.Installer
import nl.entreco.dartsscorecard.dynamic.SoundInstalledCallback
import nl.entreco.dartsscorecard.play.input.InputViewModel
import nl.entreco.dartsscorecard.play.live.LiveStatViewModel
import nl.entreco.dartsscorecard.play.score.ScoreViewModel
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.domain.play.finish.GetFinishUsecase
import nl.entreco.domain.play.start.Play01Request
import nl.entreco.domain.repository.BillingRepo
import nl.entreco.domain.setup.game.CreateGameResponse
import nl.entreco.libads.ui.AdViewModel


class Play01Activity : ViewModelActivity(), SoundInstalledCallback {

    private val component: Play01Component by componentProvider {
        it.plus(Play01Module(this) { response ->
            if (response is MakePurchaseResponse.Updated) adViewModel.onPurchasesRetrieved(response)
        })
    }
    private val viewModel: Play01ViewModel by viewModelProvider { component.viewModel() }
    private val scoreViewModel: ScoreViewModel by viewModelProvider { component.scoreViewModel() }
    private val inputViewModel: InputViewModel by viewModelProvider { component.inputViewModel() }
    private val statViewModel: LiveStatViewModel by viewModelProvider { component.statViewModel() }
    private val finishUsecase: GetFinishUsecase by componentProvider { component.finishUsecase() }
    private val adViewModel: AdViewModel by viewModelProvider { component.ads() }
    private val installer: Installer by lazy { component.installer() }
    private val billingRepo: BillingRepo by lazy { component.billing() }
    private val navigator: Play01Navigator by lazy { component.navigator() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val binding = DataBindingUtil.setContentView<ActivityPlay01Binding>(this, R.layout.activity_play_01)
        binding.viewModel = viewModel
        binding.inputViewModel = inputViewModel
        binding.statViewModel = statViewModel
        binding.scoreViewModel = scoreViewModel
        binding.finishUsecase = finishUsecase
        binding.animator = Play01Animator(binding)
        binding.navigator = navigator

        if (savedInstanceState == null) {
            initGame()
        }

        billingRepo.start()
        initToolbar(toolbar(binding))
        resumeGame()
    }

    override fun onResume() {
        super.onResume()
        billingRepo.resume()
        viewModel.resume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stop()
        billingRepo.stop()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun initGame() {
        viewModel.load(retrieveSetup(intent), scoreViewModel, statViewModel)
    }

    private fun toolbar(binding: ActivityPlay01Binding): Toolbar {
        return binding.includeToolbar.toolbar
    }

    private fun resumeGame() {
        viewModel.registerListeners(scoreViewModel, statViewModel, inputViewModel, scoreViewModel, inputViewModel)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.play, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        viewModel.initToggleMenuItem(menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_play_settings -> {
                swapStyle()
                viewModel.loading.set(true)
            }
            R.id.menu_music_settings -> {
                if (!item.isChecked) installer.install(this)
                viewModel.toggleBgMusic(item)
            }
            R.id.menu_sound_settings -> {
                if (!item.isChecked) installer.install(this)
                viewModel.toggleMasterCaller(item)
            }
            R.id.menu_delete_match -> {
                viewModel.askToDeleteMatch {
                    onBackPressed()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        @JvmStatic
        fun retrieveSetup(intent: Intent) = Play01Request(
                intent.getLongExtra("gameId", -1),
                intent.getStringExtra("teamIds").orEmpty(),
                intent.getIntExtra("startScore", -1),
                intent.getIntExtra("startIndex", -1),
                intent.getIntExtra("legs", -1),
                intent.getIntExtra("sets", -1))

        @JvmStatic
        fun startGame(context: Context, create: CreateGameResponse) {
            val intent = Intent(context, Play01Activity::class.java)
            intent.putExtra("gameId", create.gameId)
            intent.putExtra("teamIds", create.teamIds)
            intent.putExtra("startScore", create.startScore)
            intent.putExtra("startIndex", create.startIndex)
            intent.putExtra("legs", create.numLegs)
            intent.putExtra("sets", create.numSets)
            context.startActivity(intent)
        }
    }

    override fun onComplete() {
        navigator.doneSnackbar()
    }

    override fun onProgress(bytes: Long, totalBytes: Long) {
        navigator.showSnackbar((bytes.toFloat() / totalBytes.toFloat() * 100).toInt())
    }

    override fun onError() {
        Toast.makeText(this, "Unable to download Sounds", Toast.LENGTH_SHORT).show()
    }
}
