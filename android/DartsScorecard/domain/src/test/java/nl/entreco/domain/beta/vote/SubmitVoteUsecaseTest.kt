package nl.entreco.domain.beta.vote

import nl.entreco.domain.TestBackground
import nl.entreco.domain.TestForeground
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.domain.repository.FeatureRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 07/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class SubmitVoteUsecaseTest {

    private val bg = TestBackground()
    private val fg = TestForeground()

    @Mock private lateinit var mockDone: (SubmitVoteResponse) -> Unit
    @Mock private lateinit var mockFail: (Throwable) -> Unit
    @Mock private lateinit var mockFeatureRepository: FeatureRepository
    private lateinit var subject: SubmitVoteUsecase

    @Test
    fun `it should report result when voting succeeds`() {
        givenSubject()
        whenExecutingSucceeds()
        thenResultIsReported()
    }

    @Test
    fun `it should report error when voting fails`() {
        givenSubject()
        whenExecutingFails(RuntimeException("IO exception"))
        thenFailIsReported()
    }

    private fun givenSubject() {
        subject = SubmitVoteUsecase(mockFeatureRepository, bg, fg)
    }

    private fun whenExecutingSucceeds() {
        subject.exec(SubmitVoteRequest("featureId", 1), mockDone, mockFail)
        verify(mockFeatureRepository).submitVote("featureId", 1)
    }

    private fun whenExecutingFails(err: Throwable) {
        whenever(mockFeatureRepository.submitVote(any(), any())).thenThrow(err)
        subject.exec(SubmitVoteRequest("featureId", 1), mockDone, mockFail)
        verify(mockFeatureRepository).submitVote("featureId", 1)
    }

    private fun thenResultIsReported() {
        verify(mockDone).invoke(any())
    }

    private fun thenFailIsReported() {
        verify(mockFail).invoke(any())
    }

}
