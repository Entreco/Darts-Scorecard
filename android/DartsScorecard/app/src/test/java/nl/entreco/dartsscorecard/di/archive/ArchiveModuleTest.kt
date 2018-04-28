package nl.entreco.dartsscorecard.di.archive

import org.junit.Test

import org.junit.Assert.*

class ArchiveModuleTest {

    @Test
    fun provideArchiveRepository() {
        assertNotNull(ArchiveModule().provideArchiveRepository())
    }
}