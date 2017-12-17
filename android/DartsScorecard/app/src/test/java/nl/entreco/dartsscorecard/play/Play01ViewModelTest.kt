package nl.entreco.dartsscorecard.play

import com.nhaarman.mockito_kotlin.*
import nl.entreco.dartsscorecard.play.score.GameLoadable
import nl.entreco.domain.play.listeners.PlayerListener
import nl.entreco.domain.play.listeners.ScoreListener
import nl.entreco.domain.play.listeners.SpecialEventListener
import nl.entreco.domain.play.model.*
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.model.players.TeamIdsString
import nl.entreco.domain.play.usecase.GameSettingsRequest
import nl.entreco.domain.play.usecase.RetrieveGameRequest
import nl.entreco.domain.play.usecase.RetrieveGameUsecase
import nl.entreco.domain.play.usecase.RetrieveTeamsUsecase
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 14/11/2017.
 */
class Play01ViewModelTest {

    private lateinit var subject: Play01ViewModel
    private lateinit var game: Game

    @Mock private lateinit var mockRetrieveGameUsecase: RetrieveGameUsecase
    @Mock private lateinit var mockRetrieveTeamUsecase: RetrieveTeamsUsecase
    @Mock private lateinit var mockLoadable: GameLoadable
    @Mock private lateinit var mockScoreListener: ScoreListener
    @Mock private lateinit var mockPlayerListener: PlayerListener
    @Mock private lateinit var mockSpecialListener: SpecialEventListener<*>

    private val teamCaptor = argumentCaptor<(Array<Team>) -> Unit>()
    private val captor = argumentCaptor<(Game) -> Unit>()

    private val gameSettingsRequest: GameSettingsRequest = GameSettingsRequest(501, 0, 3, 2)
    private val givenTeams = arrayOf(Team(arrayOf(Player("p1"))),Team(arrayOf(Player("p2"))))
    private val mockArbiter: Arbiter = Arbiter(Score(gameSettingsRequest.startScore), TurnHandler(gameSettingsRequest.startIndex)).apply { setTeams(givenTeams) }
    private val gameId: Long = 1002
    private val teamIds = TeamIdsString("1|2")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `it should notify GameLoadable when game was loaded`() {
        givenGameRetrieved()
        whenUiIsReady()
    }

    @Test
    fun `it should not notify GameLoadable when game was loaded`() {
        givenGameRetrieved()
        whenUiIsNotReady()
    }

    @Test
    fun `it should show correct score when initial turn submitted`() {
        givenGameRetrieved()
        givenPlayerListener(mockPlayerListener)
        whenUiIsReady()
        whenTurnSubmitted(Turn(Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_20))
        thenScoresAre(arrayOf(Score(441), Score(501)))
    }

