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
    private val titles = MutableLiveData<Int>()

    val isLoading = ObservableBoolean(true)
    val description = ObservableInt(R.string.empty)

    init {
        fetchHiScoresUsecase.go(onSuccess(), onFailed())
    }

    private fun onFailed(): (Throwable) -> Unit = {
        hiscores.postValue(emptyList())
        isLoading.set(false)
    }

    private fun onSuccess(): (FetchHiScoreResponse) -> Unit = { response ->
        hiscores.postValue(response.hiScores)
        isLoading.set(false)
        updateDescription(0)
    }

    fun hiScores(): LiveData<List<HiScore>> {
        return hiscores
    }

    fun title() : LiveData<Int>{
        return titles
    }

    fun hiScores(position: Int): List<HiScoreItemModel>? {
        val sorted = hiscores.value?.map { hiScore ->
            HiScoreItemModel(hiScore.playerName, hiScore.hiScores[position], 0)
        }?.sortedByDescending {
            it.hiScore.sort()
        }

        return sorted?.mapIndexed { index, item ->
            item.copy(pos = index + 1)
        }
    }

    fun updateDescription(position: Int) {
        val (tit, desc) = when (hiscores.value?.get(0)?.hiScores?.get(position)) {
            is HiScoreItem.Num180 -> Pair(R.string.profile_num_180, R.string.profile_num_180)
            is HiScoreItem.Num140 -> Pair(R.string.profile_num_140, R.string.profile_num_140)
            is HiScoreItem.Num100 -> Pair(R.string.profile_num_100, R.string.profile_num_100)
            is HiScoreItem.Num60 -> Pair(R.string.profile_num_60, R.string.profile_num_60)
            else -> Pair(R.string.profile_overall_average, R.string.profile_overall_average)
        }
        titles.postValue(tit)
        description.set(desc)
    }
}