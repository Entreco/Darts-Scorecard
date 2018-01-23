package nl.entreco.data.db.stats

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
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
class LocalStatRepositoryTest {

    @Mock private lateinit var mockDb: DscDatabase
    @Mock private lateinit var mockMapper: StatMapper
    @Mock private lateinit var mockTurnDao: TurnDao
    @Mock private lateinit var mockMetaDao: MetaDao
    private lateinit var subject : LocalStatRepository

    @Before
    fun setUp() {
        givenSubject()
    }

    private fun givenSubject() {
        whenever(mockDb.turnDao()).thenReturn(mockTurnDao)
        whenever(mockDb.metaDao()).thenReturn(mockMetaDao)
        subject= LocalStatRepository(mockDb, mockMapper)
    }

    @Test
    fun fetchAllForGame() {
        subject.fetchAllForGame(2)
        verify(mockTurnDao).fetchAll(2)
        verify(mockMetaDao).fetchAll(2)
        verify(mockMapper).to(anyList(), anyList())
    }

    @Test(expected = NotImplementedError::class)
    fun fetchAllForPlayer() {
        subject.fetchAllForPlayer(4)
    }

    @Test
    fun fetchStat() {
        subject.fetchStat(12,14)
        verify(mockTurnDao).fetchById(12)
        verify(mockMetaDao).fetchById(14)
    }

}