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

    val isLoading = ObservableBoolean(true)
    val title = ObservableInt(R.string.empty)
    val description = ObservableInt(R.string.empty)

    init {
        fetchHiScoresUsecase.go(onSuccess(), onFailed())
    }

    private fun onFailed(): (Throwable) -> Unit = {
        hiscores.postValue(emptyList())
        isLoading.set(false)
    }

    private fun onSuccess(): (FetchHiScoreResponse) -> Unit = { response ->
        updateDescription(0)
        hiscores.postValue(response.hiScores)
        isLoading.set(false)
    }

    fun hiScores(): LiveData<List<HiScore>> {
        return hiscores
    }

    fun hiScores(position: Int): List<HiScoreItemModel>? {
        val sorted = hiscores.value?.mapIndexed { index, hiScore ->
            HiScoreItemModel(hiScore.playerName, hiScore.hiscores[position].value, "$index")
        }?.sortedByDescending {
            it.score.toDoubleOrNull()
        }

        return sorted
    }

    fun updateDescription(position: Int) {
        val (tit, desc) = when (hiscores.value?.get(0)?.hiscores?.get(position)) {
            is HiScoreItem.OverallAverage -> Pair(R.string.app_name, R.string.app_name)
            is HiScoreItem.Num180 -> Pair(R.string.profile_num_180, R.string.profile_num_180)
            is HiScoreItem.Num140 -> Pair(R.string.profile_num_140, R.string.profile_num_140)
            is HiScoreItem.Num100 -> Pair(R.string.profile_num_100, R.string.profile_num_100)
            is HiScoreItem.Num60 -> Pair(R.string.profile_num_60, R.string.profile_num_60)
            else -> Pair(R.string.undo, R.string.undo)
        }
        title.set(tit)
        description.set(desc)
    }
}