package nl.entreco.dartsscorecard.beta

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.beta.donate.DonateCallback
import nl.entreco.dartsscorecard.beta.donate.DonateViewModel
import nl.entreco.dartsscorecard.beta.votes.VoteViewModel
import nl.entreco.dartsscorecard.databinding.ActivityBetaBinding
import nl.entreco.dartsscorecard.di.beta.BetaComponent
import nl.entreco.dartsscorecard.di.beta.BetaModule
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.donations.MakePurchaseResponse

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.beta, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun lifeCycle() = lifecycle

    override fun onSwapToolbar(showDetails: Boolean, title: String) {
        if (showDetails) {
            supportActionBar?.title = title
            binding.includeToolbar.collapsingToolbar.title = title
        } else {
            supportActionBar?.setTitle(R.string.title_beta)
            binding.includeToolbar.collapsingToolbar.title = getString(R.string.title_beta)
        }
    }

    override fun makeDonation(response: MakePurchaseResponse) {
        handleDonation(response)
    }

    override fun onDonationMade(donation: Donation) {
        votesViewModel.submitDonation(donation)
        showTankYouToast()
    }

    private fun showTankYouToast() {
        val snack = Snackbar.make(binding.root, R.string.donation_thanks,
                Snackbar.LENGTH_INDEFINITE)
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

    private fun handleDonation(result: MakePurchaseResponse) {
        when (result) {
            is MakePurchaseResponse.Purchased    -> donateViewModel.onMakeDonationSuccess(result)
            is MakePurchaseResponse.Acknowledged -> toast("Donation Acknowledged")
            is MakePurchaseResponse.Consumed     -> toast("Donation Consumed")
            is MakePurchaseResponse.Cancelled    -> failAndToast("Donation Cancelled")
            is MakePurchaseResponse.Error        -> failAndToast("Donation Error:$result")
            is MakePurchaseResponse.Pending      -> toast("Donation Pending - Please follow instructions")
//            else -> failAndToast("Donation not confirmed - Unknown error")
        }
    }

    private fun failAndToast(message: String) {
        donateViewModel.onMakeDonationFailed(message)
        toast(message)
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, BetaActivity::class.java)
            context.startActivity(intent)
        }
    }
}