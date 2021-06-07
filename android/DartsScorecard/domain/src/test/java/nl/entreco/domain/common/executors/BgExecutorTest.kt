package nl.entreco.domain.common.executors

import org.mockito.kotlin.verify
import nl.entreco.libcore.threading.BgExecutor
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import java.util.concurrent.ExecutorService

/**
 * Created by Entreco on 16/12/2017.
 */
class BgExecutorTest {

    private val mockRunnable: Runnable = mock()
    private val mockExecutor: ExecutorService = mock()
    private lateinit var subject: BgExecutor

    @Before
    fun setUp() {
        subject = BgExecutor(mockExecutor)
    }

    @Test
    fun `post should submit runnable`() {
        subject.post(mockRunnable)
        verify(mockExecutor).submit(mockRunnable)
    }

}