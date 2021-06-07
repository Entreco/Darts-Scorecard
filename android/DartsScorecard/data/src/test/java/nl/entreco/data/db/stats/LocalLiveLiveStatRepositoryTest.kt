package nl.entreco.data.db.stats

import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.meta.MetaDao
import nl.entreco.data.db.turn.TurnDao
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 24/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class LocalLiveLiveStatRepositoryTest {

    @Mock private lateinit var mockDb: DscDatabase
    @Mock private lateinit var mockMapperLive: LiveStatMapper
    @Mock private lateinit var mockTurnDao: TurnDao
    @Mock private lateinit var mockMetaDao: MetaDao
    private lateinit var subject: LocalLiveStatRepository

    @Before
    fun setUp() {
        givenSubject()
    }

    private fun givenSubject() {
        whenever(mockDb.turnDao()).thenReturn(mockTurnDao)
        whenever(mockDb.metaDao()).thenReturn(mockMetaDao)
        subject = LocalLiveStatRepository(mockDb, mockMapperLive)
    }

    @Test
    fun fetchAllForGame() {
        subject.fetchAllForGame(2)
        verify(mockTurnDao).fetchAll(2)
        verify(mockMetaDao).fetchAll(2)
        verify(mockMapperLive).to(anyList(), anyList())
    }

    @Test
    fun fetchStat() {
        subject.fetchStat(12, 14)
        verify(mockTurnDao).fetchById(12)
        verify(mockMetaDao).fetchById(14)
    }

}