package nl.entreco.dartsscorecard.beta

import android.content.Context
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Created by entreco on 06/02/2018.
 */
class BetaActivityTest {

    private val mockContext: Context = mock()
    private val subject = spy(BetaActivity())

    @Test
    fun `can be created`() {
        assertNotNull(subject)
    }

    @Test
    fun `should start BetaActivity`() {
        BetaActivity.launch(mockContext)
        verify(mockContext).startActivity(any())
    }
}
