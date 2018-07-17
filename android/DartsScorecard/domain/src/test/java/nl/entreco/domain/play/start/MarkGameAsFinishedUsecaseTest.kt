package nl.entreco.domain.play.start

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.Analytics
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.GameRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 09/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class MarkGameAsFinishedUsecaseTest {

    @Mock private lateinit var mockAnalytics: Analytics
    @Mock private lateinit var mockGameRepository: GameRepository
    private val bg = TestBackground()
    private val fg = TestForeground()
    private lateinit var subject: MarkGameAsFinishedUsecase

    private var givenId: Long = -1
    private lateinit var givenMarkAsFinishedRequest: MarkGameAsFinishedRequest

    @Test
    fun `it should execute update`() {
        givenSubject()
        whenMarkingGameAsFinished(244)
        thenGameIsMarkedAsFinished()
    }

    @Test
    fun `it should not handle any errors`() {
        givenSubject()
        whenMarkingGameAsFinishedFails(RuntimeException("oops, db corrupt"))
        thenGameIsMarkedAsFinished()
    }

    @Test
    fun `it should log "game created" when executing usecase`() {
        givenSubject()
        whenMarkingGameAsFinished(244)
        thenSendToAnalytics("Game Finished")
    }

    private fun givenSubject() {
        subject = MarkGameAsFinishedUsecase(mockGameRepository, mockAnalytics, bg, fg)
    }

    private fun whenMarkingGameAsFinished(gameId: Long) {
        givenId = gameId
        givenMarkAsFinishedRequest = MarkGameAsFinishedRequest(gameId, "1")
        subject.exec(givenMarkAsFinishedRequest)
    }

    private fun whenMarkingGameAsFinishedFails(err: Throwable) {
        givenId = 666
        givenMarkAsFinishedRequest = MarkGameAsFinishedRequest(givenId, "1")
        whenever(mockGameRepository.finish(any(), any())).thenThrow(err)
        subject.exec(givenMarkAsFinishedRequest)
    }

    private fun thenGameIsMarkedAsFinished() {
        verify(mockGameRepository).finish(givenId, "1")
    }

    private fun thenSendToAnalytics(expected: String) {
        verify(mockAnalytics).trackAchievement(expected)
    }

}