package nl.entreco.dartsscorecard.setup

import android.content.Context
import android.databinding.ObservableInt
import android.widget.AdapterView
import android.widget.SeekBar
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.domain.Logger
import nl.entreco.domain.launch.TeamNamesString
import nl.entreco.domain.launch.usecase.CreateGameUsecase
import nl.entreco.domain.launch.usecase.ExtractTeamsUsecase
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.repository.RetrieveGameRequest
import nl.entreco.domain.repository.TeamIdsString
import javax.inject.Inject

/**
 * Created by Entreco on 20/12/2017.
 */
class Setup01ViewModel @Inject constructor(private val createGameUsecase: CreateGameUsecase, private val extractTeamsUsecase: ExtractTeamsUsecase, private val logger: Logger) : BaseViewModel() {

    private val min = 0
    val max = 20
    val startScore = ObservableInt(501)
    val numSets = ObservableInt(0)
    val numLegs = ObservableInt(0)

    fun onStartPressed(context: Context) {
        val setup = CreateGameRequest(startScore.get(), 0, numLegs.get(), numSets.get())
        val teams = randomTeam()

        ensureTeamPlayersExist(teams, {
            createNewGame(setup, it, onGameCreated(context), onGameCreatedFailed())
        }, onGameCreatedFailed())
    }

    fun onStartScoreSelected(adapter: AdapterView<*>, index: Int) {
        val selectedString = adapter.adapter.getItem(index) as? String
        startScore.set(selectedString?.toInt()!!)
    }

    fun onSetsChanged(seekBar: SeekBar, delta: Int) {
        seekBar.progress += delta
    }

    fun onLegsChanged(seekBar: SeekBar, delta: Int) {
        seekBar.progress += delta
    }

    fun onSetsProgressChanged(sets: Int) {
        if (sets in min..max) {
            numSets.set(sets + 1)
        }
    }

    fun onLegsProgressChanged(legs: Int) {
        if (legs in min..max) {
            numLegs.set(legs + 1)
        }
    }

    private fun onGameCreated(context: Context): (RetrieveGameRequest) -> Unit =
            { req -> Play01Activity.startGame(context, req) }

    private fun onGameCreatedFailed(): (Throwable) -> Unit = { err ->
        logger.w("Unable to create game $err")
    }

    private fun ensureTeamPlayersExist(teamNamesInput: TeamNamesString, done: (TeamIdsString) -> Unit, fail: (Throwable) -> Unit) {
        extractTeamsUsecase.exec(teamNamesInput, done, fail)
    }

    private fun createNewGame(createGameRequest: CreateGameRequest, teamNames: TeamIdsString, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit) {
        createGameUsecase.exec(createGameRequest, teamNames, done, fail)
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
}