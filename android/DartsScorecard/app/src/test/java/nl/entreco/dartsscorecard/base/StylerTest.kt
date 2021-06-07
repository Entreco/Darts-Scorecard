package nl.entreco.dartsscorecard.base

import android.app.Activity
import android.content.SharedPreferences
import androidx.annotation.StyleRes
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.dartsscorecard.R
import org.junit.Assert.assertEquals
import org.junit.Test
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

    @Mock private lateinit var mockPrefs: SharedPreferences
    @Mock private lateinit var mockEditor: SharedPreferences.Editor
    @Mock private lateinit var mockActivity: Activity
    @InjectMocks private lateinit var subject: Styler

    @Captor private lateinit var styleCaptor: ArgumentCaptor<String>

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
    fun `it should return 'PDC_2018' style`() {
        givenStyle(Styler.Style.PDC_2018.style)
        assertEquals(R.style.Pdc_2018, subject.get())
    }

    @Test
    fun `it should return 'BDO_2018' style`() {
        givenStyle(Styler.Style.BDO_2018.style)
        assertEquals(R.style.Bdo_2018, subject.get())
    }

    @Test
    fun `it should swap to PDC_2018 style`() {
        givenStyle(Styler.Style.BDO.style)
        val actualStyle = whenSwapping()
        assertEquals(R.style.Pdc_2018, actualStyle)
    }

    @Test
    fun `it should swap to BDO_2018 style`() {
        givenStyle(Styler.Style.PDC_2018.style)
        val actualStyle = whenSwapping()
        assertEquals(R.style.Bdo_2018, actualStyle)
    }

    @Test
    fun `it should swap to PCF style`() {
        givenStyle(Styler.Style.BDO_2018.style)
        val actualStyle = whenSwapping()
        assertEquals(R.style.Pcf, actualStyle)
    }

    @Test
    fun `it should swap to PDC style`() {
        givenStyle(Styler.Style.PCF.style)
        val actualStyle = whenSwapping()
        assertEquals(R.style.Pdc, actualStyle)
    }

    @Test
    fun `it should swap to BDO style`() {
        givenStyle(Styler.Style.PDC.style)
        val actualStyle = whenSwapping()
        assertEquals(R.style.Bdo, actualStyle)
    }

    private fun givenStyle(style: String) {
        whenever(mockPrefs.getString("currentStyle", Styler.Style.PDC_2018.style)).thenReturn(style)
    }

    private fun whenSwapping(): Int {
        whenever(mockPrefs.edit()).thenReturn(mockEditor)
        whenever(mockEditor.putString(eq("currentStyle"), any())).thenReturn(mockEditor)

        subject.switch()
        verify(mockEditor).putString(eq("currentStyle"), styleCaptor.capture())
        givenStyle(styleCaptor.value)

        verify(mockActivity).recreate()
        return subject.get()
    }

}