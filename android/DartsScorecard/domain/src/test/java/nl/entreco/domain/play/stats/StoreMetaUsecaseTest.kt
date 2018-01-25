package nl.entreco.domain.play.stats

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.TurnMeta
import nl.entreco.domain.play.ScoreEstimator
import nl.entreco.domain.repository.MetaRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 22/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class StoreMetaUsecaseTest {

    @Mock private lateinit var mockMetaRepo: MetaRepository
    @Mock private lateinit var mockDone: (Long, Long)->Unit
    @Mock private lateinit var mockFail: (Throwable)->Unit
    private val bg = TestBackground()
    private val fg = TestForeground()
    private val givenScoreEstimator = ScoreEstimator()
    private lateinit var subject: StoreMetaUsecase
    private lateinit var givenMeta: TurnMeta
    private lateinit var givenRequest: StoreMetaRequest
    private var givenTurnId: Long = -1
    private var givenMetaId: Long = -1

    @Test
    fun `it should notify done, when usecase succeeds`() {
        givenSubject()
        givenMeta()
        whenExecutingSucceeds()
        thenDoneIsCalled()
    }

    @Test
    fun `it should notify fail, when usecase fails`() {
        givenSubject()
        givenMeta()
        whenExecutingFails()
        thenFailIsCalled()
    }

    private fun givenSubject() {
        subject = StoreMetaUsecase(mockMetaRepo, givenScoreEstimator, bg, fg)
    }

    private fun givenMeta(){
        givenTurnId = 5
        givenMetaId = 8
        givenMeta = TurnMeta(3, 4, Score(), true, false)
        givenRequest = StoreMetaRequest(givenTurnId,2, Turn(), givenMeta)
    }

    private fun whenExecutingSucceeds(){
        whenever(mockMetaRepo.create(any(), any(), any(), any())).thenReturn(givenMetaId)
        subject.exec(givenRequest, mockDone, mockFail)
        verify(mockMetaRepo).create(eq(givenTurnId), eq(2), eq(givenMeta), any())
    }

    private fun whenExecutingFails(){
        whenever(mockMetaRepo.create(any(), any(), any(), any())).thenThrow(RuntimeException("oh no, more lemmings"))
        subject.exec(givenRequest, mockDone, mockFail)
        verify(mockMetaRepo).create(eq(givenTurnId), eq(2), eq(givenMeta), any())
    }

    private fun thenDoneIsCalled() {
        verify(mockDone).invoke(givenTurnId, givenMetaId)
    }

    private fun thenFailIsCalled() {
        verify(mockFail).invoke(any())
    }

}