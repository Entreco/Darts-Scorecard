package nl.entreco.domain.repository

import nl.entreco.domain.setup.usecase.FetchSettingsResponse
import nl.entreco.domain.setup.usecase.StoreSettingsRequest

/**
 * Created by entreco on 04/01/2018.
 */
interface PreferenceRepository {

    fun fetchPreferredSetup(): FetchSettingsResponse
    fun storePreferredSetup(request: StoreSettingsRequest)
}