package nl.entreco.dartsscorecard.profile.edit

import android.content.Context
import android.os.Handler
import android.text.Editable
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.TextView
import com.nhaarman.mockito_kotlin.*
import nl.entreco.dartsscorecard.R
import nl.entreco.domain.Analytics
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.setup.players.FetchExistingPlayersResponse
import nl.entreco.domain.setup.players.FetchExistingPlayersUsecase
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 12/03/2018.
 */
class EditPlayerNameViewModelTest {

    private val mockView: TextView = mock()
    private val mockNavigator: EditPlayerNameNavigator = mock()
    private val mockAdapter: AdapterView<*> = mock()
    private val mockEditable: Editable = mock()
    private val mockContext: Context = mock()
    private val mockHandler: Handler = mock()
    private val mockAnalytics: Analytics = mock()
    private val mockFetchExistinPlayersUsecase: FetchExistingPlayersUsecase = mock()

    private lateinit var subject: EditPlayerNameViewModel
    private var givenExistingPlayers = emptyList<Player>()

    private val doneCaptor = argumentCaptor<(FetchExistingPlayersResponse) -> Unit>()
    private val failCaptor = argumentCaptor<(Throwable) -> Unit>()

    @Test
    fun `it should fetch existing players on init`() {
        givenSubject()
        thenExistingPlayersAreFetched()
    }

    @Test
    fun `it should have empty name initially`() {
        givenSubject()
        thenNameIs("")
    }

    @Test
    fun `it should have empty favdouble initially`() {
        givenSubject()
        thenFavouriteDoubleIs("")
    }

    @Test
    fun `it should add all players when fetching existing players succeeds (empty)`() {
        givenExistingPlayers()
        givenSubject()
        whenFetchingExistingPlayersSucceeds()
        thenAllPlayersCountIs(0)
    }

    @Test
    fun `it should add all plauyers when fetching existing players succeeds (20)`() {
        givenExistingPlayers("jan", "gerrit")
        givenSubject()
        whenFetchingExistingPlayersSucceeds()
        thenAllPlayersCountIs(2)
    }

    @Test
    fun `it should clear all players when fetching existing players fails`() {
        givenExistingPlayers("jan", "gerrit")
        givenSubject()
        whenFetchingExistingPlayersFails()
        thenAllPlayersCountIs(0)
    }

    @Test
    fun `it should set errorMessage when fetching existing players fails`() {
        givenExistingPlayers("jan", "gerrit")
        givenSubject()
        whenFetchingExistingPlayersFails()
        thenErrorMessageIs(R.string.err_unable_to_fetch_players)
    }

    @Test
    fun `it should store initialPlayerName as LowerCase`() {
        givenSubject()
        whenSettingPlayerName("Remco")
        thenInitialNameIs("remco")
    }

    @Test
    fun `it should NOT set to Lowercase onNameChanged`() { // Causes CAPS Lock to break
        givenSubject()
        whenNameChanged("ReMcO")
        thenNameIs("ReMcO")
    }

    @Test
    fun `it should set favouriteDouble onFavouriteDoubleSelected`() {
        givenSubject()
        whenFavouriteDoubleSelected(12)
        thenFavouriteDoubleIndexIs(12)
    }

    @Test
    fun `it should trackAnalytics onFavouriteDoubleSelected`() {
        givenSubject()
        whenFavouriteDoubleSelected(12)
        thenAnalyticsIsNotified(12)
    }

    @Test
    fun `it should set name onActionDone (IME_ACTION_DONE)`() {
        givenSubject()
        whenOnActionDone("REMCO", EditorInfo.IME_ACTION_DONE)
        thenNameIs("remco")
    }

    @Test
    fun `it should NOT set name onActionDone (IME_ACTION_DONE)`() {
        givenSubject()
        whenOnActionDone("Pietje", 0)
        thenNameIs("")
    }

    @Test
    fun `it should set typing(false) onDone when valid name entered`() {
        givenSubject()
        givenExistingPlayers("p1", "p2")
        whenNameChanged("NEW PLAYER")
        whenDone()
        thenIsTypingIs(false)
    }

