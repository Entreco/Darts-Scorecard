package nl.entreco.dartsscorecard.profile.select

import android.app.Activity.RESULT_OK
import android.app.onActivityResult
import android.content.Context
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 04/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class SelectProfileActivityTest {
    @Mock private lateinit var mockContext: Context

    val subject = spy(SelectProfileActivity())

    @Test
    fun `should start SelectProfileActivity`() {
        SelectProfileActivity.launch(mockContext)
        verify(mockContext).startActivity(any())
    }

    @Test
    fun `should handle activity result`() {
        subject.onActivityResult(0, RESULT_OK, null)
    }
}
