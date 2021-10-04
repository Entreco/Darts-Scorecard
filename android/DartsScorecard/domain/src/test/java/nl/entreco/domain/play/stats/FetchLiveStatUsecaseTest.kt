package nl.entreco.domain.play.stats

import nl.entreco.domain.TestBackground
import nl.entreco.domain.TestForeground
import nl.entreco.domain.model.LiveStat
import nl.entreco.domain.repository.LiveStatRepository
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Created by entreco on 24/01/2018.
 */
class FetchLiveStatUsecaseTest {

    private val mockLiveStatRepository: LiveStatRepository = mock()
    private val done: (FetchLiveStatResponse) -> Unit = mock()
    private val fail: (Throwable) -> Unit = mock()
    private val mockLiveStat: LiveStat = mock()
    private val bg = TestBackground()
    private val fg = TestForeground()
    private lateinit var subject: FetchLiveStatUsecase

    private val givenTurnId: Long = 8888
    private val givenMetaId: Long = 9999

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
        subject = FetchLiveStatUsecase(mockLiveStatRepository, bg, fg)
    }

    private fun whenFetchingStats() {
        val req = FetchLiveStatRequest(givenTurnId, givenMetaId)
        subject.exec(req, done, fail)
    }

    private fun whenFetchingStatsSucceeds() {
        whenever(mockLiveStatRepository.fetchStat(givenTurnId, givenMetaId)).thenReturn(mockLiveStat)
        val req = FetchLiveStatRequest(givenTurnId, givenMetaId)
        subject.exec(req, done, fail)
    }

    private fun whenFetchingStatsFails(err: Throwable) {
        whenever(mockLiveStatRepository.fetchStat(givenTurnId, givenMetaId)).thenThrow(err)
        val req = FetchLiveStatRequest(givenTurnId, givenMetaId)
        subject.exec(req, done, fail)
    }

    private fun thenStatsAreFetched() {
        verify(mockLiveStatRepository).fetchStat(givenTurnId, givenMetaId)
    }

    private fun thenDoneIsInvoked() {
        verify(done).invoke(any())
    }

    private fun thenFailIsInvoked() {
        verify(fail).invoke(any())
    }

}