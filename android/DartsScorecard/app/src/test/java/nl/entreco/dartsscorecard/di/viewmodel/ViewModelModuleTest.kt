package nl.entreco.dartsscorecard.di.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import com.nhaarman.mockito_kotlin.whenever
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

    @Mock private lateinit var mockSharedPrefs: SharedPreferences
    @Mock private lateinit var mockLifeCycle: Lifecycle
    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockActivity: FragmentActivity
    private lateinit var subject: ViewModelModule

    @Before
    fun setUp() {
        whenever(mockActivity.getSharedPreferences("audio", Context.MODE_PRIVATE)).thenReturn(mockSharedPrefs)
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
    fun `it should provide audioPrefs`() {
        assertNotNull(subject.provideAudioPreferences())
    }

}