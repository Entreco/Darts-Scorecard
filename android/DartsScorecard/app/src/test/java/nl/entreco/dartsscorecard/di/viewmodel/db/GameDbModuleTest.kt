package nl.entreco.dartsscorecard.di.viewmodel.db

import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.game.GameMapper
import nl.entreco.data.db.LocalGameRepository
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 17/12/2017.
 */
class GameDbModuleTest {
    @Mock private lateinit var mockDb: DscDatabase
    @Mock private lateinit var mockMapper: GameMapper
    private lateinit var subject: GameDbModule

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = GameDbModule()
    }

    @Test
    fun provideGameMapper() {
        assertNotNull(subject.provideGameMapper())
    }

    @Test
    fun provideGameRepository() {
        assertNotNull(subject.provideGameRepository(mockDb, mockMapper))
        assertTrue(subject.provideGameRepository(mockDb, mockMapper) is LocalGameRepository)
    }

}