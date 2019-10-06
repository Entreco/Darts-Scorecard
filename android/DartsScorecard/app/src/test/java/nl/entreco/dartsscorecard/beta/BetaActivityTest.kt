package nl.entreco.dartsscorecard.beta

import android.content.Context
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
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
