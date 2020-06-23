package nl.entreco.dartsscorecard.hiscores

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import nl.entreco.dartsscorecard.di.hiscore.HiscoreComponent

interface HiscoreComponentProvider {
    fun provide(): HiscoreComponent
}

object ParentInjector {
    internal fun Fragment.parent() = lazy {
        (requireActivity() as? HiscoreComponentProvider)?.provide()
                ?: throw IllegalStateException("Hosting activity must implement HiscoreComponentProvider")
    }

    internal fun Fragment.parentViewModel() = lazy {
        ViewModelProvider(requireActivity()).get(HiScoreViewModel::class.java)
    }
}