package nl.entreco.dartsscorecard.settings

import android.widget.SeekBar
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.play.mastercaller.ToggleMusicUsecase
import nl.entreco.domain.play.mastercaller.ToggleSoundUsecase
import nl.entreco.domain.repository.AudioPrefRepository
import nl.entreco.domain.repository.BotPrefRepository
import nl.entreco.shared.toSingleEvent
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
        audioPrefRepository: AudioPrefRepository,
        private val toggleMusicUsecase: ToggleMusicUsecase,
        private val toggleMasterCaller: ToggleSoundUsecase,
        private val botPrefRepository: BotPrefRepository
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

    val maxSpeed = 5000
    val speed = ObservableInt(botPrefRepository.getBotSpeed()).apply {
        addOnPropertyChangedCallback(object: Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                updateSpeed(sender)
            }
        })
    }

    val speedValue = ObservableField("%.1f".format(speed.get() / 1000.0))

    private fun updateSpeed(sender: Observable?) {
        botPrefRepository.setBotSpeed((sender as ObservableInt).get())
        speedValue.set("%.1f".format(speed.get() / 1000.0))
    }

    fun onSpeedChanged(seekBar: SeekBar, delta: Int) {
        seekBar.progress += delta
    }

    fun onSpeedProgressChanged(sets: Int) {
        if (sets in 0..maxSpeed) {
            speed.set(sets)
        }
    }

    private val style = MutableLiveData<Boolean>()
    fun style(): LiveData<Boolean> = style.toSingleEvent()
    fun swapStyle() {
        style.postValue(true)
    }
    fun stopStyler() { if(style.value == true) style.postValue(false) }

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