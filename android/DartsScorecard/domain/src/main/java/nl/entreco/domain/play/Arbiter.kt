package nl.entreco.domain.play

import nl.entreco.domain.model.Next
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.State
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Team

const val OK: Int = 1
const val BUST: Int = -1
const val ERR: Int = -2

class Arbiter(initial: Score) {

    internal lateinit var turnHandler: TurnHandler
    private var scores: Array<Score> = emptyArray()
    private val scoreSettings = initial.settings
    private val legs = mutableListOf<Array<Score>>()
    private val sets = mutableListOf<MutableList<Array<Score>>>()
    private var previousScore = Score()
    private var turnCounter = 0

    fun start(startIndex: Int, teams: Array<Team>): Next {
        this.turnHandler = TurnHandler(startIndex, teams)
        this.scores = Array(teams.size, { scoreSettings.score().copy() })
        this.turnCounter = 0
        return turnHandler.start(scores[0])
    }

    fun handle(turn: Turn, next: Next): Next {
        val teamIndex = turnHandler.indexOf(next.team)
        previousScore = scores[teamIndex]

        when (applyScore(teamIndex, turn)) {
            ERR -> return turnHandler.next(scores).copy(state = State.ERR_REQUIRES_DOUBLE)
            BUST -> return turnHandler.next(scores).copy(state = State.ERR_BUST)
        }

        turnCounter++

        if (gameShotAndTheMatch(teamIndex)) return Next(State.MATCH, next.team, teamIndex, next.player, scores[teamIndex])
        if (requiresNewSet(teamIndex)) return playerForNewSet()
        else if (requiresNewLeg(teamIndex)) return playerForNewLeg()

        return turnHandler.next(scores)
    }

    private fun playerForNewLeg() = turnHandler.nextLeg(scores)
    private fun playerForNewSet() = turnHandler.nextSet(scores)

    private fun gameShotAndTheMatch(currentPlayer: Int): Boolean {
        if (matchFinished(currentPlayer)) {
            legs.add(scores)
            sets.add(legs)
            initForNewMatch()
            return true
        }
        return false
    }

    private fun requiresNewLeg(currentPlayer: Int): Boolean {
        if (legFinished(currentPlayer)) {
            legs.add(scores)
            initForNewLeg()
            return true
        }
        return false
    }

    private fun requiresNewSet(currentPlayer: Int): Boolean {
        if (setFinished(currentPlayer)) {
            legs.add(scores)
            sets.add(legs)
            initForNewSet()
            return true
        }
        return false
    }

    private fun initForNewLeg() {
        scores.forEachIndexed { index, score -> scores[index] = score.rollLeg() }
    }

    private fun initForNewSet() {
        scores.forEachIndexed { index, score -> scores[index] = score.rollSet() }
    }

    private fun initForNewMatch() {
        initForNewSet()
    }

    private fun applyScore(currentPlayer: Int, turn: Turn): Int {
        val current = scores[currentPlayer].score
        val remainder = current - turn.total()
        return when {
            remainder > 1 -> {
                scores[currentPlayer] -= turn
                OK
            }
            remainder == 0 && turn.lastIsDouble() -> {
                scores[currentPlayer] -= turn
                OK
            }
            remainder == 0 && !turn.lastIsDouble() -> {
                ERR
            }
            else -> BUST
        }
    }

    private fun legFinished(currentPlayer: Int) = scores[currentPlayer].legFinished()
    private fun setFinished(currentPlayer: Int) = scores[currentPlayer].setFinished()
    private fun matchFinished(currentPlayer: Int) = scores[currentPlayer].matchFinished()

    fun getScores(): Array<Score> {
        return scores
    }

    fun getLegs(): MutableList<Array<Score>> {
        return legs
    }

    fun getSets(): MutableList<MutableList<Array<Score>>> {
        return sets
    }

    fun getPreviousScore(): Score {
        return previousScore
    }

    fun getTurnCount(): Int{
        return turnCounter
    }
}