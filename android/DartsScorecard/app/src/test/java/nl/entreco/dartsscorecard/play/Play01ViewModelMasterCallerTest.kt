package nl.entreco.dartsscorecard.play

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.base.DialogHelper
import nl.entreco.dartsscorecard.play.score.GameLoadedNotifier
import nl.entreco.domain.Logger
import nl.entreco.domain.model.*
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.mastercaller.*
import nl.entreco.domain.play.revanche.RevancheUsecase
import nl.entreco.domain.play.start.Play01Request
import nl.entreco.domain.play.start.Play01Response
import nl.entreco.domain.play.start.Play01Usecase
import nl.entreco.domain.settings.ScoreSettings
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 14/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Play01ViewModelMasterCallerTest{

    @Mock private lateinit var mockPlayGameUsecase: Play01Usecase
    @Mock private lateinit var mockRevancheUsecase: RevancheUsecase
    @Mock private lateinit var mock01Listeners: Play01Listeners
    @Mock private lateinit var mockMasterCaller: MasterCaller
    @Mock private lateinit var mockDialogHelper: DialogHelper
    @Mock private lateinit var mockLogger: Logger

    @Mock private lateinit var mockGame : Game
    @Mock private lateinit var mockRequest : Play01Request
    @Mock private lateinit var mockGameLoaded: GameLoadedNotifier<ScoreSettings>
    private val doneCaptor = argumentCaptor<(Play01Response) -> Unit>()

    private lateinit var subject: Play01ViewModel

    private val callerCaptor = argumentCaptor<MasterCallerRequest>()

    @Test
    fun `it should play sound when turn submitted`() {
        givenSubject()
        whenSubmittingTurn(Turn(Dart.DOUBLE_1))
        thenMasterCallerSays(Fx01())
    }

    private fun givenSubject() {
        givenGameAndRequest()
        whenLoadingOk()
    }

    private fun givenGameAndRequest(vararg loaders: GameLoadedNotifier<Play01Response>) {
        subject = Play01ViewModel(mockPlayGameUsecase, mockRevancheUsecase, mock01Listeners, mockMasterCaller, mockDialogHelper, mockLogger)
        subject.load(mockRequest, mockGameLoaded, *loaders)
    }

    private fun whenLoadingOk() {
        val player = Player("henk")
        val score = Score(501)
        whenever(mockGame.id).thenReturn(1)
        whenever(mockGame.previousScore()).thenReturn(score)
        whenever(mockGame.isNewMatchLegOrSet()).thenReturn(false)
        whenever(mockGame.getTurnCount()).thenReturn(1)
        whenever(mockGame.wasBreakMade(any())).thenReturn(false)
        whenever(mockGame.next).thenReturn(Next(State.NORMAL, Team(arrayOf(player)), 0, player, score))
        verify(mockPlayGameUsecase).loadGameAndStart(any(), doneCaptor.capture(), any())
        doneCaptor.firstValue.invoke(Play01Response(mockGame, ScoreSettings(501, 1,1, 0), emptyArray(), "1"))
    }

    private fun whenSubmittingTurn(turn: Turn) {
        subject.onTurnSubmitted(turn, Player("George Noble"))
    }

    private fun thenMasterCallerSays(expected: Sound) {
        verify(mockMasterCaller).play(callerCaptor.capture())
        val actual = callerCaptor.lastValue.toSound()
        when(expected) {
            is Fx00 -> assertTrue(actual is Fx00)
            is Fx01 -> assertTrue(actual is Fx01)
            else -> fail("expected $expected, but got $actual")
        }
    }
}