    @Test
    fun `it should show correct score when second turn submitted`() {
        givenGameRetrieved()
        givenPlayerListener(mockPlayerListener)
        whenUiIsReady()
        whenTurnSubmitted(Turn(Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_20), Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.TRIPLE_20))
        thenScoresAre(arrayOf(Score(441), Score(321)))
    }

    @Test
    fun `it should show correct score when leg is finished`() {
        givenGameRetrieved()
        givenPlayerListener(mockPlayerListener)
        whenUiIsReady()
        whenTurnSubmitted(Turn(Dart.SINGLE_1, Dart.TEST_D250))
        thenScoresAre(arrayOf(Score(501, 1, 0), Score(501, 0, 0)))
    }

    @Test
    fun `it should notify scoreListeners on dart thrown`() {
        givenGameRetrieved()
        whenUiIsReady()
        givenScoreListener(mockScoreListener)
        whenDartThrown(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        thenScoreListenerIsNotifiedOfDartThrown()
    }


    @Test
    fun `it should notify scoreListeners when turns submitted`() {
        givenGameRetrieved()
        whenUiIsReady()
        givenScoreListener(mockScoreListener)
        whenTurnSubmitted(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        thenScoreListenerIsNotifiedOfScoreChange()
    }

    @Test
    fun `it should notify playerListeners when turns submitted`() {
        givenGameRetrieved()
        whenUiIsReady()
        givenPlayerListener(mockPlayerListener)
        whenTurnSubmitted(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        thenPlayerListenerIsNotified()
    }

    @Test
    fun `it should notify specialListeners when turns submitted`() {
        givenGameRetrieved()
        whenUiIsReady()
        givenSpecialListener(mockSpecialListener)
        whenTurnSubmitted(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        thenSpecialListenerIsNotified()
    }

    @Test
    fun `it should notify scoreListeners when UiIsReady`() {
        givenGameRetrieved()
        givenScoreListener(mockScoreListener)
        whenUiIsReady()
        thenScoreListenerIsNotifiedOfScoreChange()
    }

    @Test
    fun `it should notify playerListeners when UiIsReady`() {
        givenGameRetrieved()
        givenPlayerListener(mockPlayerListener)
        whenUiIsReady()
        thenPlayerListenerIsNotified()
    }

    @Test
    fun `it should NOT notify specialListeners when UiIsReady`() {
        givenGameRetrieved()
        givenSpecialListener(mockSpecialListener)
        whenUiIsReady()
        thenSpecialListenerIsNotNotified()
    }

    private fun givenScoreListener(vararg listeners: ScoreListener) {
        for (listener in listeners) {
            subject.addScoreListener(listener)
        }
    }

    private fun givenPlayerListener(vararg listeners: PlayerListener) {
        for (listener in listeners) {
            subject.addPlayerListener(listener)
        }
    }

    private fun givenSpecialListener(vararg listeners: SpecialEventListener<*>) {
        for (listener in listeners) {
            subject.addSpecialEventListener(listener)
        }
    }

    private fun givenGameRetrieved() {
        game = Game(101, mockArbiter)
        subject = Play01ViewModel(mockRetrieveGameUsecase, mockRetrieveTeamUsecase)
        val retrieveGameRequest = RetrieveGameRequest(gameId, teamIds, gameSettingsRequest)
        subject.retrieveGame(retrieveGameRequest, mockLoadable)
        verify(mockRetrieveTeamUsecase).start(eq(teamIds), teamCaptor.capture())
    }

    private fun whenTeamsRetrieved(){
        teamCaptor.firstValue.invoke(givenTeams)
        verify(mockRetrieveGameUsecase).start(eq(gameId), any(), captor.capture(), any())
    }

    private fun whenUiIsReady() {
        whenTeamsRetrieved()
        captor.firstValue.invoke(game)
        verify(mockLoadable).startWith(game, gameSettingsRequest, subject)
        subject.onLetsPlayDarts()
    }

    private fun whenUiIsNotReady() {
        verify(mockLoadable, never()).startWith(game, gameSettingsRequest, subject)
        // Some error callback in the future
    }

    private fun whenTurnSubmitted(vararg turns: Turn) {
        for (turn in turns) {
            subject.onTurnSubmitted(turn, Player(""))
        }
    }

    private fun whenDartThrown(vararg turns: Turn) {
        for (turn in turns) {
            subject.onDartThrown(turn, Player(""))
        }
    }

    private fun thenScoresAre(expected: Array<Score>) {
        assertArrayEquals(expected, game.scores)
    }

    private fun thenPlayerListenerIsNotified() {
        verify(mockPlayerListener).onNext(any())
    }

    private fun thenScoreListenerIsNotifiedOfScoreChange() {
        verify(mockScoreListener).onScoreChange(any(), any())
    }

    private fun thenScoreListenerIsNotifiedOfDartThrown() {
        verify(mockScoreListener).onDartThrown(any(), any())
    }

    private fun thenSpecialListenerIsNotNotified() {
        verify(mockSpecialListener, never()).onSpecialEvent(any(), any(), any(), any())
    }

    private fun thenSpecialListenerIsNotified() {
        verify(mockSpecialListener).onSpecialEvent(any(), any(), any(), any())
    }
}