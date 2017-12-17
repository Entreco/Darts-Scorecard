package nl.entreco.domain.play

import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score
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
    private val team1 = Team(arrayOf(player1))
    private val team2 = Team(arrayOf(player2))
    private val teams = arrayOf(team1, team2)

    fun turnHandler(): TurnHandler {
        return TurnHandler().also { it.teams = teams }
    }

    fun next(): Next {
        return Next(State.START, team1, 0, player1, Score())
    }

    fun teams(): Array<Team> {
        return teams
    }

    fun team1(): Team {
        return team1
    }

    fun team2(): Team {
        return team1
    }

    fun team3(): Team {
        return team1
    }

}