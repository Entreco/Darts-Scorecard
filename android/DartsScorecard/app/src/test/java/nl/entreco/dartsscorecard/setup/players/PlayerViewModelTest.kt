package nl.entreco.dartsscorecard.setup.players

import android.widget.AdapterView
import com.nhaarman.mockito_kotlin.whenever
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

    @Mock private lateinit var mockAdapter : AdapterView<*>
    private lateinit var subject: PlayerViewModel

    @Test
    fun `it should have correct name`() {
        givenSubject(0)
        thenPlayerNameIs("Player 0")
    }

    @Test
    fun `it should have correct teamIndex`() {
        givenSubject(0)
        thenTeamIndexIs(0)
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

    private fun whenTeamIndexSelected(expectedIndex: Int) {
        whenever(mockAdapter.getItemAtPosition(expectedIndex)).thenReturn(expectedIndex)
        subject.onTeamSelected(mockAdapter, expectedIndex)
    }

    private fun thenPlayerNameIs(expectedName: String) {
        assertEquals(expectedName, subject.name.get())
    }

    private fun thenTeamIndexIs(expectedIndex: Int) {
        assertEquals(expectedIndex, subject.teamIndex.get())
    }
}