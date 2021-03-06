package nl.entreco.data.db.game

import nl.entreco.data.db.Mapper
import nl.entreco.domain.model.Game
import nl.entreco.domain.model.Score
import nl.entreco.domain.play.Arbiter
import nl.entreco.domain.settings.ScoreSettings

/**
 * Created by Entreco on 16/12/2017.
 */
class GameMapper : Mapper<GameTable, Game> {

    override fun to(from: GameTable): Game {
        val id = from.id
        val startIndex = from.startIndex
        val startScore = from.startScore
        val legs = from.numLegs
        val sets = from.numSets

        val setting = ScoreSettings(startScore, legs, sets, startIndex)
        val initial = Score(startScore, 0, 0, setting)
        val arbiter = Arbiter(initial)
        return Game(id, arbiter)
    }
}