package nl.entreco.dartsscorecard.beta

import android.content.Context
import android.content.IntentSender
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 06/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class BetaActivityTest {

    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockBeta: BetaActivity
    @Mock private lateinit var mockIntentSender: IntentSender

    val subject = spy(BetaActivity())

    @Test
    fun `should start BetaActivity`() {
        BetaActivity.launch(mockContext)
        verify(mockContext).startActivity(any())
    }

    @Test
    fun `should start donate`() {
        BetaActivity.donate(mockBeta, mockIntentSender)
        verify(mockBeta).startIntentSenderForResult(eq(mockIntentSender), eq(180), any(), any(), any(), any())
    }
}
