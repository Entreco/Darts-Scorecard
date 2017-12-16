package nl.entreco.data

import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.data.play.repository.LocalGameRepository
import nl.entreco.domain.play.usecase.SetupModel
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

    @Before
    fun setUp() {
        whenever(mockDb.gameDao()).thenReturn(mockGameDao)
        subject = LocalGameRepository(mockDb)
    }

    @Test
    fun `it should create a new game`() {
        val game = subject.create("uid", 501, 0, 3, 2)
        assertNotNull(game)
    }
}