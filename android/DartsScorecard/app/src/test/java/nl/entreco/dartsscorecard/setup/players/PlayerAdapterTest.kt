package nl.entreco.dartsscorecard.setup.players

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 31/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class PlayerAdapterTest {

    @Mock private lateinit var mockPlayerViewHolder: SelectPlayerView
    @Mock private lateinit var mockEditor: PlayerEditor
    @InjectMocks private lateinit var subject: PlayerAdapter

    @Test
    fun `it should have zero players by default in playersMap`() {
        assertEquals(0, subject.itemCount)
        assertEquals(0, subject.participants().size)
    }

    @Test
    fun `it should add new player`() {
        givenPlayer("remco")
        assertEquals(1, subject.itemCount)
        assertEquals(1, subject.participants().size)
    }

    @Test
    fun `it should add new player with correct name`() {
        givenPlayer("remco")
        assertEquals("remco", subject.participants()[0].name.get())
    }

    @Test
    fun `it should bind to correct item`() {
        givenPlayer("cor")
        subject.onBindViewHolder(mockPlayerViewHolder, 0)
        verify(mockPlayerViewHolder).bind(any(), eq(mockEditor), any(), any(), eq(0))
    }

    @Test
    fun `it should replace existing player`() {
        givenPlayer("Frankie")
        subject.onPlayerEdited(0, 1, "new player", 0)
        assertTrue(subject.participants().map { it.name.get() }.contains("new player"))
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun `it should throw if replacing non-existing player`() {
        subject.onPlayerEdited(2, 1, "new player", 0)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun `it should throw exception if replace non-existing player`() {
        subject.onPlayerEdited(500, 1, "player that does not exist", 0)
        assertTrue(subject.participants().map { it.name.get() }.contains("two"))
    }

    private fun givenPlayer(name: String) {
        subject.onPlayerAdded(name, 0)
    }
}
