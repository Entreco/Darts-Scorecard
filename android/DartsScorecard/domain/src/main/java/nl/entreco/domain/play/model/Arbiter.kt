package nl.entreco.domain.play.model

import nl.entreco.domain.play.model.players.Team

class Arbiter(initial: Score, private val turnHandler: TurnHandler) {

    private var scores = initForStart(initial)

    private var legs = mutableListOf<Array<Score>>()

    private var sets = mutableListOf<MutableList<Array<Score>>>()

    init {
        turnHandler.start()
    }

    fun handle(turn: Turn, next: Next?): Next? {
        val teamIndex = if(next == null) 0 else teamIndexOfNext(turnHandler.teams, next)
        applyScore(teamIndex, turn)

        if (gameShotAndTheMatch(teamIndex)) return null
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

    private fun applyScore(currentPlayer: Int, turn: Turn) {
        scores[currentPlayer] -= turn
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