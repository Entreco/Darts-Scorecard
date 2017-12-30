package nl.entreco.dartsscorecard.setup.players

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * Created by Entreco on 30/12/2017.
 */
abstract class BasePlayerView<in T>(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
}