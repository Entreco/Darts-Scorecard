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
import kotlin.random.Random

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

    private val style = MutableLiveData<Int>()
    fun style(): LiveData<Int> = style.toSingleEvent()
    fun swapStyle() {
        style.postValue(Random.nextInt(180))
    }

    private val ask = MutableLiveData<Boolean>()
    fun ask(): LiveData<Boolean> = ask.toSingleEvent()
    fun askForConsent() {
        // TODO entreco - 2019-06-08: Swap Style is hanging -> some loop maybe?
//        ask.postValue(true)
    }

    private val donation = MutableLiveData<Int>()
    fun donate(): LiveData<Int> = donation.toSingleEvent()
    fun removeAds(){
        donation.postValue(Random.nextInt(180))
    }
}