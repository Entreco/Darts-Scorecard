package nl.entreco.domain.setup.settings

import nl.entreco.domain.repository.PreferenceRepository
import javax.inject.Inject

/**
 * Created by entreco on 04/01/2018.
 */
class StorePreferredSettingsUsecase @Inject constructor(private val prefRepo: PreferenceRepository) {
    fun exec(request: StoreSettingsRequest) {
        prefRepo.storePreferredSetup(request)
    }
}