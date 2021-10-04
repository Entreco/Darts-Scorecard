package nl.entreco.dartsscorecard.setup.edit

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import org.mockito.kotlin.*
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.setup.players.CreatePlayerResponse
import nl.entreco.domain.setup.players.CreatePlayerUsecase
import nl.entreco.domain.setup.players.FetchBotsUsecase
import nl.entreco.domain.setup.players.FetchExistingPlayersResponse
import nl.entreco.domain.setup.players.FetchExistingPlayersUsecase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 02/01/2018.
 */
class EditPlayerViewModelTest {

    private val mockBots: FetchBotsUsecase = mock()
    private val mockCreateUsecase: CreatePlayerUsecase = mock()
    private val mockFetchUsecase: FetchExistingPlayersUsecase = mock()
    private val mockView: TextView = mock()
    private val mockNavigator: EditPlayerNavigator = mock()
    private val mockPlayer: Player = mock()
    private val doneCaptor = argumentCaptor<(FetchExistingPlayersResponse) -> Unit>()
    private val createDoneCaptor = argumentCaptor<(CreatePlayerResponse) -> Unit>()
    private val failCaptor = argumentCaptor<(Throwable) -> Unit>()
    private lateinit var subject: EditPlayerViewModel
    private lateinit var expectedPlayers: MutableList<Player>

    @Test
    fun `it should have empty list when no existing players`() {
        givenSubject("Mvg")
        thenFilterPlayersIsEmpty()
    }

    @Test
    fun `it should have empty list when fetching players fails`() {
        givenSubject("Mvg")
        whenFetchingPlayersFails(IllegalStateException("oops"))
        thenFilterPlayersIsEmpty()
    }

    @Test
    fun `it should NOT filter list initially`() {
        givenExistingPlayers("Piet", "Hein", "Coco")
        givenSubject("Player 1")
        whenFetchingSucceeds()
        thenFilteredPlayersContains("Piet", "Hein", "Coco")
    }

    @Test
    fun `it should filter list with typed characters`() {
        givenExistingPlayers("Piet", "Hein", "Coco")
        givenSubject("Player 1")
        whenFetchingSucceeds()
        whenTypingLetters("P")
        thenFilteredPlayersContains("Piet")
    }

    @Test
    fun `it should filter list with when typing "Re" `() {
        givenExistingPlayers("Remco", "EmReCo", "Re")
        givenSubject("Player 1")
        whenFetchingSucceeds()
        whenTypingLetters("Re")
        thenFilteredPlayersContains("Remco", "EmReCo", "Re")
    }

    @Test
    fun `it should filter list with when typing "Rec" `() {
        givenExistingPlayers("Remco", "EmReCo", "Re")
        givenSubject("Player 1")
        whenFetchingSucceeds()
        whenTypingLetters("Rec")
        thenFilteredPlayersContains("EmReCo")
    }

    @Test
    fun `it should filter list with when typing, clearing and retyping `() {
        givenExistingPlayers("Abcdef", "Abcd", "Abc")
        givenSubject("Player 1")
        whenFetchingSucceeds()
        whenTypingLetters("Abc")
        thenFilteredPlayersContains("Abcdef", "Abcd", "Abc")
        whenTypingLetters("Abcdef")
        thenFilteredPlayersContains("Abcdef")
        whenTypingLetters("")
        thenFilteredPlayersContains("Abcdef", "Abcd", "Abc")
    }

    @Test
    fun `it should add Players whos name contains substring`() {
        givenExistingPlayers("Piet", "Heintje", "Coco")
        givenSubject("Player 1")
        whenFetchingSucceeds()
        whenTypingLetters("t")
        thenFilteredPlayersContains("Piet", "Heintje")
    }

    @Test
    fun `it should create player when 'Done'`() {
        givenExistingPlayers("Remco", "EmReCo", "Re")
        givenSubject("Player 1")
        whenPressingImeAction("Henk", EditorInfo.IME_ACTION_DONE)
        thenPlayerIsCreated()
    }


