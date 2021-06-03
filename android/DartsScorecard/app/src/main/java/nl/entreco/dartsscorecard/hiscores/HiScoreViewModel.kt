package nl.entreco.dartsscorecard.hiscores

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.hiscores.FetchHiScoreResponse
import nl.entreco.domain.hiscores.FetchHiScoresUsecase
import nl.entreco.domain.hiscores.HiScore
import nl.entreco.domain.hiscores.HiScoreItem
import nl.entreco.domain.hiscores.SortHiScoresRequest
import nl.entreco.domain.hiscores.SortHiScoresUsecase
import nl.entreco.libcore.LiveEvent
import nl.entreco.libcore.toSingleEvent
import javax.inject.Inject

class HiScoreViewModel @Inject constructor(
        fetchHiScoresUsecase: FetchHiScoresUsecase,
        private val sortHiScoresUsecase: SortHiScoresUsecase
) : BaseViewModel() {

    private val hiscores = MutableLiveData<List<HiScore>>()
    private val events = LiveEvent<HiScoreItemModel>()
    fun events(): LiveData<HiScoreItemModel> = events.toSingleEvent()

    val description = ObservableInt(R.string.empty)
    val isNotEmpty = ObservableBoolean(false)

    init {
        fetchHiScoresUsecase.go(onSuccess(), onFailed())
    }

    private fun onFailed(): (Throwable) -> Unit = {
        hiscores.postValue(emptyList())
    }

    private fun onSuccess(): (FetchHiScoreResponse) -> Unit = { response ->
        hiscores.postValue(response.hiScores)
        isNotEmpty.set(response.hiScores.isNotEmpty())
        updateDescription(0)
    }

    fun hiScores(): LiveData<List<HiScore>> {
        return hiscores
    }

    fun prev(pager: ViewPager) {
        pager.currentItem--
    }

    fun next(pager: ViewPager) {
        pager.currentItem++
    }

    fun updateDescription(position: Int) {
        val current = hiscores.value ?: return
        if (current.isEmpty()) return
        if (current[0].hiScore.isEmpty()) return

        val desc = when (hiscores.value?.get(0)?.hiScore?.get(position)) {
            is HiScoreItem.ScoringAvg        -> R.string.hiscore_description_first9_average
            is HiScoreItem.CheckoutPerc      -> R.string.hiscore_description_checkout_percentage
            is HiScoreItem.WinRatio          -> R.string.hiscore_description_games_win_ratio
            is HiScoreItem.Num180            -> R.string.hiscore_description_num_180
            is HiScoreItem.Num140            -> R.string.hiscore_description_num_140
            is HiScoreItem.Num100            -> R.string.hiscore_description_num_100
            is HiScoreItem.Num60             -> R.string.hiscore_description_num_60
            is HiScoreItem.Num20             -> R.string.hiscore_description_num_20
            is HiScoreItem.NumBust           -> R.string.hiscore_description_num_0
            is HiScoreItem.BestMatchAvg      -> R.string.hiscore_description_best_avg
            is HiScoreItem.BestMatchCheckout -> R.string.hiscore_description_best_co
            else                             -> R.string.hiscore_description_overall_average
        }

        description.set(desc)
    }

    private val liveDatas = mutableMapOf<Int, MutableLiveData<List<HiScoreItemModel>>>()

    fun dataAtPosition(position: Int): LiveData<List<HiScoreItemModel>> {
        val liveData = liveDatas.getOrDefault(position, MutableLiveData())
        val items = hiscores.value?.map { it to it.hiScore[position] }?.toMap() ?: emptyMap()

        // Sort in the background
        sortHiScoresUsecase.go(SortHiScoresRequest(items), { scores ->
            val mapped = scores.map { HiScoreItemModel(it.id, it.name, it.display, it.pos, ::onProfileSelected) }
            liveData.postValue(mapped)
        }, {})
        return liveData
    }

    private fun onProfileSelected(item: HiScoreItemModel) {
        events.postValue(item)
    }
}