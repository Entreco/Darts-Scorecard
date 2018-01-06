package nl.entreco.dartsscorecard.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater

/**
 * Created by Entreco on 31/12/2017.
 */
abstract class TestableAdapter<T : RecyclerView.ViewHolder?> : RecyclerView.Adapter<T>() {

    protected class LazyInflater(context: Context) {
        val inflater: LayoutInflater by lazy { LayoutInflater.from(context) }
    }

    protected fun tryNotifyItemInserted(position: Int) {
        try {
            notifyItemInserted(position)
        } catch (ignore: NullPointerException) {
        }
    }

    protected fun tryNotifyItemChanged(position: Int) {
        try {
            notifyItemChanged(position)
        } catch (ignore: NullPointerException) {
        }
    }

    protected fun tryNotifyItemRangeRemoved(position: Int, count: Int) {
        try {
            notifyItemRangeRemoved(position, count)
        } catch (ignore: NullPointerException) {
        }
    }

    protected fun tryNotifyItemRangeChanged(position: Int, count: Int) {
        try {
            notifyItemRangeChanged(position, count)
        } catch (ignore: NullPointerException) {
        }
    }
}