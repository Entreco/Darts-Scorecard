package nl.entreco.domain.common.executors

import android.os.Handler
import org.mockito.kotlin.verify
import nl.entreco.libcore.threading.FgExecutor
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 16/12/2017.
 */
class FgExecutorTest {
    @Mock private lateinit var mockRunnable: Runnable
    @Mock private lateinit var mockHandler: Handler
    private lateinit var subject: nl.entreco.libcore.threading.FgExecutor

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = nl.entreco.libcore.threading.FgExecutor(mockHandler)
    }

    @Test
    fun `post should post runnable`() {
        subject.post(mockRunnable)
        verify(mockHandler).post(mockRunnable)
    }


}