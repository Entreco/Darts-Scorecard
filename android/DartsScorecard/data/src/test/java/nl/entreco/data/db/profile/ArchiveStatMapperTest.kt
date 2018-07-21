package nl.entreco.data.db.profile

import nl.entreco.data.db.meta.MetaTable
import nl.entreco.data.db.turn.TurnTable
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ArchiveStatMapperTest {
    private val gameId: Long = 11
    private val playerId: Long = 69

    private lateinit var subject: ArchiveStatMapper
    private lateinit var turnTable: TurnTable
    private lateinit var metaTable: MetaTable

    @Before
    fun setUp() {
        givenTurnTable()
        givenMetaTable()
        subject = ArchiveStatMapper()
    }

    @Test
    fun `it should map to profileTable`() {
        assertNotNull(subject.to(gameId, playerId, "1", listOf(turnTable), listOf(metaTable)))
    }

    @Test
    fun `it should handle emptyTurns()`() {
        val profile = subject.to(gameId, playerId, "1", emptyList(), listOf(metaTable))
        assertEquals(0, profile.numDarts9)
    }

    @Test
    fun `it should handle emptyMeta()`() {
        val profile = subject.to(gameId, playerId, "1", listOf(turnTable), emptyList())
        assertEquals(0, profile.numDarts)
    }

    @Test
    fun `it should map 'didWin' (false)`() {
        val profile = subject.to(gameId, playerId, "1", listOf(turnTable), listOf(metaTable))
        assertFalse(profile.didWin)
    }
    
    @Test
    fun `it should map 'didWin' (true)`() {
        val profile = subject.to(gameId, playerId, "$playerId", listOf(turnTable), listOf(metaTable))
        assertTrue(profile.didWin)
    }

    @Test
    fun `it should map 'gameId'`() {
        val profile = subject.to(gameId, playerId, "1", listOf(turnTable), listOf(metaTable))
        assertEquals(gameId, profile.gameId)
    }

    @Test
    fun `it should map 'playerId'`() {
        val profile = subject.to(gameId, playerId, "1", listOf(turnTable), listOf(metaTable))
        assertEquals(playerId, profile.playerId)
    }

    private fun givenTurnTable() {
        turnTable = TurnTable()
        turnTable.id = 0
        turnTable.d1 = 20
        turnTable.d2 = 20
        turnTable.d3 = 20
        turnTable.m1 = 1
        turnTable.m2 = 3
        turnTable.m3 = 2
        turnTable.numDarts = 3
        turnTable.game = gameId
        turnTable.player = playerId
    }

    private fun givenMetaTable() {
        metaTable = MetaTable()
        metaTable.id = 0
        metaTable.playerId = playerId
        metaTable.gameId = gameId
        metaTable.turnId = -1
        metaTable.legNumber = -1
        metaTable.setNumber = -1
        metaTable.turnInLeg = -1
        metaTable.score = -1
        metaTable.atCheckout = 0
        metaTable.breakMade = false
    }

}