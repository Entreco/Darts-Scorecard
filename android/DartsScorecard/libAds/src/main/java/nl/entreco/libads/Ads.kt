package nl.entreco.libads

import android.view.View

interface Ads {
    fun init()
    fun load(view: View, done: (Boolean) -> Unit)
}