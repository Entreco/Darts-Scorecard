package nl.entreco.domain.play.stats

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.StatRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 24/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class FetchGameStatsUsecaseTest{
    @Mock private lateinit var mockStatRepository: StatRepository
    @Mock private lateinit var done: (FetchGameStatsResponse)->Unit
    @Mock private lateinit var fail: (Throwable)->Unit
    private val bg = TestBackground()
    private val fg = TestForeground()
    private lateinit var subject: FetchGameStatsUsecase

    private val givenGameId : Long = 8888

    @Test
    fun `it should fetch Stats from Repository`() {
        givenSubject()
        whenFetchingStats()
        thenStatsAreFetched()
    }
    @Test
    fun `it should report result when fetching succeeds`() {
        givenSubject()
        whenFetchingStatsSucceeds()
        thenDoneIsInvoked()
    }
    @Test
    fun `it should report failed when fetching fails`() {
        givenSubject()
        whenFetchingStatsFails(RuntimeException("do'h"))
        thenFailIsInvoked()
    }

    private fun givenSubject() {
        subject = FetchGameStatsUsecase(mockStatRepository, bg, fg)
    }

    private fun whenFetchingStats() {
        val req = FetchGameStatsRequest(givenGameId, "")
        subject.exec(req, done, fail)
    }

    private fun whenFetchingStatsSucceeds() {
        whenever(mockStatRepository.fetchAllForGame(givenGameId)).thenReturn(emptyMap())
        val req = FetchGameStatsRequest(givenGameId, "")
        subject.exec(req, done, fail)
    }

    private fun whenFetchingStatsFails(err: Throwable) {
        whenever(mockStatRepository.fetchAllForGame(givenGameId)).thenThrow(err)
        val req = FetchGameStatsRequest(givenGameId, "")
        subject.exec(req, done, fail)
    }

    private fun thenStatsAreFetched(){
        verify(mockStatRepository).fetchAllForGame(givenGameId)
    }

    private fun thenDoneIsInvoked(){
        verify(done).invoke(any())
    }

    private fun thenFailIsInvoked(){
        verify(fail).invoke(any())
    }
}