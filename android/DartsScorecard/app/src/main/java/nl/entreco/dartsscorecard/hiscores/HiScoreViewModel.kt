package nl.entreco.dartsscorecard.hiscores

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.hiscores.FetchHiScoreResponse
import nl.entreco.domain.hiscores.FetchHiScoresUsecase
import nl.entreco.domain.hiscores.HiScore
import nl.entreco.domain.hiscores.HiScoreItem
import javax.inject.Inject

class HiScoreViewModel @Inject constructor(
        fetchHiScoresUsecase: FetchHiScoresUsecase
) : BaseViewModel() {

    private val hiscores = MutableLiveData<List<HiScore>>()
    val description = ObservableInt(R.string.empty)

    init {
        fetchHiScoresUsecase.go(onSuccess(), onFailed())
    }

    private fun onFailed(): (Throwable) -> Unit = {
        hiscores.postValue(emptyList())
    }

    private fun onSuccess(): (FetchHiScoreResponse) -> Unit = { response ->
        hiscores.postValue(response.hiScores)
        updateDescription(0)
    }

    fun hiScores(): LiveData<List<HiScore>> {
        return hiscores
    }

    fun updateDescription(position: Int) {
        val desc = when (hiscores.value?.get(0)?.hiScore?.get(position)) {
            is HiScoreItem.Num180 -> R.string.hiscore_description_num_180
            is HiScoreItem.Num140 -> R.string.hiscore_description_num_140
            is HiScoreItem.Num100 -> R.string.hiscore_description_num_100
            is HiScoreItem.Num60 -> R.string.hiscore_description_num_60
            is HiScoreItem.Num20 -> R.string.hiscore_description_num_20
            is HiScoreItem.NumBust -> R.string.hiscore_description_num_0
            is HiScoreItem.FirstNineAvg -> R.string.hiscore_description_first9_average
            else -> R.string.hiscore_description_overall_average
        }
        description.set(desc)
    }

    fun dataAtPosition(position: Int): Map<String, HiScoreItem> {
        val items = hiscores.value?.map { it.playerName to it.hiScore[position] }?.toMap() ?: throw IllegalStateException("No data at position $position")
        return items
    }
}