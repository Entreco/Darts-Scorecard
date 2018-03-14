package nl.entreco.dartsscorecard.profile.edit

import com.nhaarman.mockito_kotlin.spy
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 04/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class EditPlayerNameActivityTest {

    @Mock private lateinit var mockActivity: EditPlayerNameActivity

    val subject = spy(EditPlayerNameActivity())

    @Test
    fun `should start EditPlayerNameActivity`() {
        assertNotNull(EditPlayerNameActivity.launch(mockActivity, "pietje", "20"))
    }

}
