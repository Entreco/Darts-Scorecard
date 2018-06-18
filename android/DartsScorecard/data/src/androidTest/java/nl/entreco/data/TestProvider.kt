package nl.entreco.data

import nl.entreco.data.db.game.GameTable
import nl.entreco.data.db.player.PlayerTable
import nl.entreco.data.db.profile.ProfileTable
import nl.entreco.data.db.turn.TurnTable

/**
 * Created by Entreco on 16/12/2017.
 */
class TestProvider {
    companion object {
        fun createPlayer(name: String, double: Int): PlayerTable {
            val table = PlayerTable()
            table.name = name
            table.fav = double.toString()
            return table
        }

        fun createGame(teamstring: String, startScore: Int, startIndex: Int, legs: Int, sets: Int): GameTable {
            val table = GameTable()
            table.numLegs = legs
            table.numSets = sets
            table.startIndex = startIndex
            table.startScore = startScore
            table.teams = teamstring
            table.finished = false
            return table
        }

        fun createTurn(game: Long, d1: Int, d2: Int, d3: Int, m1: Int, m2: Int, m3: Int, darts: Int, player: Long): TurnTable {
            val table = TurnTable()
            table.d1 = d1
            table.d2 = d2
            table.d3 = d3
            table.m1 = m1
            table.m2 = m2
            table.m3 = m3
            table.game = game
            table.numDarts = darts
            table.player = player
            return table
        }

        fun createProfile(id: Long, player: Long, gameId: Long, total: Int, total9: Int, num180s: Int, darts: Int, darts9: Int, didWin: Boolean): ProfileTable {
            val table = ProfileTable()
            table.id = id
            table.playerId = player
            table.gameId = gameId
            table.numDarts = darts
            table.numDarts9 = darts9
            table.totalScore = total
            table.totalScore9 = total9
            table.num180s = num180s
            table.didWin = didWin
            return table
        }

    }
}