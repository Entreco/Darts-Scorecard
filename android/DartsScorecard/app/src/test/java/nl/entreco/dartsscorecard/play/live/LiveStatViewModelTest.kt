package nl.entreco.dartsscorecard.play.live

import com.nhaarman.mockito_kotlin.*
import nl.entreco.libads.ui.AdViewModel
import nl.entreco.dartsscorecard.archive.ArchiveServiceLauncher
import nl.entreco.domain.model.Game
import nl.entreco.domain.model.LiveStat
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.start.Play01Response
import nl.entreco.domain.play.stats.*
import nl.entreco.liblog.Logger
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 14/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class LiveStatViewModelTest {

    @Mock private lateinit var mockAdapter: LiveStatAdapter
    @Mock private lateinit var mockAdViewModel: AdViewModel
    @Mock private lateinit var mockFetchGameStatsUsecase: FetchGameStatsUsecase
    @Mock private lateinit var mockFetchLiveStatUsecase: FetchLiveStatUsecase
    @Mock private lateinit var mockArchiveServiceLauncher: ArchiveServiceLauncher
    @Mock private lateinit var mockLogger: Logger
    @Mock private lateinit var mockGame: Game
    @Mock private lateinit var mockResponse: Play01Response
    private lateinit var subject: LiveStatViewModel
    private var givenGameId: Long = 1003
    private var givenTeamIds: String = "1|2"
    private var givenTeams: Array<Team> = emptyArray()
    private var givenScores: Array<Score> = emptyArray()
    private var givenExistingStats: Map<Long, LiveStat> = emptyMap()

    private var givenUpdatedLiveStat: LiveStat = LiveStat(1, 2, 3, 4, 5, 6, 7, 8, 9, 2, 3, emptyList(), emptyList(), emptyMap(), emptyMap(), emptyMap(), emptyMap())
    private val statsDoneCaptor = argumentCaptor<(FetchGameStatsResponse) -> Unit>()
    private val statDoneCaptor = argumentCaptor<(FetchLiveStatResponse) -> Unit>()

    private val failCaptor = argumentCaptor<(Throwable) -> Unit>()

    @Test
    fun `it should create teamstats when loaded empty`() {
        givenSubjectLoaded()
        thenNumberOfTeamStatsIs(0)
    }

    @Test
    fun `it should create teamstats when loaded 2 teams`() {
        givenTeams("piet", "henk")
        givenSubjectLoaded()
        thenNumberOfTeamStatsIs(2)
    }

    @Test
    fun `it should fetch all stats when loaded 2 teams`() {
        givenTeams("piet", "henk")
        givenSubjectLoaded()
        thenStatsAreFetched()
    }

    @Test
    fun `it should update stats, when stats fetching successfully`() {
        givenTeams("piet", "henk")
        givenSubjectLoaded()
        givenExistingStats()
        whenStatsFetchSucceeds()
        thenTeamStatsAreNotNull(0..1)
    }

    @Test
    fun `it should log error, when stats fetching fails`() {
        givenTeams("piet", "henk")
        givenSubjectLoaded()
        whenStatsFetchFails(RuntimeException("Oops, cannot load stats for you buddy"))
        thenErrorIsLogged()
    }

    @Test
    fun `it should have empty avg when loaded`() {
        givenTeams("hein", "henk")
        givenSubjectLoaded()
        thenAveragesAre("--", "--")
    }

    @Test
    fun `it should have empty 180s when loaded`() {
        givenTeams("hein", "henk")
        givenSubjectLoaded()
        thenNumberOf180sIs("--", "--")
    }

    @Test
    fun `it should update existing stats, when stats change`() {
        givenTeams("1", "2")
        givenSubjectLoaded()
        whenStatsChange()
        thenStatIsFetched()
    }

    @Test
    fun `it should update stats, when stat fetch succeeds`() {
        givenTeams("1", "2")
        givenSubjectLoaded()
        givenUpdatedStat(1, 180)
        whenStatsChangeSucceeds()
        thenTeamStat180IsEmpty(0)
    }

    @Test
    fun `it should log error, when stat fetch fails`() {
        givenTeams("1", "2")
        givenSubjectLoaded()
        whenStatsChangeFails(RuntimeException("do'h"))
        thenErrorIsLogged()
    }

    @Test
    fun `it should archive stats on game finished`() {
        givenTeams("barney", "army")
        givenSubjectLoaded()
        whenGameFinished()
        thenStatsAreArchived()
    }

    private fun givenSubjectLoaded() {
        whenever(mockGame.id).thenReturn(givenGameId)
        whenever(mockResponse.game).thenReturn(mockGame)
        whenever(mockResponse.teamIds).thenReturn(givenTeamIds)
        subject = LiveStatViewModel(mockAdapter, mockAdViewModel, mockFetchGameStatsUsecase, mockFetchLiveStatUsecase, mockArchiveServiceLauncher, mockLogger)
        subject.onLoaded(givenTeams, givenScores, mockResponse, null)
    }

    private fun givenTeams(vararg names: String) {
        val teams = mutableListOf<Team>()
        names.forEachIndexed { index, name ->
            teams.add(Team(arrayOf(Player(name, id = index.toLong()))))
        }
        givenTeams = teams.toTypedArray()
    }

    private fun givenExistingStats(vararg liveStats: LiveStat) {
        val existing = HashMap<Long, LiveStat>()
        liveStats.forEachIndexed { index, stat ->
            val player = givenTeams[index % givenTeams.size].players[0]
            existing[player.id] = stat
        }
        givenExistingStats = existing
    }

    private fun givenUpdatedStat(playerId: Long, value: Int) {
        givenUpdatedLiveStat = givenUpdatedLiveStat.copy(playerId = playerId, n180 = value)
    }

    private fun whenStatsFetchSucceeds() {
        val req = FetchGameStatsRequest(givenGameId, givenTeamIds)
        verify(mockFetchGameStatsUsecase).exec(eq(req), statsDoneCaptor.capture(), failCaptor.capture())
        statsDoneCaptor.lastValue.invoke(FetchGameStatsResponse(givenGameId, givenExistingStats))
    }

    private fun whenStatsFetchFails(err: Throwable) {
        val req = FetchGameStatsRequest(givenGameId, givenTeamIds)
        verify(mockFetchGameStatsUsecase).exec(eq(req), statsDoneCaptor.capture(), failCaptor.capture())
        failCaptor.lastValue.invoke(err)
    }

    private fun whenStatsChange() {
        subject.onStatsChange(1, 2)
    }

    private fun whenStatsChangeSucceeds() {
        val turnId: Long = 1
        val metaId: Long = 20000
        val req = FetchLiveStatRequest(turnId, metaId)
        val response = FetchLiveStatResponse(givenUpdatedLiveStat)
        subject.onStatsChange(turnId, metaId)
        verify(mockFetchLiveStatUsecase).exec(eq(req), statDoneCaptor.capture(), failCaptor.capture())
        statDoneCaptor.lastValue.invoke(response)
    }

    private fun whenStatsChangeFails(err: Throwable) {
        subject.onStatsChange(8, 9)
        verify(mockFetchLiveStatUsecase).exec(any(), any(), failCaptor.capture())
        failCaptor.lastValue.invoke(err)
    }

    private fun whenGameFinished() {
        subject.onGameFinished(12)
    }

    private fun thenStatIsFetched() {
        verify(mockFetchLiveStatUsecase).exec(any(), any(), any())
    }

    private fun thenStatsAreFetched() {
        val req = FetchGameStatsRequest(givenGameId, givenTeamIds)
        verify(mockFetchGameStatsUsecase).exec(eq(req), statsDoneCaptor.capture(), failCaptor.capture())
    }

    private fun thenNumberOfTeamStatsIs(expected: Int) {
        assertEquals(expected, subject.teamStats.size)
    }

    private fun thenAveragesAre(vararg avgs: String) {
        avgs.forEachIndexed { index, avg ->
            assertEquals(avg, subject.teamStats[index]?.avg?.get().toString())
        }
    }

    private fun thenNumberOf180sIs(vararg n180s: String) {
        n180s.forEachIndexed { index, n180 ->
            assertEquals(n180, subject.teamStats[index]?.n180?.get().toString())
        }
    }

    private fun thenErrorIsLogged() {
        verify(mockLogger).e(any())
    }

    private fun thenTeamStatsAreNotNull(range: IntRange) {
        range.forEach {
            assertNotNull(subject.teamStats[it])
        }
    }

    private fun thenTeamStat180IsEmpty(index: Int) {
        assertEquals("--", subject.teamStats[index]?.n180?.get())
    }

    private fun thenStatsAreArchived() {
        verify(mockArchiveServiceLauncher).launch(any())
    }
}
