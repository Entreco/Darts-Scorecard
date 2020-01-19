package nl.entreco.domain.model.players

const val TeamSeperator = "|"
const val PlayerSeperator = ","

/**
 * Created by Entreco on 18/11/2017.
 */
class Team(val players: Array<Player> = emptyArray()) {

    private val startTurn = -1
    private var turns = startTurn
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
        turns = startTurn
        offset++
    }

    private fun newLeg() {
        turns = startTurn
        offset++
    }

    private fun firstPlayer(): Player {
        return if (players.isEmpty()) NoPlayer()
        else players[0]
    }

    fun contains(playerId: Long): Boolean {
        return players.map { it.id }.contains(playerId)
    }

    fun contains(player: Player): Boolean {
        return players.contains(player)
    }

    fun imageUrl(): String? {
        return if(players.size <= 1) players[0].image else "team${players.size}"
    }

    fun toTeamString() : String {
        return StringBuilder(firstPlayer().id.toString()).apply {
            players.drop(1).forEach { append(PlayerSeperator).append(it.id) }
        }.toString()
    }

    override fun toString(): String {
        return StringBuilder(firstPlayer().name).apply {
            players.drop(1).forEach { append(" & ").append(it.name) }
        }.toString()
    }
}