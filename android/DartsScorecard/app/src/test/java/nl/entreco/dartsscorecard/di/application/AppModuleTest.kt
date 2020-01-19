package nl.entreco.dartsscorecard.di.application

import android.content.Context
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.App
import nl.entreco.dartsscorecard.BuildConfig
import nl.entreco.data.analytics.FirebaseAnalytics
import nl.entreco.liblog.Logger
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 11/12/2017.
 */
class AppModuleTest {

    @Mock private lateinit var mockApp: App
    @Mock private lateinit var mockContext: Context
    private lateinit var subject: AppModule

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = AppModule(mockApp)
    }

    @Test
    fun application() {
        assertNotNull(subject.application())
    }

    @Test
    fun provideAnalytics() {
        whenever(mockApp.applicationContext).thenReturn(mockContext)
        assertTrue(subject.provideAnalytics() is FirebaseAnalytics)
    }

    @Test
    fun provideLogger() {
        assertTrue(subject.provideLogger() is Logger)
    }

    @Test
    fun provideDatabase() {
        assertNotNull(subject.provideDb(mockApp))
    }

    @Test(expected = IllegalStateException::class)
    fun provideFireStore() {
        assertNotNull(subject.provideFireStore())
    }

    @Test
    fun provideDebugMode() {
        if(BuildConfig.DEBUG) {
            assertTrue(subject.provideDebugMode())
        } else {
            assertFalse(subject.provideDebugMode())
        }
    }
}