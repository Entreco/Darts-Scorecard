package nl.entreco.dartsscorecard.di.setup

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Entreco on 02/01/2018.
 */
class EditPlayerModuleTest {

    @Test
    fun `it should provideSuggestedName`() {
        val name = "suggestion"
        assertEquals(name, EditPlayerModule(name).provideSuggestedName())
    }

}