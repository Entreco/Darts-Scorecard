package nl.entreco.dartsscorecard.di.viewmodel

import android.app.Activity
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.App
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 17/12/2017.
 */
class ViewModelModuleTest {

    @Mock private lateinit var mockApp: App
    @Mock private lateinit var mockActivity: Activity
    private lateinit var subject: ViewModelModule

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        whenever(mockApp.applicationContext).thenReturn(mockApp)
        subject = ViewModelModule(mockActivity)
    }

    @Test
    fun context() {
        assertNotNull(subject.application(mockApp))
    }

    @Test
    fun application() {
        assertNotNull(subject.context())
        assertEquals(mockActivity, subject.context())
    }

}