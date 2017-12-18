package nl.entreco.domain.model.players

/**
 * Created by Entreco on 18/11/2017.
 */
class Team(val players: Array<Player> = emptyArray()) {

    private var START_TURN = -1
    private var turns = START_TURN
    private var offset = 0
    private var lastLeg = 0
    private var lastSet = 0

    fun next(currentLeg: Int = 0, currentSet: Int = 0): Player {
        when {
            isNewSet(currentSet) -> newSet()
            isNewLeg(currentLeg) -> newLeg()
            else -> newTurn()
        }
        return players[(turns + offset) % players.size]
    }

    private fun isNewLeg(currentLeg: Int) = lastLeg != currentLeg
    private fun isNewSet(currentSet: Int) = lastSet != currentSet

    private fun newTurn() {
        turns++
    }

    private fun newSet() {
        turns = START_TURN
        offset++
    }

    private fun newLeg() {
        turns = START_TURN
        offset++
    }

    override fun toString(): String {
        return StringBuilder(firstPlayer().name).apply {
            players.drop(1).forEach { append(" & ").append(it.name) }
        }.toString()
    }

    private fun firstPlayer(): Player {
        return if (players.isEmpty()) NoPlayer()
        else players[0]
    }

    fun contains(player: Player): Boolean {
        return players.contains(player)
    }
}