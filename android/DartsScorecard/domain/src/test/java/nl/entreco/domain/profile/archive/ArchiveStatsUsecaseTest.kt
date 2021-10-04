package nl.entreco.domain.profile.archive

import nl.entreco.domain.TestBackground
import nl.entreco.domain.TestForeground
import nl.entreco.domain.repository.ArchiveRepository
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ArchiveStatsUsecaseTest {

    private val mockDone: (ArchiveStatsResponse) -> Unit = mock()
    private val mockFail: (Throwable) -> Unit = mock()
    private val mockArchiveRepository: ArchiveRepository = mock()
    private lateinit var subject: ArchiveStatsUsecase

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
