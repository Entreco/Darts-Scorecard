package nl.entreco.dartsscorecard.di.viewmodel.db

import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.meta.LocalMetaRepository
import nl.entreco.data.db.meta.MetaMapper
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by entreco on 24/01/2018.
 */
class MetaDbModuleTest {
    @Mock private lateinit var mockDb: DscDatabase
    @Mock private lateinit var mockMapper: MetaMapper
    private lateinit var subject: MetaDbModule

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = MetaDbModule()
    }

    @Test
    fun provideMetaMapper() {
        Assert.assertNotNull(subject.provideMetaMapper())
    }

    @Test
    fun providePlayerRepository() {
        Assert.assertNotNull(subject.provideMetaRepository(mockDb, mockMapper))
        Assert.assertTrue(subject.provideMetaRepository(mockDb, mockMapper) is LocalMetaRepository)
    }
}