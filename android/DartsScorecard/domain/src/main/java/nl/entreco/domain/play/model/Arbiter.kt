package nl.entreco.domain.play.model

import nl.entreco.domain.play.model.players.State
import nl.entreco.domain.play.model.players.Team

class Arbiter(initial: Score, private val turnHandler: TurnHandler) {

    companion object {
        const val OK : Int = 1
        const val BUST : Int = -1
        const val ERR : Int = -2
    }

    private var scores = initForStart(initial)

    private var legs = mutableListOf<Array<Score>>()

    private var sets = mutableListOf<MutableList<Array<Score>>>()

    fun start(): Next {
        return turnHandler.start()
    }

    fun handle(turn: Turn, next: Next): Next {
        val teamIndex = teamIndexOfNext(turnHandler.teams, next)
        when (applyScore(teamIndex, turn)) {
            ERR -> return turnHandler.next().copy(state = State.ERR_REQUIRES_DOUBLE)
            BUST -> return turnHandler.next().copy(state = State.ERR_BUST)
        }

        if (gameShotAndTheMatch(teamIndex)) return Next(State.MATCH, next.team, teamIndex, next.player)
        if (requiresNewSet(teamIndex)) return playerForNewSet()
        else if (requiresNewLeg(teamIndex)) return playerForNewLeg()

        return turnHandler.next()
    }

    private fun teamIndexOfNext(teams: Array<out Team>, next: Next): Int {
        return teams.indexOf(next.team)
    }

    private fun playerForNewLeg() = turnHandler.nextLeg()
    private fun playerForNewSet() = turnHandler.nextSet()

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

    private fun initForStart(score: Score) = Array(turnHandler.teams.size, { score.copy() })

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

    override fun toString(): String {
        val builder = StringBuilder().append("${scores[0]}")
        scores.drop(1).forEach { builder.append("\n$it") }
        return builder.toString()
    }
}