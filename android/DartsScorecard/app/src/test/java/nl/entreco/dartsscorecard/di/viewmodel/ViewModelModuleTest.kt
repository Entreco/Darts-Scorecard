package nl.entreco.dartsscorecard.di.viewmodel

import androidx.lifecycle.Lifecycle
import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.data.billing.BillingServiceConnection
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 17/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class ViewModelModuleTest {

    @Mock private lateinit var mockLifeCycle: Lifecycle
    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockBillingConnection: BillingServiceConnection
    @Mock private lateinit var mockActivity: androidx.fragment.app.FragmentActivity
    private lateinit var subject: ViewModelModule

    @Before
    fun setUp() {
        subject = ViewModelModule(mockActivity)
    }

    @Test
    fun context() {
        assertNotNull(subject.context())
        assertEquals(mockActivity, subject.context())
    }


    @Test(expected = NullPointerException::class)
    fun `it should provide AlertDialogBuilder`() {
        subject.provideAlertDialogBuilder(mockContext)
    }

    @Test
    fun `it should provide lifecycle`() {
        whenever(mockActivity.lifecycle).thenReturn(mockLifeCycle)
        assertNotNull(subject.lifeCycle())
    }

    @Test
    fun `it should provide service Connection`() {
        assertNotNull(subject.provideServiceConnection())
    }

    @Test
    fun `it should provide BillingRepository`() {
        assertNotNull(subject.provideBillingRepository(mockContext, mockBillingConnection))
    }

}