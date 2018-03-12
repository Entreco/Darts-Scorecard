package nl.entreco.dartsscorecard.profile.edit

import android.content.Context
import android.os.Handler
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
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
@RunWith(MockitoJUnitRunner::class)
class EditPlayerNameViewModelTest {

    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockHandler: Handler
    @Mock private lateinit var mockAnalytics: Analytics
    @Mock private lateinit var mockFetchExistinPlayersUsecase: FetchExistingPlayersUsecase

    private lateinit var subject : EditPlayerNameViewModel
    private var givenExistingPlayers = emptyList<Player>()

    private val doneCaptor = argumentCaptor<(FetchExistingPlayersResponse)->Unit>()
    private val failCaptor = argumentCaptor<(Throwable)->Unit>()

    @Test
    fun `it should fetch existing players on init`() {
        givenSubject()
        thenExistingPlayersAreFetched()
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
        subject.playerName(initialName, "", mockContext)
    }


    private fun thenExistingPlayersAreFetched() {

    }

    private fun thenAllPlayersCountIs(expected: Int) {
        assertEquals(expected, subject.allPlayers.size)
    }

    private fun thenErrorMessageIs(expected: Int) {
        assertEquals(expected, subject.errorMsg.get())
    }

    private fun thenInitialNameIs(expected: String) {
        assertEquals(expected, subject.initialProfileName)
    }
}