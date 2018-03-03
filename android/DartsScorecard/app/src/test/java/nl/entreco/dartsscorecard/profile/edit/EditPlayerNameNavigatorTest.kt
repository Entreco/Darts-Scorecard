package nl.entreco.dartsscorecard.profile.edit

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
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
        subject.onDoneEditing("name", "double")
        verify(mockActivity).setResult(any())
        verify(mockActivity).finishAfterTransition()
    }

}
