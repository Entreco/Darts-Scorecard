package nl.entreco.domain.model

/**
 * Created by Entreco on 21/11/2017.
 */
enum class State {
    START,
    NORMAL,
    ERR_BUST,
    ERR_REQUIRES_DOUBLE,
    LEG,
    SET,
    TIEBREAK,
    MATCH
}