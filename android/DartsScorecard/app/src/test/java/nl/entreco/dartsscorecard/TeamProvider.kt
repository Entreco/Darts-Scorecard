package nl.entreco.dartsscorecard

import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team

/**
 * Created by Entreco on 12/12/2017.
 */
class TeamProvider {
    companion object {
        fun generate(vararg names: String) : ArrayList<Team> {
            val array = mutableListOf<Team>()
            names.forEachIndexed { index, s ->
                array[index] = Team(arrayOf(Player(s)))
            }
            return ArrayList(array)
        }
    }
}