package nl.entreco.dartsscorecard.launch

import android.content.Context
import org.mockito.kotlin.any
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 04/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class LaunchActivityTest {

    @Mock private lateinit var mockContext: Context
    val subject = spy(LaunchActivity())

    @Test
    fun launch() {
        LaunchActivity.launch(mockContext)
        verify(mockContext).startActivity(any())
    }
}