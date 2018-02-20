package nl.entreco.data.billing

import android.content.ComponentName
import android.os.IBinder
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 20/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class BillingServiceConnectionTest {

    @Mock private lateinit var mockComponentName : ComponentName
    @Mock private lateinit var mockService : IBinder
    @Mock private lateinit var mockCallback: (Boolean) -> Unit
    private val subject = BillingServiceConnection()

    @Before
    fun setUp() {
        subject.setCallback(mockCallback)
    }

    @Test
    fun onServiceDisconnected() {
        whenDisconnected()
        verify(mockCallback).invoke(false)
    }

    @Test
    fun onServiceConnected() {
        whenConnected()
        verify(mockCallback).invoke(true)
    }

    @Test
    fun `it should get service`() {
        whenConnected()
        assertNotNull(subject.getService())
    }

    @Test
    fun `it should clear service`() {
        whenDisconnected()
        assertNull(subject.getService())
    }


    private fun whenConnected() {
        subject.onServiceConnected(mockComponentName, mockService)
    }

    private fun whenDisconnected() {
        subject.onServiceDisconnected(mockComponentName)
    }

}
