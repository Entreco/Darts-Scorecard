package nl.entreco.dartsscorecard.tv.launch

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.streaming.receive.RegisterReceiverRequest
import nl.entreco.domain.streaming.receive.RegisterReceiverResponse
import nl.entreco.domain.streaming.receive.RegisterReceiverUsecase
import javax.inject.Inject

class LaunchTvViewModel @Inject constructor(
        registerReceiverUsecase: RegisterReceiverUsecase
        ) : BaseViewModel() {

    val isLoading = ObservableBoolean(false)
    val registrationCode = ObservableField<String>("")

    init {
        isLoading.set(true)
        registerReceiverUsecase.go(RegisterReceiverRequest("todo -> maybe tv or some identifier"), registrationOk(), registrationFailed())
    }

    private fun registrationOk(): (RegisterReceiverResponse) -> Unit = { response ->
        registrationCode.set(response.code)
        isLoading.set(false)
    }

    private fun registrationFailed(): (Throwable) -> Unit = {
        isLoading.set(false)
    }
}