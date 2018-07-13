package nl.entreco.dartsscorecard.beta

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.beta.donate.DonateCallback
import nl.entreco.dartsscorecard.beta.donate.DonateViewModel
import nl.entreco.dartsscorecard.beta.votes.VoteViewModel
import nl.entreco.dartsscorecard.databinding.ActivityBetaBinding
import nl.entreco.dartsscorecard.di.beta.BetaComponent
import nl.entreco.dartsscorecard.di.beta.BetaModule
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.donations.MakeDonationResponse

/**
 * Created by entreco on 30/01/2018.
 */
class BetaActivity : ViewModelActivity(), DonateCallback, BetaAnimator.Swapper {

    private lateinit var binding: ActivityBetaBinding
    private val component: BetaComponent by componentProvider { it.plus(BetaModule(this)) }
    private val viewModel: BetaViewModel by viewModelProvider { component.viewModel() }
    private val votesViewModel: VoteViewModel by viewModelProvider { component.votes() }
    private val donateViewModel: DonateViewModel by viewModelProvider { component.donate() }
    private val adapter: BetaAdapter by lazy { component.adapter() }
    private lateinit var animator: BetaAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_beta)
        animator = BetaAnimator(binding)
        binding.viewModel = viewModel
        binding.voteViewModel = votesViewModel
        binding.donateViewModel = donateViewModel
        binding.animator = animator

        animator.toggler = votesViewModel
        animator.swapper = this
        adapter.betaAnimator = animator

        initToolbar(toolbar(binding), R.string.title_beta)
        initRecyclerView(binding)
    }

    override fun onResume() {
        super.onResume()
        viewModel.subscribe(this, adapter)
    }

    override fun onPause() {
        super.onPause()
        viewModel.unsubscribe(this)
    }

    override fun lifeCycle(): Lifecycle {
        return lifecycle
    }

    override fun onSwapToolbar(showDetails: Boolean, title: String) {
        if (showDetails) {
            supportActionBar?.title = title
            binding.includeToolbar.collapsingToolbar.title = title
        } else {
            supportActionBar?.setTitle(R.string.title_beta)
            binding.includeToolbar.collapsingToolbar.title = getString(R.string.title_beta)
        }
    }

    override fun makeDonation(response: MakeDonationResponse) {
        donate(this, response.intent.intentSender)
    }

    override fun onDonationMade(donation: Donation) {
        votesViewModel.submitDonation(donation)
        showTankYouToast()
    }

    private fun showTankYouToast() {
        val snack = Snackbar.make(binding.root, R.string.donation_thanks, Snackbar.LENGTH_INDEFINITE)
        snack.setAction(R.string.donation_ok) { snack.dismiss() }
        snack.setActionTextColor(getColor(R.color.colorAccent))
        snack.show()
    }

    private fun initRecyclerView(binding: ActivityBetaBinding) {
        val recyclerView = binding.betaRecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)
        recyclerView.layoutManager = GridLayoutManager(binding.root.context, 2)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.isDrawingCacheEnabled = true
        recyclerView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toolbar(binding: ActivityBetaBinding): Toolbar {
        return binding.includeToolbar.toolbar
    }

    override fun onBackPressed() {
        animator.onBackPressed() ?: super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            donateOk(requestCode, resultCode, data) -> donateViewModel.onMakeDonationSuccess(data)
            requestCode == REQ_CODE_DONATE -> donateViewModel.onMakeDonationFailed()
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {

        private const val REQ_CODE_DONATE = 180

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, BetaActivity::class.java)
            context.startActivity(intent)
        }

        @JvmStatic
        fun donate(activity: BetaActivity, sender: IntentSender) {
            activity.startIntentSenderForResult(sender,
                    REQ_CODE_DONATE,
                    Intent(),
                    Integer.valueOf(0),
                    Integer.valueOf(0),
                    Integer.valueOf(0))
        }

        private fun donateOk(requestCode: Int, resultCode: Int, data: Intent?) =
                requestCode == REQ_CODE_DONATE && resultCode == Activity.RESULT_OK && data?.getIntExtra("RESPONSE_CODE", -1) == 0
    }
}