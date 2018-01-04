package nl.entreco.domain.setup.usecase

import nl.entreco.domain.repository.PreferenceRepository
import javax.inject.Inject

/**
 * Created by entreco on 04/01/2018.
 */
class FetchPreferredSettingsUsecase @Inject constructor(private val prefRepo: PreferenceRepository) {
    fun exec(done: (FetchSettingsResponse) -> Unit) {
        try {
            done(prefRepo.fetchPreferredSetup())
        } catch (oops: Exception) {
            done(FetchSettingsResponse())
        }
    }
}