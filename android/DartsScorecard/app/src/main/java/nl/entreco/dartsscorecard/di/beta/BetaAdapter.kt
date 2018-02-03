package nl.entreco.dartsscorecard.di.beta

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.TestableAdapter
import nl.entreco.dartsscorecard.databinding.BetaViewBinding
import nl.entreco.domain.beta.Feature
import javax.inject.Inject

/**
 * Created by entreco on 30/01/2018.
 */
class BetaAdapter @Inject constructor() : TestableAdapter<BetaView>(), Observer<List<Feature>> {

    private val items: MutableList<Feature> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BetaView {
        val inflater = LayoutInflater.from(parent?.context)
        val binding = DataBindingUtil.inflate<BetaViewBinding>(inflater, R.layout.beta_view, parent, false)
        return BetaView(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BetaView?, position: Int) {
        holder?.bind(items[position])
    }

    override fun onChanged(features: List<Feature>?) {
        if (features != null) {
            val diff = DiffUtil.calculateDiff(BetaDiffCalculator(items, features))
            items.clear()
            items.addAll(features)
            diff.dispatchUpdatesTo(this)
        }
    }

}