package nl.entreco.domain.play.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Created by Entreco on 24/11/2017.
 */
class DartTest {

    @Test
    fun `next should be empty`() {
        assertEquals(Dart.DOUBLE_1,  Dart.NONE.next())
    }
}