package nl.entreco.dartsscorecard.hiscores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.FragmentHiscoreBinding

class HiScoreFragment : Fragment() {

    companion object {
        private const val EXTRA_POS = "extra_position"
        fun instance(position: Int): HiScoreFragment {
            val fragment = HiScoreFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_POS, position)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(HiScoreViewModel::class.java)
    }

    private val navigator by lazy { HiScoreNavigator(activity!!) }
    private val adapter by lazy { HiScoreAdapter(navigator) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentHiscoreBinding>(inflater,
                R.layout.fragment_hiscore, container, false)
        binding.hiscoreRecycler.setHasFixedSize(true)
        binding.hiscoreRecycler.layoutManager = GridLayoutManager(context, 1)
        binding.hiscoreRecycler.adapter = adapter

        val position = arguments?.getInt(EXTRA_POS) ?: 0
        viewModel.dataAtPosition(position).observe(this, Observer {
            adapter.submitList(it)
        })
        return binding.root
    }
}