    @Test
    fun `it should navigate when player created succeeds`() {
        givenExistingPlayers("Remco", "EmReCo", "Re")
        givenSubject("Player 1")
        whenPressingImeAction("Henk", EditorInfo.IME_ACTION_DONE)
        whenPlayerIsCreated()
        thenNavigateWithSelectedPlayer()
    }

    @Test
    fun `it should NOT navigate when player creation fails`() {
        givenExistingPlayers("Remco", "EmReCo", "Re")
        givenSubject("Player 1")
        whenPressingImeAction("Willem", EditorInfo.IME_ACTION_DONE)
        whenPlayerIsNotCreated(RuntimeException("Unable to create Player"))
        thenNotNavigateWithSelectedPlayer()
    }

    @Test
    fun `it should NOT create player if it already exists when 'Done' pressed`() {
        givenExistingPlayers("plaYer 1")
        givenSubject("Player 1")
        whenFetchingSucceeds()
        whenPressingImeAction("pLaYer 1", EditorInfo.IME_ACTION_DONE)
        thenPlayerIsNotCreated()
    }

    @Test
    fun `it should NOT create player when 'Go' pressed`() {
        givenExistingPlayers("Remco", "EmReCo", "Re")
        givenSubject("Player 1")
        whenPressingImeAction("Willem", EditorInfo.IME_ACTION_GO)
        thenPlayerIsNotCreated()
    }

    private fun givenExistingPlayers(vararg names: String) {
        expectedPlayers = emptyList<Player>().toMutableList()
        names.forEachIndexed { index, name ->
            expectedPlayers.add(Player(name, index.toLong()))
        }
    }

    private fun givenSubject(suggestedName: String) {
        subject = EditPlayerViewModel(mockCreateUsecase, LongArray(0), LongArray(0), suggestedName, mockFetchUsecase, mockBots)
        verify(mockFetchUsecase).exec(doneCaptor.capture(), failCaptor.capture())
    }

    private fun whenFetchingSucceeds() {
        doneCaptor.lastValue.invoke(FetchExistingPlayersResponse(expectedPlayers))
    }

    private fun whenFetchingPlayersFails(err: Throwable) {
        failCaptor.lastValue.invoke(err)
    }

    private fun whenTypingLetters(chars: String) {
        subject.filter(chars)
    }

    private fun whenPressingImeAction(typed: String, action: Int) {
        whenever(mockView.text).thenReturn(typed)
        subject.onActionDone(mockView, action, mockNavigator)
    }

    private fun thenFilterPlayersIsEmpty() {
        assertTrue(subject.filteredPlayers.isEmpty())
    }

    private fun thenFilteredPlayersContains(vararg players: String) {
        players.forEach {
            assertTrue("missing $it", subject.filteredPlayers.map { it.name }.contains(it))
        }
        assertEquals(players.size, subject.filteredPlayers.size)
    }

    private fun whenPlayerIsCreated() {
        verify(mockCreateUsecase).exec(any(), createDoneCaptor.capture(), any())
        createDoneCaptor.lastValue.invoke(CreatePlayerResponse(mockPlayer))
    }

    private fun whenPlayerIsNotCreated(err: Throwable) {
        verify(mockCreateUsecase).exec(any(), any(), failCaptor.capture())
        failCaptor.lastValue.invoke(err)
    }

    private fun thenPlayerIsCreated() {
        verify(mockCreateUsecase).exec(any(), any(), any())
    }

    private fun thenPlayerIsNotCreated() {
        verifyZeroInteractions(mockCreateUsecase)
    }

    private fun thenNavigateWithSelectedPlayer() {
        verify(mockNavigator).onSelected(any<Player>())
    }

    private fun thenNotNavigateWithSelectedPlayer() {
        verifyZeroInteractions(mockNavigator)
    }

}