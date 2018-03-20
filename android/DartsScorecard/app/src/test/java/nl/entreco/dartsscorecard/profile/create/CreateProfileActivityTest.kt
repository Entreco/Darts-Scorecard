package nl.entreco.dartsscorecard.profile.create

import android.app.Activity
import android.view.View
import com.nhaarman.mockito_kotlin.*
import nl.entreco.dartsscorecard.profile.select.SelectProfileActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 20/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class CreateProfileActivityTest {
    @Mock private lateinit var mockContext: Activity
    @Mock private lateinit var mockView: View

    val subject = spy(CreateProfileActivity())

    @Test
    fun `should start CreateProfileActivity`() {
        CreateProfileActivity.launch(mockContext, mockView)
        verify(mockContext).startActivityForResult(any(), eq(SelectProfileActivity.REQUEST_CODE_CREATE), isNull())
    }
}