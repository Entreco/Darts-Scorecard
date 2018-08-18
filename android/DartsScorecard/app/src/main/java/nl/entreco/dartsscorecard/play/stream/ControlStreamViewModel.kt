package nl.entreco.dartsscorecard.play.stream

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.di.play.Play01Scope
import nl.entreco.dartsscorecard.play.Play01Animator
import nl.entreco.dartsscorecard.play.Play01Navigator
import nl.entreco.domain.streaming.ConnectionState
import nl.entreco.domain.streaming.Unknown
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject


class ControlStreamViewModel @Inject constructor() : BaseViewModel() {

    private val isStreaming = AtomicBoolean(false)
    val micEnabled = ObservableBoolean(true)
    val isShowingFront = ObservableBoolean(true)
    val state = ObservableField<ConnectionState>(Unknown)

    fun toggleStream(navigator: Play01Navigator, animator: Play01Animator) {
        if (isStreaming.get()) {
            isStreaming.set(false)
            animator.expand()
            navigator.detachVideoStream()
            sendDisconnect(navigator)
        } else {
            isStreaming.set(true)
            animator.collapse()
            navigator.attachVideoStream()
        }
    }

    fun sendDisconnect(navigator: Play01Navigator){
        navigator.streamController()?.sendDisconnect()
    }

    fun toggleCamera(navigator: Play01Navigator){
        isShowingFront.set(!isShowingFront.get())
        navigator.streamController()?.toggleCamera()
    }

    fun toggleMic(navigator: Play01Navigator){
        micEnabled.set(!micEnabled.get())
        navigator.streamController()?.toggleMic()
    }

    fun connectionState(connectionState: ConnectionState) {
        state.set(connectionState)
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