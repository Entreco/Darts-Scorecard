package nl.entreco.dartsscorecard.di.archive

import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.profile.ArchiveStatMapper
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ArchiveModuleTest {

    @Mock private lateinit var mockMapper: ArchiveStatMapper
    @Mock private lateinit var mockDb: DscDatabase

    @Test
    fun `it should provide ProfileStatMapper`() {
        assertNotNull(ArchiveModule().provideArchiveStatMapper())
    }


    @Test
    fun provideArchiveRepository() {
        assertNotNull(ArchiveModule().provideArchiveRepository(mockDb, mockMapper))
    }
}