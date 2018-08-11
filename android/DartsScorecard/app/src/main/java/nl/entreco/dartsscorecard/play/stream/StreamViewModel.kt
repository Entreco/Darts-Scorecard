package nl.entreco.dartsscorecard.play.stream

import android.databinding.ObservableInt
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.Play01Navigator
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject


class StreamViewModel @Inject constructor() : BaseViewModel() {

    val title = ObservableInt(R.string.live_stream_start)
    private var isStreaming = AtomicBoolean(false)

    fun toggleStream(navigator: Play01Navigator) {
        if (isStreaming.get()) {
            isStreaming.set(false)
            title.set(R.string.live_stream_start)
            navigator.detachVideoStream()
        } else {
            isStreaming.set(true)
            title.set(R.string.live_stream_connecting)
            navigator.attachVideoStream()
        }
    }

}