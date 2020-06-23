package nl.entreco.dartsscorecard.hiscores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private val viewModel by lazy { ViewModelProvider(requireActivity()).get(HiScoreViewModel::class.java) }
    private val navigator by lazy { HiScoreNavigator(requireActivity()) }
    private val adapter by lazy { HiScoreAdapter() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentHiscoreBinding>(inflater, R.layout.fragment_hiscore, container, false)
        initRecyclerView(binding.hiscoreRecycler)
        handlePagerPosition()
        viewModel.events().observe(viewLifecycleOwner, navigator)
        return binding.root
    }

    private fun handlePagerPosition() {
        val position = arguments?.getInt(EXTRA_POS) ?: 0
        viewModel.dataAtPosition(position).observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }
}