package nl.entreco.dartsscorecard.di.setup

import nl.entreco.dartsscorecard.setup.Setup01Activity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 02/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Setup01ModuleTest {

    @Mock private lateinit var mockActivity: Setup01Activity

    @Test
    fun provideNavigator() {
        assertNotNull(givenNavigator())
    }

    @Test
    fun providePlayerEditor() {
        assertNotNull(givenEditor())
    }

    @Test
    fun `navigator should same instance as playerEditor`() {
        assertEquals(givenEditor(), givenNavigator())
    }

    private fun givenNavigator() = Setup01Module(mockActivity).provideNavigator()

    private fun givenEditor() = Setup01Module(mockActivity).providePlayerEditor()
}