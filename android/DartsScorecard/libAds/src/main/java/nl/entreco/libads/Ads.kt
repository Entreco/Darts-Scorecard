package nl.entreco.libads

import android.view.View

interface Ads {
    fun init(appId: String)
    fun load(view: View, done: (Boolean) -> Unit)
}