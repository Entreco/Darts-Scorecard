package nl.entreco.dartsscorecard.setup.players

import android.widget.AdapterView
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 31/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class PlayerViewModelTest {

    @Mock private lateinit var mockAdapter: AdapterView<*>
    private lateinit var subject: PlayerViewModel

    @Test
    fun `it should have correct name(1)`() {
        givenSubject(1)
        thenPlayerNameIs("Player 1")
    }

    @Test
    fun `it should have correct name(-1)`() {
        givenSubject(-1)
        thenPlayerNameIs("Player -1")
    }

    @Test
    fun `it should have correct teamIndex(1)`() {
        givenSubject(1)
        thenTeamIndexIs(0)
    }

    @Test
    fun `it should have correct teamIndex(-1)`() {
        givenSubject(-1)
        thenTeamIndexIs(-2)
    }

    @Test
    fun `it should update teamIndex when Spinner item selected`() {
        givenSubject(1)
        whenTeamIndexSelected(4)
        thenTeamIndexIs(4)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun `it should update teamIndex with default when Spinner item selected is out of range`() {
        givenSubject(1)
        whenInvalidTeamIndexSelected(4)
    }

    @Test(expected = Exception::class)
    fun `it should throw if other exceptions occur`() {
        givenSubject(1)
        whenWeirdErrorOccurs()
    }

    private fun givenSubject(index: Int) {
        subject = PlayerViewModel(-1, index)
    }

    private fun whenTeamIndexSelected(expectedIndex: Int) {
        whenever(mockAdapter.getItemAtPosition(expectedIndex)).thenReturn(expectedIndex)
        subject.onTeamSelected(mockAdapter, expectedIndex)
    }

    private fun whenInvalidTeamIndexSelected(expectedIndex: Int) {
        whenever(mockAdapter.getItemAtPosition(expectedIndex)).thenThrow(IndexOutOfBoundsException())
        subject.onTeamSelected(mockAdapter, expectedIndex)
    }

    private fun whenWeirdErrorOccurs() {
        whenever(mockAdapter.getItemAtPosition(any())).thenReturn(NullPointerException())
        subject.onTeamSelected(mockAdapter, 600)
    }

    private fun thenPlayerNameIs(expectedName: String) {
        assertEquals(expectedName, subject.name.get())
    }

    private fun thenTeamIndexIs(expectedIndex: Int) {
        assertEquals(expectedIndex, subject.teamIndex.get())
    }
}