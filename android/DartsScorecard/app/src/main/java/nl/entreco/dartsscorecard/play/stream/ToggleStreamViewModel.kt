package nl.entreco.dartsscorecard.play.stream

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.Play01Navigator
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject


class ToggleStreamViewModel @Inject constructor() : BaseViewModel() {

    private val isStreaming = AtomicBoolean(false)

    fun toggleStream(navigator: Play01Navigator) {
        if (isStreaming.get()) {
            isStreaming.set(false)
            navigator.detachVideoStream()
        } else {
            isStreaming.set(true)
            navigator.attachVideoStream()
        }
    }

    @DrawableRes
    fun menuIcon(): Int {
        return if (isStreaming.get()) R.drawable.ic_live_stop else R.drawable.ic_live_start
    }

    @StringRes
    fun menuTitle(): Int {
        return if (isStreaming.get()) R.string.stream_stop else R.string.stream_start
    }

}