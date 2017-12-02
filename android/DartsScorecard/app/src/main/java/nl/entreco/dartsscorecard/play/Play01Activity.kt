package nl.entreco.dartsscorecard.play

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityPlay01Binding
import nl.entreco.dartsscorecard.play.score.ReadyListener
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.settings.ScoreSettings

class Play01Activity : ViewModelActivity(), ReadyListener {

    private val play01Module by lazy { Play01Module(ScoreSettings(teamStartIndex = 1), arrayOf(Team(Player("remco"), Player("sibbel")), Team("Boeffie"), Team(Player("eva"), Player("guusje"), Player("Beer"))), this) }
    private val viewModel: Play01ViewModel by viewModelProvider {
        component.plus(play01Module).viewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityPlay01Binding>(this, R.layout.activity_play_01)
        binding.viewModel = viewModel

        val fab = binding.includeInput?.fab!!
        val inputSheet = binding.includeInput?.inputSheet!!
        val mainSheet = binding.includeMain?.mainSheet!!
        val scoreSheet = binding.includeScore?.scoreSheet!!

        val behaviour = BottomSheetBehavior.from(inputSheet)
        behaviour.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

                // Slide Out Bottom/Top
                fab.animate().scaleY(slideOffset).scaleX(slideOffset).setDuration(0).start()
                scoreSheet.animate().translationY((1 - slideOffset) * -(scoreSheet.height - 100)).setDuration(0).start()

                // Fade Out
                inputSheet.animate().alpha(slideOffset).setDuration(0).start()
                scoreSheet.animate().alpha(slideOffset).setDuration(0).start()

                // Fade In MainSheet
                mainSheet.animate().alpha(1 - Math.sqrt(slideOffset.toDouble()).toFloat()).setDuration(0).start()
                mainSheet.animate().translationY((slideOffset) * -100).setDuration(0).start()

                // Fly In stats
                binding.includeMain.stat1.animate().translationY(-100 * slideOffset).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(0).start()
                binding.includeMain.stat2.animate().translationY(-200 * slideOffset).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(0).start()
                binding.includeMain.stat3.animate().translationY(-300 * slideOffset).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(0).start()
                binding.includeMain.stat4.animate().translationY(-400 * slideOffset).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(0).start()
                binding.includeMain.stat5.animate().translationY(-500 * slideOffset).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(0).start()
                binding.includeMain.stat6.animate().translationY(-600 * slideOffset).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(0).start()
                binding.includeMain.stat7.animate().translationY(-700 * slideOffset).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(0).start()
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {}
        })
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onReady() {
        viewModel.onReady()
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
}
