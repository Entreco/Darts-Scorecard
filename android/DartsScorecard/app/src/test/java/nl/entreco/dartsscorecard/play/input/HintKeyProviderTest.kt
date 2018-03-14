package nl.entreco.dartsscorecard.play.input

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Entreco on 10/12/2017.
 */
class HintKeyProviderTest {
    private lateinit var subject: HintKeyProvider

    @Test
    fun `it should provide normal hints`() {
        subject = HintKeyProvider(false)
        assertEquals("0", subject.getHintForKey(-1))
        assertEquals("21", subject.getHintForKey(1))
        assertEquals("22", subject.getHintForKey(2))
        assertEquals("25", subject.getHintForKey(3))
        assertEquals("26", subject.getHintForKey(4))
        assertEquals("30", subject.getHintForKey(5))
        assertEquals("41", subject.getHintForKey(6))
        assertEquals("45", subject.getHintForKey(7))
        assertEquals("60", subject.getHintForKey(8))
        assertEquals("100", subject.getHintForKey(9))
        assertEquals("140", subject.getHintForKey(0))
        assertEquals("", subject.getHintForKey(10))
    }

    @Test
    fun `it should provide single hints`() {
        subject = HintKeyProvider(true)
        assertEquals("0", subject.getHintForKey(-1))
        assertEquals("10", subject.getHintForKey(1))
        assertEquals("12", subject.getHintForKey(2))
        assertEquals("14", subject.getHintForKey(3))
        assertEquals("15", subject.getHintForKey(4))
        assertEquals("16", subject.getHintForKey(5))
        assertEquals("17", subject.getHintForKey(6))
        assertEquals("18", subject.getHintForKey(7))
        assertEquals("19", subject.getHintForKey(8))
        assertEquals("20", subject.getHintForKey(9))
        assertEquals("25", subject.getHintForKey(0))
        assertEquals("", subject.getHintForKey(10))
    }
}