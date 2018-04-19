package nl.entreco.dartsscorecard.play

import android.view.Menu
import android.view.MenuItem
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.ad.AdProvider
import nl.entreco.dartsscorecard.base.DialogHelper
import nl.entreco.dartsscorecard.play.score.GameLoadedNotifier
import nl.entreco.domain.Logger
import nl.entreco.domain.model.*
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.mastercaller.MasterCaller
import nl.entreco.domain.play.mastercaller.MasterCallerRequest
import nl.entreco.domain.play.mastercaller.ToggleSoundUsecase
import nl.entreco.domain.play.revanche.RevancheUsecase
import nl.entreco.domain.play.start.Play01Request
import nl.entreco.domain.play.start.Play01Response
import nl.entreco.domain.play.start.Play01Usecase
import nl.entreco.domain.repository.AudioPrefRepository
import nl.entreco.domain.settings.ScoreSettings
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 14/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Play01ViewModelMasterCallerTest {

    @Mock private lateinit var mockMenu: Menu
    @Mock private lateinit var mockMenuItem: MenuItem
    @Mock private lateinit var mockPlayGameUsecase: Play01Usecase
    @Mock private lateinit var mockToggleSoundUsecase: ToggleSoundUsecase
    @Mock private lateinit var mockAudioPrefs: AudioPrefRepository
    @Mock private lateinit var mockAdProvider: AdProvider
    @Mock private lateinit var mockRevancheUsecase: RevancheUsecase
    @Mock private lateinit var mock01Listeners: Play01Listeners
    @Mock private lateinit var mockMasterCaller: MasterCaller
    @Mock private lateinit var mockDialogHelper: DialogHelper
    @Mock private lateinit var mockLogger: Logger

    @Mock private lateinit var mockGame: Game
    @Mock private lateinit var mockRequest: Play01Request
    @Mock private lateinit var mockGameLoaded: GameLoadedNotifier<ScoreSettings>
    private val doneCaptor = argumentCaptor<(Play01Response) -> Unit>()

    private lateinit var subject: Play01ViewModel

    private val callerCaptor = argumentCaptor<MasterCallerRequest>()

    @Test
    fun `it should play sound when turn submitted`() {
        givenSubject()
        whenSubmittingTurn(Turn(Dart.DOUBLE_1))
        thenMasterCallerSays(2)
    }

    @Test
    fun `it should set initial toggle state (true)`() {
        givenSubject()
        whenInitializingToggle(true)
        thenMenuIs(true)
    }

    @Test
    fun `it should set initial toggle state (false0`() {
        givenSubject()
        whenInitializingToggle(false)
        thenMenuIs(false)
    }

    @Test
    fun `it should toggle masterCaller (true)`() {
        givenSubject()
        whenTogglingMasterCaller(true)
        thenToggleUsecaseIsCalled()
    }

    @Test
    fun `it should toggle masterCaller (false)`() {
        givenSubject()
        whenTogglingMasterCaller(false)
        thenToggleUsecaseIsCalled()
    }

    private fun givenSubject() {
        givenGameAndRequest()
        whenLoadingOk()
    }

    private fun givenGameAndRequest(vararg loaders: GameLoadedNotifier<Play01Response>) {
        subject = Play01ViewModel(mockPlayGameUsecase, mockRevancheUsecase, mock01Listeners, mockMasterCaller, mockDialogHelper, mockToggleSoundUsecase, mockAudioPrefs, mockAdProvider, mockLogger)
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
        doneCaptor.firstValue.invoke(Play01Response(mockGame, ScoreSettings(501, 1, 1, 0), emptyArray(), "1"))
    }

    private fun whenSubmittingTurn(turn: Turn) {
        subject.onTurnSubmitted(turn, Player("George Noble"))
    }

    private fun whenInitializingToggle(enabled: Boolean) {
        whenever(mockAudioPrefs.isMasterCallerEnabled()).thenReturn(enabled)
        whenever(mockMenu.findItem(R.id.menu_sound_settings)).thenReturn(mockMenuItem)
        subject.initToggleMenuItem(mockMenu)
    }

    private fun whenTogglingMasterCaller(enabled: Boolean) {
        whenever(mockMenuItem.isChecked).thenReturn(enabled)
        subject.toggleMasterCaller(mockMenuItem)
    }

    private fun thenMasterCallerSays(expected: Int) {
        verify(mockMasterCaller).play(callerCaptor.capture())
        assertEquals(expected, callerCaptor.lastValue.scored)
    }

    private fun thenMenuIs(expected: Boolean) {
        verify(mockMenuItem).isChecked = expected
    }

    private fun thenToggleUsecaseIsCalled() {
        verify(mockToggleSoundUsecase).toggle()
    }
}