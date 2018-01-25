package nl.entreco.domain.repository

import nl.entreco.domain.setup.settings.FetchSettingsResponse
import nl.entreco.domain.setup.settings.StoreSettingsRequest

/**
 * Created by entreco on 04/01/2018.
 */
interface PreferenceRepository {

    fun fetchPreferredSetup(): FetchSettingsResponse
    fun storePreferredSetup(request: StoreSettingsRequest)
}