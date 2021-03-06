package nl.entreco.dartsscorecard.di.setup

import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.setup.Setup01Activity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 02/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Setup01ModuleTest {

    @Mock private lateinit var mockPrefs: SharedPreferences
    @Mock private lateinit var mockActivity: Setup01Activity
    private lateinit var subject: Setup01Module

    @Before
    fun setUp() {
        whenever(mockActivity.getSharedPreferences(any(), any())).thenReturn(mockPrefs)
        subject = Setup01Module(mockActivity)
    }

    @Test
    fun providePreferenceRepo() {
        assertNotNull(givenPreferenceRepo())
    }

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

    private fun givenPreferenceRepo() = subject.providePreferenceRepo()
    private fun givenNavigator() = subject.provideNavigator()
    private fun givenEditor() = subject.providePlayerEditor()
}