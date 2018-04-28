package nl.entreco.dartsscorecard.di.application

import android.content.Context
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.App
import nl.entreco.dartsscorecard.BuildConfig
import nl.entreco.dartsscorecard.DscLogger
import nl.entreco.data.analytics.FirebaseAnalytics
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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
        assertTrue(subject.provideLogger() is DscLogger)
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
        assertTrue(subject.provideDebugMode())
    }
}