package nl.entreco.dartsscorecard.tv.launch

import android.support.v4.app.FragmentManager
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.dartsscorecard.tv.match.MatchFragment
import javax.inject.Inject

class LaunchTvNavigator @Inject constructor(@ActivityScope private val fm: FragmentManager) {
    fun attach() {
//        fm.beginTransaction()
//                .add(R.id.matchContainer, MatchFragment(), MatchFragment.TAG)
//                .commit()
    }

    fun detach() {
//        val frag = fm.findFragmentByTag(MatchFragment.TAG)
//        fm.beginTransaction()
//                .remove(frag)
//                .commit()
    }
}