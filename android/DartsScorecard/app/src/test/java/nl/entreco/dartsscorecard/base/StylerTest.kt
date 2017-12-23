package nl.entreco.dartsscorecard.base

import android.app.Activity
import android.content.SharedPreferences
import android.support.annotation.StyleRes
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.R
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 10/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class StylerTest {

    @Mock private lateinit var mockPrefs : SharedPreferences
    @Mock private lateinit var mockEditor : SharedPreferences.Editor
    @Mock private lateinit var mockActivity : Activity
    @InjectMocks private lateinit var subject : Styler

    @Captor private lateinit var styleCaptor : ArgumentCaptor<Int>

    @Test
    fun `it should return 'PDC' style`() {
        givenStyle(Styler.Style.PDC.style)
        assertEquals(R.style.Pdc, subject.get())
    }

    @Test
    fun `it should return 'BDO' style`() {
        givenStyle(Styler.Style.BDO.style)
        assertEquals(R.style.Bdo, subject.get())
    }

    @Test
    fun `it should swap styles`() {
        givenStyle(Styler.Style.PDC.style)
        val actualStyle = whenSwapping()
        assertEquals(R.style.Bdo, actualStyle)
    }

    private fun givenStyle(@StyleRes style: Int) {
        whenever(mockPrefs.getInt("curStyle", Styler.Style.PDC.style)).thenReturn(style)
    }

    private fun whenSwapping(): Int {
        whenever(mockPrefs.edit()).thenReturn(mockEditor)
        whenever(mockEditor.putInt(eq("curStyle"), any())).thenReturn(mockEditor)

        subject.switch()
        verify(mockEditor).putInt(eq("curStyle"), styleCaptor.capture())
        givenStyle(styleCaptor.value)

        verify(mockActivity).recreate()
        return subject.get()
    }

}