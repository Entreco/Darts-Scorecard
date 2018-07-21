package nl.entreco.domain.profile.fetch

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.ProfileStatRepository
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FetchProfileStatsUsecaseTest {

    private val bg = TestBackground()
    private val fg = TestForeground()
    @Mock private lateinit var mockProfileStatRepo: ProfileStatRepository
    private lateinit var subject : FetchProfileStatsUsecase

    @Mock private lateinit var mockDone: (FetchProfileStatResponse) -> Unit
    @Mock private lateinit var mockFail: (Throwable) -> Unit

    @Before
    fun setUp() {
        givenSubject()
    }

    @Test
    fun `it should fetch profiles for each player`() {
        subject.exec(FetchProfileStatRequest(longArrayOf(1,2,3)), mockDone, mockFail)
        verify(mockProfileStatRepo, times(3)).fetchForPlayer(any())
        verify(mockDone).invoke(any())
    }
    
    @Test
    fun `it should report error when something goes wrong`() {
        whenever(mockProfileStatRepo.fetchForPlayer(3)).thenThrow(RuntimeException("doh"))
        subject.exec(FetchProfileStatRequest(longArrayOf(1,2,3)), mockDone, mockFail)
        verify(mockProfileStatRepo, times(3)).fetchForPlayer(any())
        verify(mockFail).invoke(any())
    }

    private fun givenSubject() {
        subject = FetchProfileStatsUsecase(mockProfileStatRepo, bg, fg)
    }
}