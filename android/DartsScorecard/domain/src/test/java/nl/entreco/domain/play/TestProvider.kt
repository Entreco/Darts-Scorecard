package nl.entreco.domain.play

import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.TurnHandler
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.State
import nl.entreco.domain.play.model.players.Team

/**
 * Created by Entreco on 21/11/2017.
 */
class TestProvider {
    private val player1 = Player("1")
    private val player2 = Player("2")
    private val team1 = Team(player1)
    private val team2 = Team(player2)
    private val teams = arrayOf(team1, team2)

    fun turnHandler() : TurnHandler {
        return TurnHandler(teams)
    }

    fun next(): Next {
        return Next(State.START, team1, 0, player1)
    }
}