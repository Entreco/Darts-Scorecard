package nl.entreco.dartsscorecard.di.viewmodel.db

import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.stats.StatMapper
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 26/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class LiveStatDbModuleTest {

    @Mock private lateinit var mockDb: DscDatabase
    @Mock private lateinit var mockMapper: StatMapper

    @Test
    fun provideStatMapper() {
        assertNotNull(StatDbModule().provideStatMapper())
    }

    @Test
    fun provideStatRepository() {
        assertNotNull(StatDbModule().provideStatRepository(mockDb, mockMapper))
    }

}
