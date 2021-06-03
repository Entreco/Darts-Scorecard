package nl.entreco.dartsscorecard.profile.edit

import android.app.Activity
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 03/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class EditPlayerNameNavigatorTest {

    @Mock private lateinit var mockActivity: EditPlayerNameActivity
    @InjectMocks private lateinit var subject: EditPlayerNameNavigator

    @Test
    fun onCancel() {
        subject.onCancel()
        verify(mockActivity).onBackPressed()
    }

    @Test
    fun onDoneEditing() {
        subject.onDoneEditing("name", 0)
        verify(mockActivity).setResult(eq(Activity.RESULT_OK), any())
        verify(mockActivity).finishAfterTransition()
    }

}
