package nl.entreco.domain.play.stats

import nl.entreco.domain.TestBackground
import nl.entreco.domain.TestForeground
import nl.entreco.domain.repository.GameRepository
import nl.entreco.domain.repository.MetaRepository
import nl.entreco.domain.repository.TurnRepository
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Created by entreco on 25/01/2018.
 */
class UndoTurnUsecaseTest {

    private val mockTurnRepo: TurnRepository = mock()
    private val mockMetaRepo: MetaRepository = mock()
    private val mockGameRepo: GameRepository = mock()
    private val mockDone: (UndoTurnResponse) -> Unit = mock()
    private val mockFail: (Throwable) -> Unit = mock()
    private val bg = TestBackground()
    private val fg = TestForeground()
    private lateinit var subject: UndoTurnUsecase
    private lateinit var givenUndoRequest: UndoTurnRequest

    @Before
    fun setUp() {
        givenSubject()
    }

    @Test
    fun `it should 'revert game finished' when undoing`() {
        givenUndoRequest(5)
        whenExecutingUsecase()
        thenGameIsUnFinished(5)
    }

    @Test
    fun `it should 'delete last turn' when undoing`() {
        givenUndoRequest(1006)
        whenExecutingUsecase()
        thenLastTurnIsDeleted(1006)
    }

    @Test
    fun `it should 'delete last meta' when undoing`() {
        givenUndoRequest(-1)
        whenExecutingUsecase()
        thenLastMetaIsDeleted(-1)
    }

    @Test
    fun `it should notify done when ok`() {
        givenUndoRequest(11)
        whenExecutingUsecase()
        thenDoneIsCalled()
    }

    @Test
    fun `it should notify fail when game fails`() {
        givenUndoRequest(66)
        whenUnfinishingFails(RuntimeException("unable to delete game"))
        thenFailIsCalled()
    }

    @Test
    fun `it should notify fail when turn fails`() {
        givenUndoRequest(66)
        whenUndoTurnFails(RuntimeException("unable to delete turns"))
        thenFailIsCalled()
    }

    @Test
    fun `it should notify fail when meta fails`() {
        givenUndoRequest(66)
        whenUndoMetaFails(RuntimeException("unable to delete meta"))
        thenFailIsCalled()
    }

    private fun givenSubject() {
        subject = UndoTurnUsecase(mockTurnRepo, mockMetaRepo, mockGameRepo, bg, fg)
    }

    private fun givenUndoRequest(gameId: Long) {
        givenUndoRequest = UndoTurnRequest(gameId)
    }

    private fun whenExecutingUsecase() {
        subject.exec(givenUndoRequest, mockDone, mockFail)
    }

    private fun whenUnfinishingFails(err: Throwable) {
        whenever(mockGameRepo.undoFinish(any())).thenThrow(err)
        subject.exec(givenUndoRequest, mockDone, mockFail)
    }

    private fun whenUndoTurnFails(err: Throwable) {
        whenever(mockTurnRepo.undo(any())).thenThrow(err)
        subject.exec(givenUndoRequest, mockDone, mockFail)
    }

    private fun whenUndoMetaFails(err: Throwable) {
        whenever(mockMetaRepo.undo(any())).thenThrow(err)
        subject.exec(givenUndoRequest, mockDone, mockFail)
    }

    private fun thenGameIsUnFinished(expected: Long) {
        verify(mockGameRepo).undoFinish(expected)
    }

    private fun thenLastTurnIsDeleted(expected: Long) {
        verify(mockTurnRepo).undo(expected)
    }

    private fun thenLastMetaIsDeleted(expected: Long) {
        verify(mockMetaRepo).undo(expected)
    }

    private fun thenDoneIsCalled() {
        verify(mockDone).invoke(any())
    }

    private fun thenFailIsCalled() {
        verify(mockFail).invoke(any())
    }
}