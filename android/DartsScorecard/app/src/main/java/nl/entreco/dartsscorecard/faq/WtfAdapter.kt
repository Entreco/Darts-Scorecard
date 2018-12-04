package nl.entreco.dartsscorecard.faq

import androidx.lifecycle.Observer
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.TestableAdapter
import nl.entreco.dartsscorecard.databinding.WtfViewBinding
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import nl.entreco.domain.wtf.SubmitViewedItemRequest
import nl.entreco.domain.wtf.SubmitViewedItemUsecase
import nl.entreco.domain.wtf.WtfItem
import java.util.*
import javax.inject.Inject

class WtfAdapter @Inject constructor(private val bg: Background, private val fg: Foreground,
                                     private val submitViewedItemUsecase: SubmitViewedItemUsecase) : TestableAdapter<WtfView>(), Observer<List<WtfItem>>, WtfToggler, WtfSearchable {

    private var searchText : String = ""
    private var allItems: MutableMap<String, WtfItem> = mutableMapOf()
    private val visibleItems: MutableList<WtfItem> = mutableListOf()
    private val queue: Queue<List<WtfItem>> = ArrayDeque()
    private var expandedItem: String? = null
    private val lock: Any = Any()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WtfView {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<WtfViewBinding>(inflater, R.layout.wtf_view, parent, false)
        return WtfView(binding)
    }

    override fun onBindViewHolder(holder: WtfView, position: Int) {
        synchronized(lock) {
            val currentItem = visibleItems[position]
            holder.bind(currentItem, currentItem.docId != expandedItem, this)
        }
    }

    override fun getItemCount(): Int {
        synchronized(lock) {
            return visibleItems.size
        }
    }

    override fun search(text: CharSequence) {
        if (text.isNotEmpty() && text.length >= 3) {
            searchText = text.toString().toLowerCase()
            onChanged(allItems.values.toList())
        } else {
            clearSearch()
        }
    }

    private fun clearSearch() {
        synchronized(lock) {
            searchText = ""
            onChanged(allItems.values.toList())
        }
    }

    override fun toggle(item: WtfItem) {
        synchronized(lock) {
            expandedItem = if (item.docId == expandedItem) null
            else item.docId
            notifyItemRangeChanged(0, visibleItems.size)
            submitViewedItemUsecase.exec(SubmitViewedItemRequest(item))
        }
    }

    override fun onChanged(features: List<WtfItem>?) {
        if (features != null) {
            queue.add(features)
            if (queue.size <= 1) {
                calculateDiff(features)
            }
        }
    }

    private fun calculateDiff(features: List<WtfItem>) {
        bg.post(Runnable {

            synchronized(lock) {
                features.forEach { item ->
                    allItems[item.docId] = item
                }
                val filteredSorted = features.filter { doFilter(it, searchText) }.sortedByDescending { score(it, searchText) }
                val diff = DiffUtil.calculateDiff(WtfDiffCalculator(visibleItems, filteredSorted), true)
                fg.post(Runnable {
                    queue.remove()
                    updateItems(filteredSorted, diff)
                    if (queue.size > 0) {
                        calculateDiff(queue.peek())
                    }
                })
            }
        })
    }

    private fun doFilter(item: WtfItem, searchText: String): Boolean {
        if(searchText.isBlank()) return true
        return item.title.toLowerCase().contains(searchText) || item.description.toLowerCase().contains(searchText)
    }

    private fun score(item: WtfItem, searchText: String): Int {
        if(searchText.isBlank()) return 0
        return if(item.title.toLowerCase().contains(searchText)) 10 else 0 +
                if(item.description.toLowerCase().contains(searchText)) 5 else 0
    }

    private fun updateItems(features: List<WtfItem>, diff: DiffUtil.DiffResult) {
        synchronized(lock) {
            visibleItems.clear()
            visibleItems.addAll(features)
            diff.dispatchUpdatesTo(this)
        }
    }
}