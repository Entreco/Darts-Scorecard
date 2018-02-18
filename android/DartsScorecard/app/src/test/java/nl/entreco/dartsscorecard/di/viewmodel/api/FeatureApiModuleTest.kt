package nl.entreco.dartsscorecard.di.viewmodel.api

import com.nhaarman.mockito_kotlin.mock
import nl.entreco.domain.Logger
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.exceptions.base.MockitoException
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 18/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class FeatureApiModuleTest {

    @Mock private lateinit var mockLogger: Logger

    @Test(expected = MockitoException::class)
    fun provideFbDb() {
        assertNotNull(FeatureApiModule().provideRemoteFeatureRepository(mock(), mockLogger))
    }

}