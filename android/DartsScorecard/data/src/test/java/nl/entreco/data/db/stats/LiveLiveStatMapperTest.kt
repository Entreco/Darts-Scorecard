package nl.entreco.data.db.stats

import nl.entreco.data.db.meta.MetaMapper
import nl.entreco.data.db.meta.MetaTable
import nl.entreco.data.db.turn.TurnMapper
import nl.entreco.data.db.turn.TurnTable
import nl.entreco.domain.model.*
import nl.entreco.domain.play.ScoreEstimator
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 20/01/2018.
 */
class LiveLiveStatMapperTest {

    private lateinit var subject: LiveStatMapper
    private lateinit var givenScores: Map<Long, Score>
    private lateinit var givenTurns: List<TurnTable>
    private lateinit var givenPlayerIds: List<Long>
    private lateinit var givenMetas: List<MetaTable>
    private val gameId: Long = 3
    private val turnMapper = TurnMapper()
    private val metaMapper = MetaMapper()
    private lateinit var expectedStats: Map<Long, LiveStat>
    private val scoreEstimator = ScoreEstimator()

    @Test
    fun `it should calculate correct stats for player1 first turn`() {
        givenSubject()
        givenPlayers(1, 2)
        givenTurns(`100`(), `60`())
        whenConverting()

        thenAvgIs(1, 100.toDouble())
        then100sIs(1, 1)
        then140sIs(1, 0)
        then180sIs(1, 0)
        thenHighestIs(1, 100)
    }

    @Test
    fun `it should calculate correct stats for player1 second turn`() {
        givenSubject()
        givenPlayers(1, 2)
        givenTurns(`100`(), `60`(), `180`())
        whenConverting()

        thenAvgIs(1, 140.toDouble())
        then100sIs(1, 1)
        then140sIs(1, 0)
        then180sIs(1, 1)
        thenHighestIs(1, 180)
    }

    @Test
    fun `it should calculate correct stats for player2 first turn`() {
        givenSubject()
        givenPlayers(1, 2)
        givenTurns(`100`(), `60`())
        whenConverting()

        thenAvgIs(2, 60.toDouble())
        then100sIs(2, 0)
        then140sIs(2, 0)
        then180sIs(2, 0)
        thenHighestIs(2, 60)
    }

    @Test
    fun `it should calculate correct finishes for player1`() {
        givenSubject()
        givenPlayers(1)
        givenTurns(`180`(), `180`(), `100`(), `41`())
        whenConverting()

        thenCheckOutIs(1, 1.0)
        thenHighestCheckOutIs(1, 41)
    }

    @Test
    fun `it should calculate correct Doubles for player1`() {
        givenSubject()
        givenPlayers(1)
        givenTurns(`180`(), `180`(), `100`(), `41`())
        whenConverting()

        thenAtDoubleIs(1, 1)
    }

    @Test
    fun `it should calculate correct Doubles for player1 - multiple`() {
        givenSubject()
        givenPlayers(1)
        givenTurns(`180`(), `180`(), `60`(), `60`(), Turn(Dart.SINGLE_1, Dart.ZERO, Dart.ZERO))
        whenConverting()

        thenAtDoubleIs(1, 2)
    }

    @Test
    fun `it should calculate correct breaksMade for player1`() {
        givenSubject()
        givenPlayers(1)
        givenTurns(`180`(), `180`(), `100`(), `41`())
        whenConverting()

        thenBreaksMadeIs(1, 0)
    }

    @Test
    fun `it should calculate correct breaksMade for player2`() {
        givenSubject()
        givenPlayers(1, 2)
        givenTurns(`60`(), `180`(), `60`(), `180`(), `60`(), `141`())
        whenConverting()

        thenBreaksMadeIs(1, 0)
        thenBreaksMadeIs(2, 1)
    }

    private fun givenSubject() {
        subject = LiveStatMapper()
    }

    private fun givenPlayers(vararg ids: Long) {
        givenPlayerIds = ids.toList()
        val scores = emptyMap<Long, Score>().toMutableMap()
        ids.forEach {
            scores[it] = Score()
        }
        givenScores = scores
    }

    private fun givenTurns(vararg turns: Turn) {
        val metaTables = mutableListOf<MetaTable>()
        val turnTables = mutableListOf<TurnTable>()
        val starterId = id(0)

        turns.forEachIndexed { index, turn ->
            val playerId = id(index)
            turnTables.add(toTable(playerId, turn))

            val score = givenScores[playerId]!!
            val previousScore = score.copy()
            val atDouble = scoreEstimator.atDouble(turn, score.score)
            score -= turn

            val meta = TurnMeta(id(index), index, 0, 0, previousScore, score.score == 0 && playerId != starterId)
            metaTables.add(toTable(playerId, atDouble, meta))
        }
        givenTurns = turnTables
        givenMetas = metaTables
    }

    private fun whenConverting() {
        expectedStats = subject.to(givenTurns, givenMetas)
    }

    private fun thenAvgIs(playerId: Long, expected: Double) {
        val score = expectedStats[playerId]!!.totalScore
        val darts = expectedStats[playerId]!!.nDarts
        assertEquals(expected, (score / darts.toDouble()) * 3.0, 0.1)
    }

    private fun then100sIs(playerId: Long, expected: Int) {
        assertEquals(expected, expectedStats[playerId]!!.n100)
    }

    private fun then140sIs(playerId: Long, expected: Int) {
        assertEquals(expected, expectedStats[playerId]!!.n140)
    }

    private fun then180sIs(playerId: Long, expected: Int) {
        assertEquals(expected, expectedStats[playerId]!!.n180)
    }

    private fun thenHighestIs(playerId: Long, expected: Int) {
        assertEquals(expected, expectedStats[playerId]!!.highest[0])
    }

    private fun thenCheckOutIs(playerId: Long, expected: Double) {
        val co = expectedStats[playerId]!!.nCheckouts
        val count = expectedStats[playerId]!!.nAtCheckout
        assertEquals(expected, co / count.toDouble(), 0.1)
    }

    private fun thenHighestCheckOutIs(playerId: Long, expected: Int) {
        assertEquals(expected, expectedStats[playerId]!!.highestCo[0])
    }

    private fun thenAtDoubleIs(playerId: Long, expected: Int) {
        assertEquals(expected, expectedStats[playerId]!!.nAtCheckout)
    }

    private fun thenBreaksMadeIs(playerId: Long, expected: Int) {
        assertEquals(expected, expectedStats[playerId]!!.nBreaks)
    }

    private fun `180`(): Turn = Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.TRIPLE_20)
    private fun `141`(): Turn = Turn(Dart.TRIPLE_20, Dart.TRIPLE_19, Dart.DOUBLE_12)
    private fun `100`(): Turn = Turn(Dart.TRIPLE_20, Dart.SINGLE_20, Dart.SINGLE_20)
    private fun `60`(): Turn = Turn(Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_20)
    private fun `41`(): Turn = Turn(Dart.SINGLE_1, Dart.DOUBLE_20)

    private fun id(index: Int): Long {
        return givenPlayerIds[index % givenPlayerIds.size]
    }

    private fun toTable(id: Long, turn: Turn): TurnTable {
        return turnMapper.from(gameId, id, turn)
    }

    private fun toTable(turnId: Long, atDouble: Int, meta: TurnMeta): MetaTable {
        return metaMapper.from(gameId, turnId, meta, atDouble)
    }
}