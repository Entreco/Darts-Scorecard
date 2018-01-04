package nl.entreco.domain.setup.usecase

/**
 * Created by entreco on 04/01/2018.
 */
data class FetchSettingsResponse(val startSets: Int = def_sets,
                                 val startLegs: Int = def_legs,
                                 val min: Int = def_min,
                                 val max: Int = def_max,
                                 val startScore: Int = def_start) {
    companion object {
        const val def_sets = 1
        const val def_legs = 3
        const val def_min = 0
        const val def_max = 20
        const val def_start = 4 // should be index of '501'
    }
}



