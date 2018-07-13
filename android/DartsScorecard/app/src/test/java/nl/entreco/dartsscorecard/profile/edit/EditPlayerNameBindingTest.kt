package nl.entreco.dartsscorecard.profile.edit

import android.content.Context
import android.os.IBinder
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 02/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class EditPlayerNameBindingTest {

    @Mock private lateinit var mockToken: IBinder
    @Mock private lateinit var mockWindow: Window
    @Mock private lateinit var mockInputMethodManager: InputMethodManager
    @Mock private lateinit var mockView: EditText
    @Mock private lateinit var mockContext: Context

    @Before
    fun setUp() {
        whenever(mockContext.getSystemService(Context.INPUT_METHOD_SERVICE)).thenReturn(mockInputMethodManager)
        whenever(mockView.context).thenReturn(mockContext)
    }

    @Test
    fun `it should show Keyboard`() {
        EditPlayerNameBinding.showKeyboard(mockView, true, mockWindow)
        verify(mockInputMethodManager).showSoftInput(mockView, InputMethodManager.SHOW_IMPLICIT)

    }

    @Test
    fun `it should hide Keyboard`() {
        whenever(mockView.windowToken).thenReturn(mockToken)
        EditPlayerNameBinding.showKeyboard(mockView, false, mockWindow)
        verify(mockInputMethodManager).hideSoftInputFromWindow(mockToken, 0)
    }
}
