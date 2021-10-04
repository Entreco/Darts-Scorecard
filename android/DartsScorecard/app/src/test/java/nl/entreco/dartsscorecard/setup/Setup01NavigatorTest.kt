package nl.entreco.dartsscorecard.setup

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.viewpager.widget.PagerAdapter.POSITION_NONE
import nl.entreco.dartsscorecard.setup.players.PlayerEditor
import nl.entreco.dartsscorecard.setup.players.PlayerViewModel
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.setup.game.CreateGameResponse
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyZeroInteractions
import org.mockito.kotlin.whenever

/**
 * Created by Entreco on 02/01/2018.
 */
class Setup01NavigatorTest {

    private val mockActivity: Setup01Activity = mock()
    private val mockName: ObservableField<String> = mock()
    private val mockTeamIndex: ObservableInt = mock()
    private val mockPlayerViewModel: PlayerViewModel = mock()
    private val mockIntent: Intent = mock()
    private val mockPlayer: Player = mock()
    private val mockCallback: PlayerEditor.Callback = mock()
    private lateinit var subject: Setup01Navigator

    @Test
    fun `it should start EditPlayerActivity when editing player`() {
        givenEditRequest()
        givenSubject()
        subject.onEditPlayer(0, mockPlayerViewModel, emptyList(), emptyList())
        verify(mockActivity).startActivityForResult(any(), eq(1002))
    }

    @Test(expected = NullPointerException::class)
    fun `it should NOT start EditPlayerActivity when editing bot player`() {
        givenEditRequest(isHuman = false)
        givenSubject()
        subject.onEditPlayer(0, mockPlayerViewModel, emptyList(), emptyList())
        verify(mockActivity, never()).startActivityForResult(any(), eq(1002))
    }

    @Test
    fun `it should add new player with index`() {
        givenSubject()
        subject.onAddNewPlayer(2, emptyList(), emptyList())
        verify(mockActivity).startActivityForResult(any(), eq(1002))
    }

    @Test
    fun `it should notify player added, when item position==POSITION_NONE && no suggestion is given`() {
        givenIntentData("", "what's my name", POSITION_NONE, "", 3, POSITION_NONE)
        givenSubject()
        whenHandlingResult(1002, RESULT_OK)
        verify(mockCallback).onPlayerAdded("what's my name", 0)
    }

    @Test
    fun `it should notify player added, when item position==POSITION_NONE && no newName is given and 'Player 1'`() {
        givenIntentData("Player 1", "", POSITION_NONE, "", 2, POSITION_NONE)
        givenSubject()
        whenHandlingResult(1002, RESULT_OK)
        verify(mockCallback).onPlayerAdded("Player 1", 0)
    }

    @Test
    fun `it should NOT notify player added, when item position==POSITION_NONE && no newName is given but not 'Player 1'`() {
        givenIntentData("suggestion", "", POSITION_NONE, "", -1, POSITION_NONE)
        givenSubject()
        whenHandlingResult(1002, RESULT_OK)
        verify(mockCallback, never()).onPlayerAdded(any(), any())
    }

    @Test
    fun `it should notify bot added, when item position==POSITION_NONE`() {
        givenIntentData("", "", POSITION_NONE, "Beginner",  11, POSITION_NONE)
        givenSubject()
        whenHandlingResult(1002, RESULT_OK)
        verify(mockCallback).onBotAdded("Beginner", 11L)
    }

    @Test
    fun `it should NOT notify bot added, when item position!=POSITION_NONE`() {
        givenIntentData("", "", POSITION_NONE, "Beginner",  11, POSITION_NONE + 2)
        givenSubject()
        whenHandlingResult(1002, RESULT_OK)
        verify(mockCallback, never()).onBotAdded("Beginner", 11L)
    }

    @Test
    fun `it should notify player edited, when item position!=POSITION_NONE`() {
        givenIntentData("suggestion", "no hables names", POSITION_NONE, "", 0, POSITION_NONE + 2)
        givenSubject()
        whenHandlingResult(1002, RESULT_OK)
        verify(mockCallback).onPlayerEdited(POSITION_NONE + 2, POSITION_NONE, "no hables names", 0)
    }

    @Test
    fun `it should NOT notify callback, when RESULT_CANCELLED`() {
        givenIntentData("suggestion", "no hablez names", POSITION_NONE, "", 1, POSITION_NONE + 8)
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

    private fun givenIntentData(suggestion: String, name: String, teamIndex: Int, botName: String, botId: Long, position: Int) {
        whenever(mockIntent.getStringExtra("suggestion")).thenReturn(suggestion)
        whenever(mockIntent.getStringExtra("playerName")).thenReturn(name)
        whenever(mockIntent.getIntExtra(eq("teamIndex"), any())).thenReturn(teamIndex)
        whenever(mockIntent.getStringExtra(eq("botName"))).thenReturn(botName)
        whenever(mockIntent.getLongExtra(eq("botId"), eq(-1))).thenReturn(botId)
        whenever(mockIntent.getIntExtra(eq("positionInList"), any())).thenReturn(position)
    }

    private fun givenEditRequest(isHuman: Boolean = true) {
        whenever(mockPlayerViewModel.name).thenReturn(mockName)
        whenever(mockName.get()).thenReturn("another name")
        whenever(mockPlayerViewModel.teamIndex).thenReturn(mockTeamIndex)
        whenever(mockPlayerViewModel.isHuman).thenReturn(ObservableBoolean(isHuman))
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