package nl.entreco.dartsscorecard.play.stream

import android.databinding.BindingAdapter
import android.view.View
import android.widget.TextView
import nl.entreco.dartsscorecard.R
import nl.entreco.domain.streaming.*

object StreamBinding {

    @JvmStatic
    @BindingAdapter("streamVisibility")
    fun updateStreamState(view: View, state: ConnectionState) {
        when(state){
            Unknown -> view.visibility = View.GONE
            else -> view.visibility = View.VISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter("streamState")
    fun updateStreamState(view: TextView, state: ConnectionState) {
        updateText(state, view)
        updateDrawable(state, view)
    }

    private fun updateText(state: ConnectionState, view: TextView) {
        when (state) {
            is Unknown -> view.setText("Unknown")
            is Initializing -> view.setText("Init")
            is ReadyToConnect -> view.setText("Ready to Stream")
            is Connecting -> view.setText("Connecting")
            is Connected -> view.setText("Streaming")
            is Disconnecting -> view.setText("Disconnecting")
            is Disconnected -> view.setText("Disconnected")
            is Killing -> view.setText("Shutting down")
        }
    }

    private fun updateDrawable(state: ConnectionState, view: TextView) {
        when (state) {
            is Unknown -> view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stream_state_connect, 0)
            is Initializing -> view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stream_state_connect, 0)
            is ReadyToConnect -> view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stream_state_stopped, 0)
            is Connecting -> view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stream_state_live, 0)
            is Connected -> view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stream_state_live, 0)
            is Disconnecting -> view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stream_state_live, 0)
            is Disconnected -> view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stream_state_stopped, 0)
            is Killing -> view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stream_state_stopped, 0)
        }
    }
}