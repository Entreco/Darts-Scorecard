package nl.entreco.dartsscorecard.play.stream

import android.databinding.BindingAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import nl.entreco.dartsscorecard.R
import nl.entreco.domain.streaming.*

object StreamBinding {

    @JvmStatic
    @BindingAdapter("streamVisibility")
    fun updateStreamVisibility(view: ViewGroup, state: ConnectionState) {
        updateVisibility(state, view)
    }

    @JvmStatic
    @BindingAdapter("streamState")
    fun updateStreamState(view: TextView, state: ConnectionState) {
        updateText(state, view)
        updateDrawable(state, view)
        updateVisibility(state, view)
    }

    private fun updateVisibility(state: ConnectionState, view: View) {
        when (state) {
            Unknown -> view.visibility = View.GONE
            else -> view.visibility = View.VISIBLE
        }
    }

    private fun updateText(state: ConnectionState, view: TextView) {
        when (state) {
            is Unknown -> view.text = "Unknown"
            is Initializing -> view.text = "Init"
            is ReadyToConnect -> view.text = "Ready to Stream"
            is Connecting -> view.text = "Connecting"
            is Connected -> view.text = "Streaming"
            is Disconnecting -> view.text = "Disconnecting"
            is Disconnected -> view.text = "Disconnected"
            is Killing -> view.text = "Shutting down"
        }
    }

    private fun updateDrawable(state: ConnectionState, view: TextView) {
        when (state) {
            is Unknown -> view.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.stream_state_connect, 0)
            is Initializing -> view.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.stream_state_connect, 0)
            is ReadyToConnect -> view.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.stream_state_stopped, 0)
            is Connecting -> view.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.stream_state_live, 0)
            is Connected -> view.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.stream_state_live, 0)
            is Disconnecting -> view.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.stream_state_live, 0)
            is Disconnected -> view.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.stream_state_stopped, 0)
            is Killing -> view.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.stream_state_stopped, 0)
        }
    }
}