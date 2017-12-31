package nl.entreco.dartsscorecard.setup.players

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * Created by Entreco on 31/12/2017.
 */
class PlayersWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), PlayersObserver.Callback {

    private val observer = PlayersObserver(this)

    var adapter: PlayerAdapter? = null
        set(value) {
            field = value
            if (value != null) {
                value.registerAdapterDataObserver(observer)
                updateItems(value)
            }
        }

    init {
        orientation = VERTICAL
        layoutTransition = LayoutTransition()
    }

    override fun onPlayersInserted(positionStart: Int, itemCount: Int) {
        for (index in positionStart until positionStart + itemCount) {
            addPlayerView(adapter!!, index - 1)
        }
    }

    private fun updateItems(adapter: PlayerAdapter) {
        for (index in 0 until adapter.itemCount) {
            addPlayerView(adapter, index)
        }
    }

    private fun addPlayerView(adapter: PlayerAdapter, index: Int) {
        val type = adapter.getItemViewType(index)
        val viewHolder = createViewHolder(type, adapter)
        if (viewHolder != null) {
            adapter.bindViewHolder(viewHolder, index)
            addView(viewHolder.itemView)
        }
    }

    private fun createViewHolder(type: Int, adapter: PlayerAdapter): SelectPlayerView? {
        return adapter.createViewHolder(this, type)
    }
}