package nl.entreco.domain.setup.usecase

/**
 * Created by entreco on 04/01/2018.
 */
data class StoreSettingsRequest(val sets: Int,
                                val legs: Int,
                                val min: Int,
                                val max: Int,
                                val score: Int)