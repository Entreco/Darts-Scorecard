package nl.entreco.dartsscorecard.base.widget

import android.content.Context
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 16/12/2017.
 */
class CounterTextViewTest {

    @Mock private lateinit var mockContext : Context

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test(expected = NullPointerException::class)
    fun `it can be instantiated`() {
        assertNotNull(CounterTextView(mockContext))
    }
}