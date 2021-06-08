package nl.entreco.libcore.threading

import android.os.Handler
import nl.entreco.libcore.threading.FgExecutor
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

/**
 * Created by Entreco on 16/12/2017.
 */
class FgExecutorTest {

    private val mockRunnable: Runnable = mock()
    private val mockHandler: Handler = mock()
    private lateinit var subject: FgExecutor

    @Before
    fun setUp() {
        subject = FgExecutor(mockHandler)
    }

    @Test
    fun `post should post runnable`() {
        subject.post(mockRunnable)
        verify(mockHandler).post(mockRunnable)
    }


}