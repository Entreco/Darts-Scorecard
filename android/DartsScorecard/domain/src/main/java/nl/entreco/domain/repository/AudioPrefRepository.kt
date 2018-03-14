package nl.entreco.domain.repository

/**
 * Created by entreco on 14/03/2018.
 */
interface AudioPrefRepository {
    fun isMasterCallerEnabled() : Boolean
    fun setMasterCallerEnabled(enabled: Boolean)
}