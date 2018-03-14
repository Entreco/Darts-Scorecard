package nl.entreco.data.db.profile

import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.player.PlayerDao
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 26/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class LocalProfileRepositoryTest {

    @Mock private lateinit var mockDb: DscDatabase
    @Mock private lateinit var mockPlayerDao: PlayerDao
    private lateinit var subject: LocalProfileRepository
    private lateinit var mapper: ProfileMapper

    @Before
    fun setUp() {
        whenever(mockDb.playerDao()).thenReturn(mockPlayerDao)
        mapper = ProfileMapper()
        subject = LocalProfileRepository(mockDb, mapper)
    }

    @Test
    fun fetchAll() {
        assertNotNull(subject.fetchAll(LongArray(0)))
    }

}
