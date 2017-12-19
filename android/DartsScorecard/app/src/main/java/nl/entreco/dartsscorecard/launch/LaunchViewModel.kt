package nl.entreco.dartsscorecard.launch

import android.content.Context
import android.databinding.ObservableField
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.domain.launch.TeamNamesString
import nl.entreco.domain.launch.usecase.CreateGameUsecase
import nl.entreco.domain.launch.usecase.CreateTeamsUsecase
import nl.entreco.domain.launch.usecase.RetrieveLatestGameUsecase
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.repository.RetrieveGameRequest
import nl.entreco.domain.repository.TeamIdsString
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class LaunchViewModel @Inject constructor(private val createGameUsecase: CreateGameUsecase, private val createTeamsUsecase: CreateTeamsUsecase, private val retrieveGameUsecase: RetrieveLatestGameUsecase) : BaseViewModel() {

    val resumeGame = ObservableField<RetrieveGameRequest?>(null)

    init {
        retrieveLatestGame()
    }

    fun startNewGame(context: Context) {
        val setup = CreateGameRequest(501, 0, 3, 2)
        val teams = randomTeam()

        ensureTeamPlayersExist(teams, {
            createNewGame(setup, it, { Play01Activity.startGame(context, it) }, {})
        }, {})
    }

    fun resumeGame(context: Context) {
        val request = resumeGame.get()
        if (request != null) {
            Play01Activity.startGame(context, request)
        }
    }

    private fun randomTeam(): TeamNamesString {
        return when ((Math.random() * 10).toInt()) {
            0 -> TeamNamesString("Remco,Charlie|Eva,Guusje")
            1 -> TeamNamesString("Remco,Eva,Guusje|Boeffie,Beer,Charlie")
            2 -> TeamNamesString("Boeffie|Beer")
            3 -> TeamNamesString("Remco|Eva|Guusje")
            4 -> TeamNamesString("Remco|Eva|Guusje|Boeffie|Beer|Charlie")
            5 -> TeamNamesString("Rob|Geert|Boy")
            6 -> TeamNamesString("Rob,Allison|Geert,Mikka|Boy,Linda")
            7 -> TeamNamesString("Guusje|Eva")
            8 -> TeamNamesString("Guusje,Eva,Beer,Boeffie,Charlie|Co")
            9 -> TeamNamesString("Entreco|Bonske")
            else -> TeamNamesString("Co")
        }
    }

    fun retrieveLatestGame() {
        retrieveGameUsecase.fetchLatest({ (gameId, teamIds, gameRequest) ->
            resumeGame.set(RetrieveGameRequest(gameId, teamIds, gameRequest))
        }, { resumeGame.set(null) })
    }

    private fun ensureTeamPlayersExist(teamNamesInput: TeamNamesString, done: (TeamIdsString) -> Unit, fail: (Throwable) -> Unit) {
        createTeamsUsecase.start(teamNamesInput, done, fail)
    }

    private fun createNewGame(createGameRequest: CreateGameRequest, teamNames: TeamIdsString, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit) {
        createGameUsecase.start(createGameRequest, teamNames, done, fail)
    }
}