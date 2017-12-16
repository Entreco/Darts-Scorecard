package nl.entreco.data

import nl.entreco.data.db.game.GameTable
import nl.entreco.data.db.player.PlayerTable
import nl.entreco.domain.play.model.players.TeamsString
import java.util.*

/**
 * Created by Entreco on 16/12/2017.
 */
class TestProvider {
    companion object {
        fun createPlayer(name: String, double: Int): PlayerTable {
            val table = PlayerTable()
            table.name = name
            table.fav = double.toString()
            table.uid = UUID.randomUUID().toString()
            return table
        }

        fun createGame(teamstring: String, startScore: Int, startIndex: Int, legs: Int, sets: Int): GameTable {
            val table = GameTable()
            table.uid = UUID.randomUUID().toString()
            table.numLegs = legs
            table.numSets = sets
            table.startIndex = startIndex
            table.startScore = startScore
            table.teams = teamstring
            return table
        }

    }
}