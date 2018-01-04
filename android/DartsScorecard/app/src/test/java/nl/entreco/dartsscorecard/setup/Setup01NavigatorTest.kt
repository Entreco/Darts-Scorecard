package nl.entreco.dartsscorecard.setup

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.support.v4.view.PagerAdapter.POSITION_NONE
import com.nhaarman.mockito_kotlin.*
import nl.entreco.dartsscorecard.setup.players.PlayerEditor
import nl.entreco.dartsscorecard.setup.players.PlayerViewModel
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.repository.RetrieveGameRequest
import nl.entreco.domain.repository.TeamIdsString
import org.junit.Assert.assertNotNull
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
    @Mock private lateinit var mockPlayer: Player
    @Mock private lateinit var mockCallback: PlayerEditor.Callback
    private lateinit var subject: Setup01Navigator

    @Test
    fun `it should start EditPlayerActivity when editing player`() {
        givenEditRequest()
        givenSubject()
        subject.onEditPlayer(0, mockPlayerViewModel)
        verify(mockActivity).startActivityForResult(any(), eq(1002))
    }

    @Test
    fun `it should add new player with index`() {
        givenSubject()
        subject.onAddNewPlayer(2)
        verify(mockActivity).startActivityForResult(any(), eq(1002))
    }

    @Test
    fun `it should notify player added, when item position==POSITION_NONE`() {
        givenIntentData("what's my name", POSITION_NONE, POSITION_NONE)
        givenSubject()
        whenHandlingResult(1002,RESULT_OK)
        verify(mockCallback).onPlayerAdded("what's my name")
    }

    @Test
    fun `it should notify player edited, when item position!=POSITION_NONE`() {
        givenIntentData("no hables names", POSITION_NONE, POSITION_NONE + 2)
        givenSubject()
        whenHandlingResult(1002,RESULT_OK)
        verify(mockCallback).onPlayerEdited(POSITION_NONE + 2, POSITION_NONE, "no hables names")
    }

    @Test
    fun `it should notify player added, when RESULT_CANCELLED`() {
        givenIntentData("no hablez names", POSITION_NONE, POSITION_NONE + 8)
        givenSubject()
        whenHandlingResult(1002, RESULT_CANCELED)
        verify(mockCallback).onPlayerAdded("no hablez names")
    }

    @Test
    fun `it should ignore results that are not related to REQUEST_CODE`() {
        givenSubject()
        subject.handleResult(1, 2, mockIntent, mockCallback)
        verifyZeroInteractions(mockCallback)
    }

    @Test
    fun `it should launch Play01Activity`() {
        givenSubject()
        subject.launch(RetrieveGameRequest(42, TeamIdsString("1,2"), CreateGameRequest(-1, -2, -3, -4)))
        verify(mockActivity).finish()
    }


    @Test
    fun `it should create editPlayerResponse`() {
        givenEditResponse(2,5)
        assertNotNull(Setup01Navigator.editPlayerResponse(mockPlayer, mockIntent))
    }

    @Test
    fun `it should create cancelPlayerResponse`() {
        givenCancelResponse("Remco", 66, 5)
        assertNotNull(Setup01Navigator.cancelPlayerResponse(mockIntent))
    }

    private fun givenSubject() {
        subject = Setup01Navigator(mockActivity)
    }

    private fun givenIntentData(name: String, teamIndex: Int, position: Int) {
        whenever(mockIntent.getStringExtra("suggestion")).thenReturn(name)
        whenever(mockIntent.getStringExtra("playerName")).thenReturn(name)
        whenever(mockIntent.getIntExtra(eq("teamIndex"), any())).thenReturn(teamIndex)
        whenever(mockIntent.getIntExtra(eq("positionInList"), any())).thenReturn(position)
    }
    private fun givenEditRequest() {
        whenever(mockPlayerViewModel.name).thenReturn(mockName)
        whenever(mockName.get()).thenReturn("another name")
        whenever(mockPlayerViewModel.teamIndex).thenReturn(mockTeamIndex)
        whenever(mockTeamIndex.get()).thenReturn(4)
    }

    private fun givenEditResponse(teamIndex: Int, position: Int){
        whenever(mockIntent.getIntExtra(Setup01Navigator.EXTRA_TEAM_INDEX, POSITION_NONE)).thenReturn(teamIndex)
        whenever(mockIntent.getIntExtra(Setup01Navigator.EXTRA_POSITION_IN_LIST, POSITION_NONE)).thenReturn(position)
    }

    private fun givenCancelResponse(suggestion: String, teamIndex: Int, position: Int){
        whenever(mockIntent.getStringExtra(Setup01Navigator.EXTRA_SUGGESTION)).thenReturn(suggestion)
        whenever(mockIntent.getIntExtra(Setup01Navigator.EXTRA_TEAM_INDEX, POSITION_NONE)).thenReturn(teamIndex)
        whenever(mockIntent.getIntExtra(Setup01Navigator.EXTRA_POSITION_IN_LIST, POSITION_NONE)).thenReturn(position)
    }

    private fun whenHandlingResult(request: Int, result: Int) {
        subject.handleResult(request, result, mockIntent, mockCallback)
    }
}