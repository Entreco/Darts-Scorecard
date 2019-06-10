package nl.entreco.dartsscorecard.settings

import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.play.mastercaller.ToggleMusicUsecase
import nl.entreco.domain.play.mastercaller.ToggleSoundUsecase
import nl.entreco.domain.repository.AudioPrefRepository
import nl.entreco.shared.toSingleEvent
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
        audioPrefRepository: AudioPrefRepository,
        private val toggleMusicUsecase: ToggleMusicUsecase,
        private val toggleMasterCaller: ToggleSoundUsecase
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

    private val style = MutableLiveData<Boolean>()
    fun style(): LiveData<Boolean> = style.toSingleEvent()
    fun swapStyle() {
        // TODO entreco - 2019-06-08: Swap Style is hanging -> some loop maybe?
        style.postValue(true)
    }

    private val ask = MutableLiveData<Boolean>()
    fun ask(): LiveData<Boolean> = ask.toSingleEvent()
    fun askForConsent() {
        ask.postValue(true)
    }

    private val donation = MutableLiveData<Boolean>()
    fun donate(): LiveData<Boolean> = donation.toSingleEvent()
    fun removeAds(){
        donation.postValue(true)
    }
}