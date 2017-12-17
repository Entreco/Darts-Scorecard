package nl.entreco.dartsscorecard.di.viewmodel.threading

import android.os.Handler
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.executors.BgExecutor
import nl.entreco.domain.executors.FgExecutor
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
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