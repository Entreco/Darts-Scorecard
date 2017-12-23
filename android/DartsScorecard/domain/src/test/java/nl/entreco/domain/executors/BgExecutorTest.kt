package nl.entreco.domain.executors

import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.ExecutorService

/**
 * Created by Entreco on 16/12/2017.
 */
class BgExecutorTest {

    @Mock private lateinit var mockRunnable: Runnable
    @Mock private lateinit var mockExecutor: ExecutorService
    private lateinit var subject: BgExecutor

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = BgExecutor(mockExecutor)
    }

    @Test
    fun `post should submit runnable`() {
        subject.post(mockRunnable)
        verify(mockExecutor).submit(mockRunnable)
    }

}