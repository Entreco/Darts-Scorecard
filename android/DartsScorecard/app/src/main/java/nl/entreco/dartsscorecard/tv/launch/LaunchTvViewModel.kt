package nl.entreco.dartsscorecard.tv.launch

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.stream.RegisterStreamReceiverUsecase
import javax.inject.Inject

class LaunchTvViewModel @Inject constructor(
        registerStreamReceiverUsecase: RegisterStreamReceiverUsecase) : BaseViewModel() {

    val isLoading = ObservableBoolean(false)
    val registrationCode = ObservableField<String>("")

    init {
        isLoading.set(true)
        registerStreamReceiverUsecase.exec(
                registrationOk(),
                registrationFailed())
    }

    private fun registrationOk(): (String) -> Unit = {
        registrationCode.set(it)
        isLoading.set(false)
    }

    private fun registrationFailed(): (Throwable) -> Unit = {
        isLoading.set(false)
    }
}