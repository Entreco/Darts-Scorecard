package nl.entreco.dartsscorecard.di.beta

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.TestableAdapter
import nl.entreco.dartsscorecard.beta.BetaView
import nl.entreco.dartsscorecard.databinding.BetaViewBinding
import nl.entreco.domain.beta.Feature
import javax.inject.Inject

/**
 * Created by entreco on 30/01/2018.
 */
class BetaAdapter @Inject constructor(): TestableAdapter<BetaView>() {

    private val items : MutableList<Feature> = mutableListOf()

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

    fun setFeatures(features: List<Feature>) {
        items.clear()
        items.addAll(features)
        notifyItemRangeInserted(0, features.size)
    }
}