package nl.entreco.data.db.profile

import org.mockito.kotlin.*
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.game.GameDao
import nl.entreco.data.db.game.GameTable
import nl.entreco.data.db.meta.MetaDao
import nl.entreco.data.db.turn.TurnDao
import org.junit.Test

import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.math.exp

@RunWith(MockitoJUnitRunner::class)
class ArchiveServiceRepositoryTest {

    @Mock private lateinit var mockDb: DscDatabase
    @Mock private lateinit var mockMapper: ArchiveStatMapper

    @Mock private lateinit var gameDao : GameDao
    @Mock private lateinit var turnDao : TurnDao
    @Mock private lateinit var metaDao : MetaDao
    @Mock private lateinit var profileDao : ProfileDao

    private lateinit var subject : ArchiveServiceRepository

    @Test(expected = NumberFormatException::class)
    fun `it should archive when game is finished`() {
        givenSubject()
        whenArchivingStats(true)
        thenProfilesAreCreated(2)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `it should throw when game is not finished`() {
        givenSubject()
        whenArchivingStats(false)
        thenProfilesAreCreated(2)
    }

    private fun givenSubject() {
        whenever(mockDb.gameDao()).thenReturn(gameDao)
        whenever(mockDb.turnDao()).thenReturn(turnDao)
        whenever(mockDb.metaDao()).thenReturn(metaDao)
        whenever(mockDb.profileDao()).thenReturn(profileDao)
        subject = ArchiveServiceRepository(mockDb, mockMapper)
    }

    private fun whenArchivingStats(gameFinished: Boolean) {
        val table = GameTable()
        table.id = 1L
        table.numSets = 1
        table.numLegs = 1
        table.startIndex = 0
        table.startScore = 501
        table.finished = gameFinished
        whenever(gameDao.fetchBy(12)).thenReturn(table)
        subject.archive(12)
    }

    private fun thenProfilesAreCreated(expected: Int) {
        verify(profileDao, times(expected)).create(any())
    }
}