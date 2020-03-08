package nl.entreco.dartsscorecard.beta

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.beta.donate.DonateViewModel
import nl.entreco.dartsscorecard.beta.donate.DonationEvent
import nl.entreco.dartsscorecard.beta.votes.VoteViewModel
import nl.entreco.dartsscorecard.databinding.ActivityBetaBinding
import nl.entreco.dartsscorecard.di.beta.BetaComponent
import nl.entreco.dartsscorecard.di.beta.BetaModule
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.domain.repository.BillingRepo

/**
 * Created by entreco on 30/01/2018.
 */
class BetaActivity : ViewModelActivity(), BetaAnimator.Swapper {

    private lateinit var binding: ActivityBetaBinding
    private val component: BetaComponent by componentProvider {
        it.plus(BetaModule(this) { response ->

            Log.i("IAB", "BetaActivity: $response")

            when (response) {
                is MakePurchaseResponse.Updated     -> donateViewModel.onUpdate(response.purchases)
                is MakePurchaseResponse.Purchased   -> {
                    donateViewModel.onDonated(response.donation, response.orderId)
                    votesViewModel.submitDonation(response.donation) {
                        showTankYouToast()
                    }
                }
                is MakePurchaseResponse.Donations   -> donateViewModel.showDonations(response.donations)
                is MakePurchaseResponse.Unavailable -> donateViewModel.showDonations(emptyList())
                is MakePurchaseResponse.Cancelled   -> donateViewModel.onCancelled("User Cancelled")
                is MakePurchaseResponse.Unknown     -> donateViewModel.onCancelled("Unknown error: $response")
            }
        })
    }
    private val viewModel: BetaViewModel by viewModelProvider { component.viewModel() }
    private val votesViewModel: VoteViewModel by viewModelProvider { component.votes() }
    private val donateViewModel: DonateViewModel by viewModelProvider { component.donate() }
    private val billingService: BillingRepo by lazy { component.billing() }
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

        billingService.start()
        donateViewModel.events().observe(this, Observer { event ->
            when (event) {
                is DonationEvent.Purchase -> billingService.purchase(event.donation.sku)
            }
        })

        initToolbar(toolbar(binding), R.string.title_beta)
        initRecyclerView(binding)
    }

    override fun onResume() {
        super.onResume()
        viewModel.subscribe(this, adapter)
        billingService.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        billingService.stop()
    }

    override fun onPause() {
        super.onPause()
        viewModel.unsubscribe(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.beta, menu)
        return super.onCreateOptionsMenu(menu)
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

    private fun initRecyclerView(binding: ActivityBetaBinding) {
        val recyclerView = binding.betaRecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)
        recyclerView.layoutManager = GridLayoutManager(binding.root.context, 2)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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

    private fun showTankYouToast() {
        val snack = Snackbar.make(binding.root, R.string.donation_thanks,
                Snackbar.LENGTH_INDEFINITE)
        snack.setAction(R.string.donation_ok) { snack.dismiss() }
        snack.setActionTextColor(getColor(R.color.colorAccent))
        snack.show()
    }

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, BetaActivity::class.java)
            context.startActivity(intent)
        }
    }
}