package nl.entreco.data.play.repository

import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.data.DscDatabase
import nl.entreco.data.db.game.GameDao
import nl.entreco.data.db.game.GameMapper
import nl.entreco.domain.play.model.Game
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 15/11/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class LocalGameRepositoryTest {

    @Mock private lateinit var mockDb: DscDatabase
    @Mock private lateinit var mockGameDao: GameDao
    private lateinit var subject: LocalGameRepository
    private lateinit var mapper: GameMapper

    @Before
    fun setUp() {
        whenever(mockDb.gameDao()).thenReturn(mockGameDao)
        mapper = GameMapper()
        subject = LocalGameRepository(mockDb, mapper)
    }

    @Test
    fun `it should create a new game`() {
        val game = subject.create("uid", "1|2", 2, 3, 501, 0)
        assertNotNull(game)
    }
}