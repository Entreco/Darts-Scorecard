package nl.entreco.dartsscorecard.tv.match

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v17.leanback.app.BrandedSupportFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.FragmentMatchBinding

class MatchFragment : BrandedSupportFragment() {

    companion object {
        const val TAG : String = "MatchFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentMatchBinding>(inflater, R.layout.fragment_match, container, false)
        return binding.root
    }
}