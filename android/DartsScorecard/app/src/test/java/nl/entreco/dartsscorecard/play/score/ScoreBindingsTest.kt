package nl.entreco.dartsscorecard.play.score

import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.finish.GetFinishUsecase
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

    @Mock private lateinit var mockTextView: TextView
    @Mock private lateinit var mockRecyclerView: RecyclerView
    @Mock private lateinit var mockScoreAdapter: ScoreAdapter
    @Mock private lateinit var mockGetFinishUsecase: GetFinishUsecase
    @Mock private lateinit var mockUiCallback: UiCallback

    private val givenTeams = arrayListOf(Team(arrayOf(Player("pietje"))), Team(arrayOf(Player("potje"))))
    private val givenScores = arrayListOf(Score(), Score())
    private val givenScoreSettings = ScoreSettings()

    @Test
    fun `it should add all teams to the RecyclerView`() {
        ScoreBindings.addTeams(mockRecyclerView, mockScoreAdapter, givenTeams, givenScores, givenScoreSettings, mockGetFinishUsecase, mockUiCallback)
        verify(mockScoreAdapter, times(givenTeams.size)).addItem(any())
    }

    @Test
    fun `it should notify adapter when all teams have been added`() {
        ScoreBindings.addTeams(mockRecyclerView, mockScoreAdapter, givenTeams, givenScores, givenScoreSettings, mockGetFinishUsecase, mockUiCallback)
        verify(mockScoreAdapter, times(givenTeams.size)).addItem(any())
        verify(mockUiCallback).onLetsPlayDarts(any())
    }

    @Test
    fun `it should scroll to position`() {
        ScoreBindings.scrollToCurrentTeam(mockRecyclerView, 0)
        verify(mockRecyclerView).smoothScrollToPosition(0)
    }

    @Test
    fun `it should set description when not blank`() {
        ScoreBindings.setMatchDescription(mockTextView, "some description")
        verify(mockTextView).text = "some description"
    }
    
    @Test
    fun `it should NOT set description when blank`() {
        ScoreBindings.setMatchDescription(mockTextView, " ")
        verify(mockTextView, never()).text = any()
    }

}