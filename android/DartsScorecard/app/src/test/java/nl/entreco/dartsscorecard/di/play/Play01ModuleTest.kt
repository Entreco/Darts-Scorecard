package nl.entreco.dartsscorecard.di.play

import android.content.Context
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 02/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Play01ModuleTest {

    @Mock private lateinit var mockContext: Context

    @Test
    fun `it should not be null`() {
        assertNotNull(Play01Module())
    }


    @Test(expected = NullPointerException::class)
    fun `it should provide AlertDialogBuilder`() {
        Play01Module().provideAlertDialogBuilder(mockContext)
    }
}
