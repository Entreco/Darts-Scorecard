package nl.entreco.data.db.stats

import nl.entreco.data.db.meta.MetaMapper
import nl.entreco.data.db.meta.MetaTable
import nl.entreco.data.db.turn.TurnMapper
import nl.entreco.data.db.turn.TurnTable
import nl.entreco.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 20/01/2018.
 */
class StatMapperTest {

    private lateinit var subject: StatMapper
    private lateinit var givenScores: Map<Long, Score>
    private lateinit var givenTurns: List<TurnTable>
    private lateinit var givenPlayerIds: List<Long>
    private lateinit var givenMetas: List<MetaTable>
    private val gameId: Long = 3
    private val turnMapper = TurnMapper()
    private val metaMapper = MetaMapper()
    private lateinit var expectedStats: Map<Long, Stat>

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
    }

    @Test
    fun `it should calculate correct stats for player1 second turn`() {
        givenSubject()
        givenPlayers(1, 2)
        givenTurns(`100`(), `60`(), `180`)
        whenConverting()

        thenAvgIs(1, 100.toDouble())
        then100sIs(1, 1)
        then140sIs(1, 0)
        then180sIs(1, 0)
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
    }

    private fun givenSubject() {
        subject = StatMapper()
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

        turns.forEachIndexed { index, turn ->
            val playerId = id(index)
            turnTables.add(toTable(playerId, turn))

            val score = givenScores[playerId]!!
            score -= turn

            val meta = TurnMeta(id(index), index, score, 0)
            metaTables.add(toTable(playerId, meta))
        }
        givenTurns = turnTables
        givenMetas = metaTables
    }

    private fun whenConverting() {
        expectedStats = subject.to(givenTurns, givenMetas)
    }

    private fun thenAvgIs(playerId: Long, expected: Double) {
        assertEquals(expected, expectedStats[playerId]!!.avg, 0.1)
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

    private fun `100`(): Turn = Turn(Dart.TRIPLE_20, Dart.SINGLE_20, Dart.SINGLE_20)

    private fun `60`(): Turn = Turn(Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_20)

    private fun id(index: Int): Long {
        return givenPlayerIds[index % givenPlayerIds.size]
    }

    private fun toTable(id: Long, turn: Turn): TurnTable {
        return turnMapper.from(gameId, id, turn)
    }

    private fun toTable(turnId: Long, meta: TurnMeta): MetaTable {
        return metaMapper.from(gameId, turnId, meta)
    }
}