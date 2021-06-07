package nl.entreco.dartsscorecard.di.viewmodel.threading

import android.os.Handler
import nl.entreco.libcore.threading.BgExecutor
import nl.entreco.libcore.threading.FgExecutor
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

/**
 * Created by Entreco on 17/12/2017.
 */
class ThreadingModuleTest {

    private val mockHandler: Handler = mock()
    private lateinit var subject: ThreadingModule

    @Before
    fun setUp() {
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