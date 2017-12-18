package nl.entreco.domain.play.model.players

import org.junit.Test

import org.junit.Assert.*

/**
 * Created by Entreco on 16/12/2017.
 */
class PlayerTest {

    @Test
    fun `it should print name in toString()`() {
        assertEquals("myName", Player("myName").toString())
    }

    @Test
    fun getName() {
        assertEquals("myName", Player("myName").name)
    }

    @Test
    fun `it should have correct favourite double`() {
        assertEquals(15, Player("", 0, PlayerPrefs(15)).prefs.favoriteDouble)
    }

}