package nl.entreco.data.db.turn

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.TestProvider
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by entreco on 05/01/2018.
 */
@RunWith(AndroidJUnit4::class)
class TurnDaoTest {
    private lateinit var turnDao: TurnDao
    private lateinit var db: DscDatabase
    private var givenTurn: TurnTable? = null
    private var actualRow: Long = -1
    private val givenGame: Long = 44

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getContext()
        db = Room.inMemoryDatabaseBuilder(context, DscDatabase::class.java).build()
        turnDao = db.turnDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun create() {
        givenTurn(1, 2, 3)
        whenStoringTurn()
        Assert.assertEquals(1, actualRow)
    }

    @Test
    fun fetchAll() {
        createTurn(1, 2, 3, 1, 2, 3, 3)
        createTurn(1, 2, 3, 1, 2, 3, 2)

        val turns = turnDao.fetchAll(givenGame)
        Assert.assertNotNull(turns)

        Assert.assertEquals(2, turns.size)

        verifyTurn(1 + 4 + 9, turns[0])
        verifyTurn(1 + 4, turns[1])
    }

    private fun createTurn(d1: Int, d2: Int, d3: Int, m1: Int, m2: Int, m3: Int, darts: Int) {
        val turn = TestProvider.createTurn(givenGame, d1, d2, d3, m1, m2, m3, darts)
        turnDao.create(turn)
    }

    private fun givenTurn(d1: Int, d2: Int, d3: Int) {
        givenTurn = TestProvider.createTurn(1, d1, d2, d3, 1, 2, 3, 3)
    }

    private fun whenStoringTurn() {
        actualRow = turnDao.create(givenTurn!!)
    }

    private fun verifyTurn(expected: Int, turnTable: TurnTable) {
        val actual = (0 until turnTable.numDarts).sumBy { score(turnTable, it) * multiplier(turnTable, it) }
        assertEquals(expected, actual)
    }

    private fun score(turnTable: TurnTable, dart: Int): Int {
        return when (dart) {
            0 -> turnTable.d1
            1 -> turnTable.d2
            else -> turnTable.d3
        }
    }

    private fun multiplier(turnTable: TurnTable, dart: Int): Int {
        return when (dart) {
            0 -> turnTable.m1
            1 -> turnTable.m2
            else -> turnTable.m3
        }
    }
}