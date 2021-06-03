package nl.entreco.dartsscorecard.setup

import android.content.Context
import org.mockito.kotlin.any
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 03/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Setup01ActivityTest {

    @Mock private lateinit var mockContext: Context
    val subject = spy(Setup01Activity())

    @Test
    fun launch() {
        Setup01Activity.launch(mockContext)
        verify(mockContext).startActivity(any())
    }

}