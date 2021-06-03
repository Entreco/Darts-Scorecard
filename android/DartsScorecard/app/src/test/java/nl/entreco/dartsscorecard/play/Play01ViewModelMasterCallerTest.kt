package nl.entreco.dartsscorecard.play

import android.view.Menu
import android.view.MenuItem
import org.mockito.kotlin.*
import nl.entreco.dartsscorecard.R
import nl.entreco.libads.ui.AdViewModel
import nl.entreco.dartsscorecard.base.DialogHelper
import nl.entreco.dartsscorecard.play.score.GameLoadedNotifier
import nl.entreco.domain.model.*
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.dartsscorecard.sounds.mastercaller.*
import nl.entreco.domain.play.revanche.RevancheUsecase
import nl.entreco.domain.play.start.Play01Request
import nl.entreco.domain.play.start.Play01Response
import nl.entreco.domain.play.start.Play01Usecase
import nl.entreco.domain.rating.AskForRatingUsecase
import nl.entreco.dartsscorecard.sounds.AudioPrefRepository
import nl.entreco.domain.settings.ScoreSettings
import nl.entreco.liblog.Logger
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 14/03/2018.
 */
class Play01ViewModelMasterCallerTest {

    private val mockMenu: Menu = mock()
    private val mockMenuItem: MenuItem = mock()
    private val mockPlayGameUsecase: Play01Usecase = mock()
    private val mockAskForRatingUsecase: AskForRatingUsecase = mock()
    private val mockToggleSoundUsecase: ToggleSoundUsecase = mock()
    private val mockAudioPrefs: nl.entreco.dartsscorecard.sounds.AudioPrefRepository = mock()
    private val mockAdProvider: AdViewModel = mock()
    private val mockToggleMusicUsecase: ToggleMusicUsecase = mock()
    private val mockMusicPlayer: MusicPlayer = mock()
    private val mockRevancheUsecase: RevancheUsecase = mock()
    private val mock01Listeners: Play01Listeners = mock()
    private val mockMasterCaller: MasterCaller = mock()
    private val mockDialogHelper: DialogHelper = mock()
    private val mockLogger: Logger = mock()

    private val mockGame: Game = mock()
    private val mockRequest: Play01Request = mock()
    private val mockGameLoaded: GameLoadedNotifier<ScoreSettings> = mock()
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
    fun `it should set initial toggle state (false)`() {
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
        subject = Play01ViewModel(mockPlayGameUsecase, mockRevancheUsecase, mock01Listeners, mockMasterCaller, mockMusicPlayer, mockDialogHelper, mockToggleSoundUsecase, mockToggleMusicUsecase, mockAskForRatingUsecase, mockAudioPrefs, mockAdProvider, mockLogger)
        subject.load(mockRequest, mockGameLoaded, *loaders)
    }

    private fun whenLoadingOk() {
        val player = Player("henk")
        val score = Score(501)
        whenever(mockGame.id).thenReturn(1)
        whenever(mockGame.previousScore()).thenReturn(score)
        whenever(mockGame.getTurnCount()).thenReturn(1)
        whenever(mockGame.wasBreakMade(any())).thenReturn(false)
        whenever(mockGame.next).thenReturn(Next(State.NORMAL, Team(arrayOf(player)), 0, player, score))
        verify(mockPlayGameUsecase).loadGameAndStart(any(), doneCaptor.capture(), any())
        doneCaptor.firstValue.invoke(Play01Response(mockGame, ScoreSettings(501, 1, 1, 0), emptyArray(), "1"))
        verify(mockMasterCaller, never()).play(any())
        reset(mockMasterCaller)
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