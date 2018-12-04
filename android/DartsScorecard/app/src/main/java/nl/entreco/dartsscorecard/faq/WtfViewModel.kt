package nl.entreco.dartsscorecard.faq

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.wtf.SubscribeToWtfsUsecase
import nl.entreco.domain.wtf.WtfItem
import javax.inject.Inject

class WtfViewModel @Inject constructor(private val subscribeToWtfUsecase: SubscribeToWtfsUsecase) : BaseViewModel() {

    private val items: MutableLiveData<List<WtfItem>> = MutableLiveData()

    private fun refresh() {
        subscribeToWtfUsecase.subscribe({
            items.value = it
        }, {})
    }

    fun subscribe(owner: LifecycleOwner, observer: Observer<List<WtfItem>>) {
        items.observe(owner, observer)
        refresh()
    }

    fun unsubscribe(owner: LifecycleOwner) {
        items.removeObservers(owner)
        items.value = emptyList()
        subscribeToWtfUsecase.unsubscribe()
    }
}