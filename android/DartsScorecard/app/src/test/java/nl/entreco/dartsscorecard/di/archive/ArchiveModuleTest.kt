package nl.entreco.dartsscorecard.di.archive

import nl.entreco.data.db.DscDatabase
import nl.entreco.domain.common.log.Logger
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ArchiveModuleTest {

    @Mock private lateinit var mockDb: DscDatabase
    @Mock private lateinit var mockLogger: Logger

    @Test
    fun provideArchiveRepository() {
        assertNotNull(ArchiveModule().provideArchiveRepository(mockDb, mockLogger))
    }
}