package nl.entreco.dartsscorecard.play.score

import android.support.v7.widget.RecyclerView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.usecase.GetFinishUsecase
import nl.entreco.domain.settings.ScoreSettings
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 28/11/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class ScoreBindingsTest {

    @Mock private lateinit var mockRecyclerView: RecyclerView
    @Mock private lateinit var mockScoreAdapter: ScoreAdapter
    @Mock private lateinit var mockGetFinishUsecase: GetFinishUsecase

    private val givenTeams = arrayOf(Team("team1"), Team("team2"))
    private val givenScoreSettings = ScoreSettings()

    @Test
    fun `it should add all teams to the RecyclerView`() {
        ScoreBindings.addTeams(mockRecyclerView, mockScoreAdapter, givenTeams, givenScoreSettings, mockGetFinishUsecase)
        verify(mockScoreAdapter, times(givenTeams.size)).addItem(any())
    }

    @Test
    fun `it should notify adapter when all teams have been added`() {
        ScoreBindings.addTeams(mockRecyclerView, mockScoreAdapter, givenTeams, givenScoreSettings, mockGetFinishUsecase)
        verify(mockScoreAdapter).onTeamsReady()
    }
}