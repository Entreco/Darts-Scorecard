package nl.entreco.dartsscorecard.beta

import android.content.Context
import com.nhaarman.mockito_kotlin.any
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

    val subject = spy(BetaActivity())

    @Test
    fun `should start Play01Activity`() {
        BetaActivity.launch(mockContext)
        verify(mockContext).startActivity(any())
    }
}