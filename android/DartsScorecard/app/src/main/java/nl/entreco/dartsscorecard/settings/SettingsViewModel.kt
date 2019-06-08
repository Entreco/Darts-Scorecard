package nl.entreco.dartsscorecard.settings

import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.play.mastercaller.ToggleMusicUsecase
import nl.entreco.domain.play.mastercaller.ToggleSoundUsecase
import nl.entreco.domain.repository.AudioPrefRepository
import nl.entreco.libconsent.ask.AskConsentUsecase
import javax.inject.Inject
import kotlin.random.Random

class SettingsViewModel @Inject constructor(
        audioPrefRepository: AudioPrefRepository,
        private val toggleMusicUsecase: ToggleMusicUsecase,
        private val toggleMasterCaller: ToggleSoundUsecase,
        private val askConsentUsecase: AskConsentUsecase
) : BaseViewModel() {
    val hasMasterCaller = ObservableBoolean(audioPrefRepository.isMasterCallerEnabled()).apply {
        addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                toggleMasterCaller.toggle()
            }
        })
    }
    val hasBackground = ObservableBoolean(audioPrefRepository.isBackgroundMusicEnabled()).apply {
        addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                toggleMusicUsecase.toggle()
            }
        })
    }

    private val style = MutableLiveData<Int>()
    fun style() : LiveData<Int> = style
    fun swapStyle(){
        style.postValue(Random.nextInt(100))
    }

    private val ask = MutableLiveData<Boolean>()
    fun ask() : LiveData<Boolean> = ask
    fun askForConsent(){
        ask.postValue(true)
    }
}