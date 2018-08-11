package nl.entreco.dartsscorecard.tv.launch

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import nl.entreco.dartsscorecard.base.BaseViewModel
import javax.inject.Inject

class LaunchTvViewModel @Inject constructor() : BaseViewModel() {

    val isLoading = ObservableBoolean(false)
    val registrationCode = ObservableField<String>("")

    init {
        isLoading.set(true)
    }

    private fun registrationOk(): (String) -> Unit = {
        registrationCode.set(it)
        isLoading.set(false)
    }

    private fun registrationFailed(): (Throwable) -> Unit = {
        isLoading.set(false)
    }
}