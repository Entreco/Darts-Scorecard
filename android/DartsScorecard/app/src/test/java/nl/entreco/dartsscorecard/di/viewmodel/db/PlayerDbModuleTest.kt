package nl.entreco.dartsscorecard.di.viewmodel.db

import android.os.Handler
import nl.entreco.data.DscDatabase
import nl.entreco.data.db.player.PlayerMapper
import nl.entreco.data.play.repository.LocalPlayerRepository
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 17/12/2017.
 */
class PlayerDbModuleTest {

    @Mock private lateinit var mockDb : DscDatabase
    @Mock private lateinit var mockMapper : PlayerMapper
    private lateinit var subject : PlayerDbModule

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = PlayerDbModule()
    }

    @Test
    fun providePlayerMapper() {
        assertNotNull(subject.providePlayerMapper())
    }

    @Test
    fun providePlayerRepository() {
        assertNotNull(subject.providePlayerRepository(mockDb, mockMapper))
        assertTrue(subject.providePlayerRepository(mockDb, mockMapper) is LocalPlayerRepository)
    }

}