package nl.entreco.data.db.profile

import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.player.PlayerDao
import nl.entreco.data.db.player.PlayerTable
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
class LocalProfileInfoRepositoryTest {

    @Mock private lateinit var mockDb: DscDatabase
    @Mock private lateinit var mockPlayerDao: PlayerDao
    private lateinit var subject: LocalProfileInfoInfoRepository
    private lateinit var mapper: ProfileMapper

    @Before
    fun setUp() {
        whenever(mockDb.playerDao()).thenReturn(mockPlayerDao)
        mapper = ProfileMapper()
        subject = LocalProfileInfoInfoRepository(mockDb, mapper)
    }

    @Test
    fun fetchAll() {
        assertNotNull(subject.fetchAll(LongArray(0)))
    }

    @Test
    fun update() {
        whenever(mockPlayerDao.fetchById(0)).thenReturn(PlayerTable())
        assertNotNull(subject.update(0, "name", "image", "20"))
    }

    @Test(expected = IllegalStateException::class)
    fun `update can also throw for non-existing players`() {
        assertNotNull(subject.update(0, "name", "image", "double"))
    }
}
