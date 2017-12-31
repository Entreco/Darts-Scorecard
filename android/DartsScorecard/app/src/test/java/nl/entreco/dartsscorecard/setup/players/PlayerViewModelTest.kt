package nl.entreco.dartsscorecard.setup.players

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 31/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class PlayerViewModelTest {

    private lateinit var subject: PlayerViewModel

    @Test
    fun `it should have correct name`() {
        givenSubject(0)
        thenPlayerNameIs("Player 1")
    }

    @Test
    fun `it should have correct teamIndex`() {
        givenSubject(0)
        thenTeamIndexIs(0)
    }

    @Test
    fun `it should update teams array when teams have been added`() {
        givenSubject(1)
        whenAddingNewPlayers(4)
        thenTeamIndices(1, 2, 3, 4)
        andTeamIndexStillIs(1)
    }

    @Test
    fun `it should update teamIndex when Spinner item selected`() {
        givenSubject(1)
        whenTeamIndexSelected(4)
        thenTeamIndexIs(4)
    }

    private fun givenSubject(index: Int) {
        subject = PlayerViewModel(index)
    }

    private fun whenAddingNewPlayers(count: Int) {
        subject.onTeamsUpdated(count)
        subject.onTeamSelected(0)
    }

    private fun whenTeamIndexSelected(expectedIndex: Int) {
        subject.onTeamSelected(expectedIndex)
    }

    private fun thenPlayerNameIs(expectedName: String) {
        assertEquals(expectedName, subject.name.get())
    }

    private fun thenTeamIndexIs(expectedIndex: Int) {
        assertEquals(expectedIndex, subject.teamIndex.get())
    }

    private fun thenTeamIndices(vararg indices: Int) {
        indices.forEachIndexed { index, _ ->
            assertEquals(indices[index], subject.teams[index])
        }
    }

    private fun andTeamIndexStillIs(expectedIndex: Int) {
        thenTeamIndexIs(expectedIndex)
    }
}