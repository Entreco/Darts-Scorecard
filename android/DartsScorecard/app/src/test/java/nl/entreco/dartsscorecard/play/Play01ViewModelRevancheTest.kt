package nl.entreco.dartsscorecard.play

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
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
import nl.entreco.domain.play.revanche.RevancheResponse
import nl.entreco.domain.play.revanche.RevancheUsecase
import nl.entreco.domain.play.start.Play01Request
import nl.entreco.domain.play.start.Play01Response
import nl.entreco.domain.play.start.Play01Usecase
import nl.entreco.domain.rating.AskForRatingUsecase
import nl.entreco.domain.repository.AudioPrefRepository
import nl.entreco.domain.settings.ScoreSettings
import nl.entreco.liblog.Logger
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 19/02/2018.
 */
class Play01ViewModelRevancheTest {

    private val mockCalc: CalculateBotScoreUsecase = mock()
    private val mockPlay01Usecamse: Play01Usecase = mock()
    private val mockToggleMusicUsecase: ToggleMusicUsecase = mock()
    private val mockMusicPlayer: MusicPlayer = mock()
    private val mockToggleSoundUsecase: ToggleSoundUsecase = mock()
    private val mockAudioPrefs: AudioPrefRepository = mock()
    private val mockAdProvider: AdViewModel = mock()
    private val mockRevancheUsecase: RevancheUsecase = mock()
    private val mockGameListeners: Play01Listeners = mock()
    private val mockMasterCaller: MasterCaller = mock()
    private val mockAskForRatingUsecase: AskForRatingUsecase = mock()
    private val mockLoadNotifier: GameLoadedNotifier<ScoreSettings> = mock()
    private val mockDialogHelper: DialogHelper = mock()
    private val mockLogger: Logger = mock()
    private val mockGame: Game = mock()
    private val mockNext: Next = mock()
    private val mockScoreSettings: ScoreSettings = mock()
    private val team1: Team = mock()
    private val team2: Team = mock()

    private val revancheCaptor = argumentCaptor<(RevancheResponse) -> Unit>()

    private lateinit var subject: Play01ViewModel

    private val doneCaptor = argumentCaptor<(Play01Response) -> Unit>()
    private val selectCaptor = argumentCaptor<(Int) -> Unit>()

    @Test
    fun `it should create new game when taking revanche`() {
        givenFullyLoadedSubject()
        whenTakingRevanche()
        thenRevancheUsecaseIsExecuted()
    }

    private fun givenFullyLoadedSubject() {
        whenever(mockNext.state).thenReturn(State.START)
        whenever(mockGame.next).thenReturn(mockNext)
        subject = Play01ViewModel(mockPlay01Usecamse, mockRevancheUsecase, mockGameListeners, mockMasterCaller, mockMusicPlayer, mockDialogHelper, mockToggleSoundUsecase, mockToggleMusicUsecase, mockAskForRatingUsecase, mockAudioPrefs, mockCalc, mockAdProvider, mockLogger)
        subject.load(Play01Request(1, "1|2", 501, 1, 1, 1), mockLoadNotifier)
        verify(mockPlay01Usecamse).loadGameAndStart(any(), doneCaptor.capture(), any())
        doneCaptor.lastValue.invoke(Play01Response(mockGame, mockScoreSettings, arrayOf(team1, team2), "1|2"))
    }

    private fun whenTakingRevanche() {
        subject.onRevanche()
        verify(mockDialogHelper).revanche(any(), any(), selectCaptor.capture())
        selectCaptor.lastValue.invoke(0)
    }

    private fun thenRevancheUsecaseIsExecuted() {
        verify(mockRevancheUsecase).recreateGameAndStart(any(), revancheCaptor.capture(), any())
        revancheCaptor.lastValue.invoke(RevancheResponse(mockGame, mockScoreSettings, arrayOf(team1, team2), "1|2"))
    }
}
