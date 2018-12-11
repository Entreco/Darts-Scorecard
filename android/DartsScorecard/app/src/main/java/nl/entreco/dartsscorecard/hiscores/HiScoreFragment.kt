package nl.entreco.dartsscorecard.hiscores

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.FragmentHiscoreBinding
import nl.entreco.domain.hiscores.HiScoreItem
import nl.entreco.domain.hiscores.SortHiScoresUsecase
import nl.entreco.shared.threading.BgExecutor
import nl.entreco.shared.threading.FgExecutor

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

    private val loadingViewModel by lazy { LoadingViewModel(SortHiScoresUsecase(BgExecutor(), FgExecutor())) }
    private val adapter by lazy { HiScoreAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentHiscoreBinding>(inflater,
                R.layout.fragment_hiscore, container, false)
        binding.viewModel = loadingViewModel
        binding.hiscoreRecycler.setHasFixedSize(true)
        binding.hiscoreRecycler.layoutManager = GridLayoutManager(context, 1)
        binding.hiscoreRecycler.itemAnimator = DefaultItemAnimator()
        binding.hiscoreRecycler.adapter = adapter

        val position = arguments?.getInt(EXTRA_POS) ?: 0
        val items = viewModel.dataAtPosition(position)
        loadingViewModel.showSorted(items) {
            adapter.submitList(it)
        }

        return binding.root
    }
}