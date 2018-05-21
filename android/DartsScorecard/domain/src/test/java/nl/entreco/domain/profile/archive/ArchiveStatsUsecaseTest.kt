package nl.entreco.domain.profile.archive

import com.nhaarman.mockito_kotlin.*
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.ArchiveRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ArchiveStatsUsecaseTest {

    @Mock private lateinit var mockDone: (ArchiveStatsResponse) -> Unit
    @Mock private lateinit var mockFail: (Throwable) -> Unit
    @Mock private lateinit var mockArchiveRepository: ArchiveRepository
    private lateinit var subject: ArchiveStatsUsecase
    private val responseCaptor = argumentCaptor<ArchiveStatsResponse>()

    @Test
    fun `it should archive stats`() {
        givenSubject()
        whenArchivingStats(2)
        thenArchiveIsCalled(2)
    }
    @Test
    fun `it should report success when archiving succeeds`() {
        givenSubject()
        whenArchivingStatsSucceeds(1)
        thenSuccessIsReported(1)
    }
    @Test
    fun `it should report fail when archiving fails`() {
        givenSubject()
        whenArchivingStatsFails()
        thenFailureIsReported()
    }

    private fun givenSubject() {
        subject = ArchiveStatsUsecase(mockArchiveRepository, TestBackground(), TestForeground())
    }

    private fun whenArchivingStats(gameId: Long) {
        subject.exec(ArchiveStatsRequest(gameId), mockDone, mockFail)
    }

    private fun whenArchivingStatsSucceeds(result: Int) {
        whenever(mockArchiveRepository.archive(1)).thenReturn(result)
        subject.exec(ArchiveStatsRequest(1), mockDone, mockFail)
    }

    private fun whenArchivingStatsFails() {
        whenever(mockArchiveRepository.archive(1)).thenThrow(RuntimeException("wtf"))
        subject.exec(ArchiveStatsRequest(1), mockDone, mockFail)
    }

    private fun thenArchiveIsCalled(expected: Long) {
        verify(mockArchiveRepository).archive(expected)
    }

    private fun thenSuccessIsReported(expected: Int) {
        verify(mockDone).invoke(eq(ArchiveStatsResponse(expected)))
    }
    private fun thenFailureIsReported() {
        verify(mockFail).invoke(any())
    }

}
