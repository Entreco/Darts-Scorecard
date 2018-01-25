package nl.entreco.dartsscorecard.di.viewmodel.threading

import android.os.Handler
import nl.entreco.domain.common.executors.BgExecutor
import nl.entreco.domain.common.executors.FgExecutor
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 17/12/2017.
 */
class ThreadingModuleTest {

    @Mock private lateinit var mockHandler : Handler
    private lateinit var subject : ThreadingModule

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = ThreadingModule()
    }

    @Test
    fun provideBackground() {
        assertTrue(subject.provideBackground() is BgExecutor)
    }

    @Test
    fun provideForeground() {
        assertTrue(subject.provideForeground(mockHandler) is FgExecutor)
    }

    @Test
    fun provideHandler() {
        assertNotNull(subject.provideHandler())
    }
}