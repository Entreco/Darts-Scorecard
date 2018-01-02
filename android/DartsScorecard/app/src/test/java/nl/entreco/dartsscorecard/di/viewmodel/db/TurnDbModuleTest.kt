package nl.entreco.dartsscorecard.di.viewmodel.db

import nl.entreco.data.DscDatabase
import nl.entreco.data.db.turn.TurnMapper
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 02/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class TurnDbModuleTest {

    @Mock private lateinit var mockDb : DscDatabase
    @Mock private lateinit var mockMapper : TurnMapper

    @Test
    fun `it should provideTurnMapper`() {
        assertNotNull(TurnDbModule().provideTurnMapper())
    }

    @Test
    fun `it should provideTurnRepository`() {
        assertNotNull(TurnDbModule().provideTurnRepository(mockDb, mockMapper))
    }

}