package nl.entreco.domain.profile.fetch

import nl.entreco.domain.TestBackground
import nl.entreco.domain.TestForeground
import nl.entreco.domain.repository.ProfileStatRepository
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class FetchProfileStatsUsecaseTest {

    private val bg = TestBackground()
    private val fg = TestForeground()
    private val mockProfileStatRepo: ProfileStatRepository = mock()
    private val mockDone: (FetchProfileStatResponse) -> Unit = mock()
    private val mockFail: (Throwable) -> Unit = mock()
    private lateinit var subject: FetchProfileStatsUsecase

    @Before
    fun setUp() {
        givenSubject()
    }

    @Test
    fun `it should fetch profiles for each player`() {
        subject.exec(FetchProfileStatRequest(longArrayOf(1, 2, 3)), mockDone, mockFail)
        verify(mockProfileStatRepo, times(3)).fetchForPlayer(any())
        verify(mockDone).invoke(any())
    }

    @Test
    fun `it should report error when something goes wrong`() {
        whenever(mockProfileStatRepo.fetchForPlayer(3)).thenThrow(RuntimeException("doh"))
        subject.exec(FetchProfileStatRequest(longArrayOf(1, 2, 3)), mockDone, mockFail)
        verify(mockProfileStatRepo, times(3)).fetchForPlayer(any())
        verify(mockFail).invoke(any())
    }

    private fun givenSubject() {
        subject = FetchProfileStatsUsecase(mockProfileStatRepo, bg, fg)
    }
}