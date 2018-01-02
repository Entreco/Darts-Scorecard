package nl.entreco.dartsscorecard.di.setup

import android.app.Activity
import nl.entreco.dartsscorecard.setup.Setup01Activity
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 02/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Setup01ModuleTest {

    @Mock private lateinit var mockActivity : Setup01Activity

    @Test
    fun provideNavigator() {
        assertNotNull(Setup01Module(mockActivity).provideNavigator())
    }

}