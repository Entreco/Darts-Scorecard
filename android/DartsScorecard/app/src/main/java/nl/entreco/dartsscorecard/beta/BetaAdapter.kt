package nl.entreco.dartsscorecard.beta

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.TestableAdapter
import nl.entreco.dartsscorecard.databinding.BetaViewBinding
import nl.entreco.domain.beta.Feature
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import java.util.*
import javax.inject.Inject

/**
 * Created by entreco on 30/01/2018.
 */
class BetaAdapter @Inject constructor(private val bg: Background, private val fg: Foreground) : TestableAdapter<BetaView>(), Observer<List<Feature>> {

    private val items: MutableList<Feature> = mutableListOf()
    private val queue: Queue<List<Feature>> = ArrayDeque()
    var betaAnimator: BetaAnimator? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BetaView {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<BetaViewBinding>(inflater, R.layout.beta_view, parent, false)
        return BetaView(binding)
    }

    override fun onBindViewHolder(holder: BetaView, position: Int) {
        holder.bind(items[position], betaAnimator)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onChanged(features: List<Feature>?) {
        if (features != null) {
            queue.add(features)
            if (queue.size <= 1) {
                calculateDiff(features)
            }
        }
    }

    private fun calculateDiff(features: List<Feature>) {
        bg.post(Runnable {
            val diff = DiffUtil.calculateDiff(BetaDiffCalculator(items, features), true)
            fg.post(Runnable {
                queue.remove()
                updateItems(features, diff)
                if (queue.size > 0) {
                    calculateDiff(queue.peek())
                }
            })
        })
    }

    private fun updateItems(features: List<Feature>, diff: DiffUtil.DiffResult) {
        items.clear()
        items.addAll(features)
        diff.dispatchUpdatesTo(this)
    }
}
