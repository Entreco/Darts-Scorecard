package nl.entreco.dartsscorecard.tv.launch

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.view.View
import nl.entreco.dartsscorecard.tv.base.TvViewModel
import nl.entreco.domain.stream.RegisterStreamReceiverUsecase
import nl.entreco.shared.log.Logger
import javax.inject.Inject

class LaunchTvViewModel @Inject constructor(
        private val logger: Logger,
        registerStreamReceiverUsecase: RegisterStreamReceiverUsecase) : TvViewModel() {

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