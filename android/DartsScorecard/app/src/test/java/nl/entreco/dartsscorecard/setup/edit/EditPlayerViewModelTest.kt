package nl.entreco.dartsscorecard.setup.edit

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.nhaarman.mockito_kotlin.*
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.setup.usecase.CreatePlayerUsecase
import nl.entreco.domain.setup.usecase.FetchExistingPlayersUsecase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 02/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class EditPlayerViewModelTest {

    @Mock private lateinit var mockCreateUsecase: CreatePlayerUsecase
    @Mock private lateinit var mockFetchUsecase: FetchExistingPlayersUsecase
    @Mock private lateinit var mockView: TextView
    @Mock private lateinit var mockNavigator: EditPlayerNavigator
    private val doneCaptor = argumentCaptor<(List<Player>) -> Unit>()
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
    fun `it should create player when 'Done'`() {
        givenExistingPlayers("Remco", "EmReCo", "Re")
        givenSubject("Player 1")
        whenPressingImeAction("Henk", EditorInfo.IME_ACTION_DONE)
        thenPlayerIsCreated()
    }

    @Test
    fun `it should NOT create player when 'Go'`() {
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
        subject = EditPlayerViewModel(mockCreateUsecase, suggestedName, mockFetchUsecase)
        verify(mockFetchUsecase).exec(doneCaptor.capture(), failCaptor.capture())
    }

    private fun whenFetchingSucceeds() {
        doneCaptor.lastValue.invoke(expectedPlayers)
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

    private fun thenPlayerIsCreated() {
        verify(mockCreateUsecase).exec(any(), any(), any())
    }

    private fun thenPlayerIsNotCreated() {
        verifyZeroInteractions(mockCreateUsecase)
    }

}