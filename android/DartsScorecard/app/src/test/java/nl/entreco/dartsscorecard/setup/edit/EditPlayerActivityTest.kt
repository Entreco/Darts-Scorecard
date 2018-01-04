package nl.entreco.dartsscorecard.setup.edit

import com.nhaarman.mockito_kotlin.spy
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Created by entreco on 04/01/2018.
 */
class EditPlayerActivityTest {
    val subject = spy(EditPlayerActivity())

    @Test
    fun `can be created`() {
        assertNotNull(subject)
    }
}