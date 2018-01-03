package nl.entreco.dartsscorecard.setup

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.support.v4.view.PagerAdapter.POSITION_NONE
import com.nhaarman.mockito_kotlin.*
import nl.entreco.dartsscorecard.setup.players.PlayerEditor
import nl.entreco.dartsscorecard.setup.players.PlayerViewModel
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.repository.RetrieveGameRequest
import nl.entreco.domain.repository.TeamIdsString
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 02/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Setup01NavigatorTest {

    @Mock private lateinit var mockActivity: Setup01Activity
    @Mock private lateinit var mockName: ObservableField<String>
    @Mock private lateinit var mockTeamIndex: ObservableInt
    @Mock private lateinit var mockPlayerViewModel: PlayerViewModel
    @Mock private lateinit var mockIntent: Intent
    @Mock private lateinit var mockCallback: PlayerEditor.Callback
    private lateinit var subject: Setup01Navigator

    @Test
    fun onEditPlayerName() {
        whenever(mockPlayerViewModel.name).thenReturn(mockName)
        whenever(mockName.get()).thenReturn("another name")
        whenever(mockPlayerViewModel.teamIndex).thenReturn(mockTeamIndex)
        whenever(mockTeamIndex.get()).thenReturn(4)
        givenSubject()
        subject.onEditPlayer(0, mockPlayerViewModel)
        verify(mockActivity).startActivityForResult(any(), eq(1002))
    }

    @Test
    fun onAddNewPlayer() {
        givenSubject()
        subject.onAddNewPlayer(2)
        verify(mockActivity).startActivityForResult(any(), eq(1002))
    }

    @Test
    fun `it should notify player added, when item position==POSITION_NONE`() {
        whenever(mockIntent.getStringExtra("playerName")).thenReturn("what my name")
        whenever(mockIntent.getIntExtra(eq("teamIndex"), any())).thenReturn(POSITION_NONE)
        whenever(mockIntent.getIntExtra(eq("positionInList"), any())).thenReturn(POSITION_NONE)

        givenSubject()
        subject.handleResult(1002, RESULT_OK, mockIntent, mockCallback)
        verify(mockCallback).onPlayerAdded(any(), any())
    }

    @Test
    fun `it should notify player edited, when item position!=POSITION_NONE`() {
        whenever(mockIntent.getStringExtra("playerName")).thenReturn("no hables names")
        whenever(mockIntent.getIntExtra(eq("teamIndex"), any())).thenReturn(POSITION_NONE)
        whenever(mockIntent.getIntExtra(eq("positionInList"), any())).thenReturn(POSITION_NONE + 2)

        givenSubject()
        subject.handleResult(1002, RESULT_OK, mockIntent, mockCallback)
        verify(mockCallback).onPlayerEdited(eq(POSITION_NONE + 2), any(), any())
    }

    @Test
    fun `it should ignore results that are not related to REQUEST_CODE`() {
        givenSubject()
        subject.handleResult(1, 2, mockIntent, mockCallback)
        verifyZeroInteractions(mockCallback)
    }

    @Test
    fun launch() {
        givenSubject()
        subject.launch(RetrieveGameRequest(42, TeamIdsString("1,2"), CreateGameRequest(-1, -2, -3, -4)))
        verify(mockActivity).finish()
    }

    private fun givenSubject() {
        subject = Setup01Navigator(mockActivity)
    }

}