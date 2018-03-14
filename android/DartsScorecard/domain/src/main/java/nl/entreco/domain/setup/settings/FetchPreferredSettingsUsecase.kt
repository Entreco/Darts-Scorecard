package nl.entreco.domain.setup.settings

import nl.entreco.domain.repository.SetupPrefRepository
import javax.inject.Inject

/**
 * Created by entreco on 04/01/2018.
 */
class FetchPreferredSettingsUsecase @Inject constructor(private val prefRepo: SetupPrefRepository) {
    fun exec(done: (FetchSettingsResponse) -> Unit) {
        try {
            done(prefRepo.fetchPreferredSetup())
        } catch (oops: Exception) {
            done(FetchSettingsResponse())
        }
    }
}