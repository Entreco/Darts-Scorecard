package nl.entreco.domain.setup.settings

import nl.entreco.domain.repository.SetupPrefRepository
import javax.inject.Inject

/**
 * Created by entreco on 04/01/2018.
 */
class StorePreferredSettingsUsecase @Inject constructor(private val prefRepo: SetupPrefRepository) {
    fun exec(request: StoreSettingsRequest) {
        prefRepo.storePreferredSetup(request)
    }
}