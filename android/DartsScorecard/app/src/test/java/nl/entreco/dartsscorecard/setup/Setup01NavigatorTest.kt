package nl.entreco.dartsscorecard.setup

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.viewpager.widget.PagerAdapter.POSITION_NONE
import com.nhaarman.mockito_kotlin.*
import nl.entreco.dartsscorecard.setup.players.PlayerEditor
import nl.entreco.dartsscorecard.setup.players.PlayerViewModel
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.setup.game.CreateGameResponse
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
        subject.onEditPlayer(0, mockPlayerViewModel, emptyList(), emptyList())
        verify(mockActivity).startActivityForResult(any(), eq(1002))
    }

    @Test
    fun `it should add new player with index`() {
        givenSubject()
        subject.onAddNewPlayer(2, emptyList(), emptyList())
        verify(mockActivity).startActivityForResult(any(), eq(1002))
    }

    @Test
    fun `it should notify player added, when item position==POSITION_NONE && no suggestion is given`() {
        givenIntentData("", "what's my name", POSITION_NONE, POSITION_NONE)
        givenSubject()
        whenHandlingResult(1002, RESULT_OK)
        verify(mockCallback).onPlayerAdded("what's my name", 0)
    }

    @Test
    fun `it should notify player added, when when item position==POSITION_NONE && no newName is given and 'Player 1'`() {
        givenIntentData("Player 1", "", POSITION_NONE, POSITION_NONE)
        givenSubject()
        whenHandlingResult(1002, RESULT_OK)
        verify(mockCallback).onPlayerAdded("Player 1", 0)
    }

    @Test
    fun `it should NOT notify player added, when when item position==POSITION_NONE && no newName is given but not 'Player 1'`() {
        givenIntentData("suggestion", "", POSITION_NONE, POSITION_NONE)
        givenSubject()
        whenHandlingResult(1002, RESULT_OK)
        verify(mockCallback, never()).onPlayerAdded(any(), any())
    }

    @Test
    fun `it should notify player edited, when item position!=POSITION_NONE`() {
        givenIntentData("suggestion", "no hables names", POSITION_NONE, POSITION_NONE + 2)
        givenSubject()
        whenHandlingResult(1002, RESULT_OK)
        verify(mockCallback).onPlayerEdited(POSITION_NONE + 2, POSITION_NONE, "no hables names", 0)
    }

    @Test
    fun `it should NOT notify callback, when RESULT_CANCELLED`() {
        givenIntentData("suggestion", "no hablez names", POSITION_NONE, POSITION_NONE + 8)
        givenSubject()
        whenHandlingResult(1002, RESULT_CANCELED)
        verifyZeroInteractions(mockCallback)
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
        subject.launch(CreateGameResponse(42, "1,2", -1, -2, -3, -4))
        verify(mockActivity).finish()
    }


    @Test
    fun `it should create editPlayerResponse`() {
        givenEditResponse(2, 5)
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

    private fun givenIntentData(suggestion: String, name: String, teamIndex: Int, position: Int) {
        whenever(mockIntent.getStringExtra("suggestion")).thenReturn(suggestion)
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

    private fun givenEditResponse(teamIndex: Int, position: Int) {
        whenever(mockIntent.getIntExtra(Setup01Navigator.EXTRA_TEAM_INDEX, POSITION_NONE)).thenReturn(teamIndex)
        whenever(mockIntent.getIntExtra(Setup01Navigator.EXTRA_POSITION_IN_LIST, POSITION_NONE)).thenReturn(position)
    }

    private fun givenCancelResponse(suggestion: String, teamIndex: Int, position: Int) {
        whenever(mockIntent.getStringExtra(Setup01Navigator.EXTRA_SUGGESTION)).thenReturn(suggestion)
        whenever(mockIntent.getIntExtra(Setup01Navigator.EXTRA_TEAM_INDEX, POSITION_NONE)).thenReturn(teamIndex)
        whenever(mockIntent.getIntExtra(Setup01Navigator.EXTRA_POSITION_IN_LIST, POSITION_NONE)).thenReturn(position)
    }

    private fun whenHandlingResult(request: Int, result: Int) {
        subject.handleResult(request, result, mockIntent, mockCallback)
    }
}