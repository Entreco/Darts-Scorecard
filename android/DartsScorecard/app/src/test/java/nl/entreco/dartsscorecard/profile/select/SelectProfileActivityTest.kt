package nl.entreco.dartsscorecard.profile.select

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
}
