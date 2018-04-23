package nl.entreco.dartsscorecard.play

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.dartsscorecard.ad.AdViewModel
import nl.entreco.dartsscorecard.base.DialogHelper
import nl.entreco.dartsscorecard.play.score.GameLoadedNotifier
import nl.entreco.domain.common.log.Logger
import nl.entreco.domain.model.Game
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.mastercaller.MasterCaller
import nl.entreco.domain.play.mastercaller.ToggleSoundUsecase
import nl.entreco.domain.play.revanche.RevancheResponse
import nl.entreco.domain.play.revanche.RevancheUsecase
import nl.entreco.domain.play.start.Play01Request
import nl.entreco.domain.play.start.Play01Response
import nl.entreco.domain.play.start.Play01Usecase
import nl.entreco.domain.repository.AudioPrefRepository
import nl.entreco.domain.settings.ScoreSettings
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 19/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Play01ViewModelRevancheTest {

    @Mock private lateinit var mockPlay01Usecamse: Play01Usecase
    @Mock private lateinit var mockToggleSoundUsecase: ToggleSoundUsecase
    @Mock private lateinit var mockAudioPrefs: AudioPrefRepository
    @Mock private lateinit var mockAdProvider: AdViewModel
    @Mock private lateinit var mockRevancheUsecase: RevancheUsecase
    @Mock private lateinit var mockGameListeners: Play01Listeners
    @Mock private lateinit var mockMasterCaller: MasterCaller
    @Mock private lateinit var mockLoadNotifier: GameLoadedNotifier<ScoreSettings>
    @Mock private lateinit var mockDialogHelper: DialogHelper
    @Mock private lateinit var mockLogger: Logger
    @Mock private lateinit var mockGame: Game
    @Mock private lateinit var mockScoreSettings: ScoreSettings
    @Mock private lateinit var team1: Team
    @Mock private lateinit var team2: Team

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
        subject = Play01ViewModel(mockPlay01Usecamse, mockRevancheUsecase, mockGameListeners, mockMasterCaller, mockDialogHelper, mockToggleSoundUsecase, mockAudioPrefs, mockAdProvider, mockLogger)
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