    @Test
    fun `it should notifyNavigator onDone when valid name entered`() {
        givenSubject()
        givenExistingPlayers("p1", "p2")
        whenNameChanged("NEW PLAYER")
        whenDone()
        thenNavigatorIsNotified("new player")
    }

    @Test
    fun `it should set typing(true) onDone when name is empty`() {
        givenSubject()
        whenDone()
        thenIsTypingIs(true)
    }

    @Test
    fun `it should set errorMessage onDone when name is empty`() {
        givenSubject()
        whenDone()
        thenErrorMessageIs(R.string.err_player_name_is_empty)
    }
    @Test
    fun `it should set typing(true) onDone when name is already taken`() {
        givenExistingPlayers("p1")
        givenSubject()
        whenSettingPlayerName("player 1")
        whenFetchingExistingPlayersSucceeds()
        whenNameChanged("p1")
        whenDone()
        thenIsTypingIs(true)
    }

    @Test
    fun `it should set errorMessage onDone when name is already taken`() {
        givenExistingPlayers("p1")
        givenSubject()
        whenSettingPlayerName("player 1")
        whenFetchingExistingPlayersSucceeds()
        whenNameChanged("p1")
        whenDone()
        thenErrorMessageIs(R.string.err_player_already_exists)
    }

    private fun givenExistingPlayers(vararg players: String) {
        givenExistingPlayers = players.map { Player(it) }
    }

    private fun givenSubject() {
        subject = EditPlayerNameViewModel(mockHandler, mockAnalytics, mockFetchExistinPlayersUsecase)
        verify(mockFetchExistinPlayersUsecase).exec(doneCaptor.capture(), failCaptor.capture())
    }

    private fun whenFetchingExistingPlayersSucceeds() {
        doneCaptor.lastValue.invoke(FetchExistingPlayersResponse(givenExistingPlayers))
    }

    private fun whenFetchingExistingPlayersFails() {
        failCaptor.lastValue.invoke(Throwable("OOps"))
    }

    private fun whenSettingPlayerName(initialName: String) {
        val runnableCaptor = argumentCaptor<Runnable>()
        subject.playerName(initialName, "", mockContext)
        verify(mockHandler).postDelayed(runnableCaptor.capture(), eq(500))
        runnableCaptor.lastValue.run()
    }

    private fun whenNameChanged(name: String) {
        whenever(mockEditable.toString()).thenReturn(name)
        subject.onNameChanged(mockEditable)
    }

    private fun whenFavouriteDoubleSelected(index: Int) {
        whenever(mockAdapter.getItemAtPosition(any())).thenReturn("$index")
        subject.onFavouriteDoubleSelected(mockAdapter, index)
    }

    private fun whenOnActionDone(name: String, action: Int) {
        whenever(mockView.text).thenReturn(name)
        subject.onActionDone(mockView, action, mockNavigator)
    }

    private fun whenDone(){
        subject.onDone(mockNavigator)
    }

    private fun thenExistingPlayersAreFetched() {}

    private fun thenAllPlayersCountIs(expected: Int) {
        assertEquals(expected, subject.allPlayers.size)
    }

    private fun thenErrorMessageIs(expected: Int) {
        assertEquals(expected, subject.errorMsg.get())
    }

    private fun thenInitialNameIs(expected: String) {
        assertEquals(expected, subject.initialProfileName)
    }

    private fun thenNameIs(expected: String) {
        assertEquals(expected, subject.name.get())
    }

    private fun thenFavouriteDoubleIs(expected: String) {
        assertEquals(expected, subject.favDouble.get())
    }

    private fun thenFavouriteDoubleIndexIs(expected: Int) {
        assertEquals(expected, subject.favDoubleIndex.get())
        assertEquals("$expected", subject.favDouble.get())
    }

    private fun thenAnalyticsIsNotified(expected: Int){
        verify(mockAnalytics).setFavDoubleProperty("$expected")
    }

    private fun thenIsTypingIs(expected: Boolean) {
        assertEquals(expected, subject.isTyping.get())
    }

    private fun thenNavigatorIsNotified(expected: String){
        verify(mockNavigator).onDoneEditing(eq(expected), any())
    }
}