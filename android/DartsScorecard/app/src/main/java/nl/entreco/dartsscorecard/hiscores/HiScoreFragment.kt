package nl.entreco.dartsscorecard.hiscores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.FragmentHiscoreBinding

class HiScoreFragment : Fragment() {

    companion object {
        fun instance(position: Int): HiScoreFragment {
            return HiScoreFragment()
        }
    }

    private val adapter by lazy { HiScoreAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentHiscoreBinding>(inflater, R.layout.fragment_hiscore, container, false)
        binding.hiscoreRecycler.setHasFixedSize(true)
        binding.hiscoreRecycler.layoutManager = GridLayoutManager(context, 1)
        binding.hiscoreRecycler.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.submitList(listOf("Gary Anderson",
                "Michael v. Gerwen",
                "Davy Chisnall",
                "Jelle Klaassen",
                "Guusje").shuffled())
    }
}