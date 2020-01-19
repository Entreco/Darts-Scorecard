package nl.entreco.dartsscorecard.play

import com.nhaarman.mockito_kotlin.*
import nl.entreco.libads.ui.AdViewModel
import nl.entreco.dartsscorecard.base.DialogHelper
import nl.entreco.dartsscorecard.play.bot.CalculateBotScoreUsecase
import nl.entreco.dartsscorecard.play.score.GameLoadedNotifier
import nl.entreco.domain.model.Game
import nl.entreco.domain.model.Next
import nl.entreco.domain.model.State
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.mastercaller.MasterCaller
import nl.entreco.domain.play.mastercaller.MusicPlayer
import nl.entreco.domain.play.mastercaller.ToggleMusicUsecase
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
import nl.entreco.liblog.Logger
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 25/01/2018.
 */
class Play01ViewModelUndoTest {

    private val mockCalculateBotUsecase: CalculateBotScoreUsecase = mock()
    private val mockNext: Next = mock()
    private val mockGame: Game = mock()
    private val mockTeam: Team = mock()
    private val mockSettings: ScoreSettings = mock()
    private val mockRequest: Play01Request = mock()
    private val mockResponse: Play01Response = mock()
    private val mockAdProvider: AdViewModel = mock()
    private val mockToggleMusicUsecase: ToggleMusicUsecase = mock()
    private val mockMusicPlayer: MusicPlayer = mock()
    private val mockToggleSoundUsecase: ToggleSoundUsecase = mock()
    private val mockAskForRatingUsecase: AskForRatingUsecase = mock()
    private val mockAudioPrefs: AudioPrefRepository = mock()
    private val mockLoad: GameLoadedNotifier<ScoreSettings> = mock()
    private val mockLoaders: GameLoadedNotifier<Play01Response> = mock()

    private val mockPlay01Usecase: Play01Usecase = mock()
    private val mockRevancheUsecase: RevancheUsecase = mock()
    private val mockGameListeners: Play01Listeners = mock()
    private val mockMasterCaller: MasterCaller = mock()
    private val mockDialogHelper: DialogHelper = mock()
    private val mockLogger: Logger = mock()
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

        subject = Play01ViewModel(mockPlay01Usecase, mockRevancheUsecase, mockGameListeners, mockMasterCaller, mockMusicPlayer, mockDialogHelper, mockToggleSoundUsecase, mockToggleMusicUsecase, mockAskForRatingUsecase, mockAudioPrefs, mockCalculateBotUsecase, mockAdProvider, mockLogger)
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
