package nl.entreco.dartsscorecard.play

import com.nhaarman.mockito_kotlin.*
import nl.entreco.dartsscorecard.ad.AdViewModel
import nl.entreco.dartsscorecard.base.DialogHelper
import nl.entreco.dartsscorecard.play.score.GameLoadedNotifier
import nl.entreco.shared.log.Logger
import nl.entreco.domain.model.Game
import nl.entreco.domain.model.Next
import nl.entreco.domain.model.State
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.mastercaller.MasterCaller
import nl.entreco.domain.play.mastercaller.ToggleSoundUsecase
import nl.entreco.domain.play.revanche.RevancheUsecase
import nl.entreco.domain.play.start.Play01Request
import nl.entreco.domain.play.start.Play01Response
import nl.entreco.domain.play.start.Play01Usecase
import nl.entreco.domain.play.stats.UndoTurnRequest
import nl.entreco.domain.play.stats.UndoTurnResponse
import nl.entreco.domain.rating.AskForRatingUsecase
import nl.entreco.domain.repository.AudioPrefRepository
import nl.entreco.domain.settings.ScoreSettings
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 25/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Play01ViewModelUndoTest {

    @Mock private lateinit var mockNext: Next
    @Mock private lateinit var mockGame: Game
    @Mock private lateinit var mockTeam: Team
    @Mock private lateinit var mockSettings: ScoreSettings
    @Mock private lateinit var mockRequest: Play01Request
    @Mock private lateinit var mockResponse: Play01Response
    @Mock private lateinit var mockAdProvider: AdViewModel
    @Mock private lateinit var mockToggleSoundUsecase: ToggleSoundUsecase
    @Mock private lateinit var mockAskForRatingUsecase: AskForRatingUsecase
    @Mock private lateinit var mockAudioPrefs: AudioPrefRepository
    @Mock private lateinit var mockLoad: GameLoadedNotifier<ScoreSettings>
    @Mock private lateinit var mockLoaders: GameLoadedNotifier<Play01Response>

    @Mock private lateinit var mockPlay01Usecase: Play01Usecase
    @Mock private lateinit var mockRevancheUsecase: RevancheUsecase
    @Mock private lateinit var mockGameListeners: Play01Listeners
    @Mock private lateinit var mockMasterCaller: MasterCaller
    @Mock private lateinit var mockDialogHelper: DialogHelper
    @Mock private lateinit var mockLogger: Logger
    private lateinit var subject: Play01ViewModel

    private val undoRequestCaptor = argumentCaptor<UndoTurnRequest>()
    private val load = argumentCaptor<(Play01Response) -> Unit>()
    private val done = argumentCaptor<(UndoTurnResponse) -> Unit>()
    private val fail = argumentCaptor<(Throwable) -> Unit>()

    @Test
    fun `it should execute undoUsecase when undo pressed`() {
        givenSubject()
        whenUndoEvent()
        thenUndoLastTurnIsCalled()
        andLoadingIs(true)
    }

    @Test
    fun `it should call 'load()' again when undo succeeds`() {
        givenSubject()
        whenUndoUsecaseSucceeds()
        thenLoadGameAndStartIsCalledAgain()
    }

    @Test
    fun `it should log error, when undo fails`() {
        givenSubject()
        whenUndoUsecaseFails(RuntimeException("db exception dude"))
        thenErrorIsLogged()
    }

    private fun givenSubject() {
        whenever(mockGame.id).thenReturn(5)
        whenever(mockNext.state).thenReturn(State.START)
        whenever(mockGame.next).thenReturn(mockNext)
        whenever(mockResponse.game).thenReturn(mockGame)
        whenever(mockResponse.teams).thenReturn(arrayOf(mockTeam, mockTeam))
        whenever(mockResponse.teamIds).thenReturn("")
        whenever(mockResponse.settings).thenReturn(mockSettings)

        subject = Play01ViewModel(mockPlay01Usecase, mockRevancheUsecase, mockGameListeners, mockMasterCaller, mockDialogHelper, mockToggleSoundUsecase, mockAskForRatingUsecase, mockAudioPrefs, mockAdProvider, mockLogger)
        subject.load(mockRequest, mockLoad, mockLoaders)
        verify(mockPlay01Usecase).loadGameAndStart(eq(mockRequest), load.capture(), any())
        load.lastValue.invoke(mockResponse)
    }

    private fun whenUndoEvent() {
        subject.onUndo()
    }

    private fun whenUndoUsecaseSucceeds() {
        subject.onUndo()
        verify(mockPlay01Usecase).undoLastTurn(undoRequestCaptor.capture(), done.capture(), fail.capture())
        done.lastValue.invoke(UndoTurnResponse(1, 2))
    }

    private fun whenUndoUsecaseFails(err: Throwable) {
        subject.onUndo()
        verify(mockPlay01Usecase).undoLastTurn(undoRequestCaptor.capture(), done.capture(), fail.capture())
        fail.lastValue.invoke(err)
    }

    private fun thenUndoLastTurnIsCalled() {
        verify(mockPlay01Usecase).undoLastTurn(undoRequestCaptor.capture(), done.capture(), fail.capture())
    }

    private fun thenLoadGameAndStartIsCalledAgain() {
        verify(mockPlay01Usecase, times(2)).loadGameAndStart(any(), any(), any())
    }

    private fun thenErrorIsLogged() {
        verify(mockLogger).w(any())
    }

    private fun andLoadingIs(expected: Boolean) {
        assertEquals(expected, subject.loading.get())
    }
}
