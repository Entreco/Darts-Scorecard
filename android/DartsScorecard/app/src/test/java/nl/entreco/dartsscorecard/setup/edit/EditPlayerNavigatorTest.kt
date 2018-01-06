package nl.entreco.dartsscorecard.setup.edit

import android.app.Activity
import android.content.Intent
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.model.players.Player
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 02/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class EditPlayerNavigatorTest {

    @Mock private lateinit var mockActivity: EditPlayerActivity
    @Mock private lateinit var mockIntent: Intent
    @Mock private lateinit var mockPlayer: Player

    private lateinit var subject: EditPlayerNavigator

    @Before
    fun setUp() {
        whenever(mockActivity.intent).thenReturn(mockIntent)
        subject = EditPlayerNavigator(mockActivity)
    }

    @Test
    fun `it should set RESULT_OK onSelected`() {
        subject.onSelected(mockPlayer)
        verify(mockActivity).setResult(eq(Activity.RESULT_OK), any())
    }

    @Test
    fun `it should finish activity onSelected`() {
        subject.onSelected(mockPlayer)
        verify(mockActivity).finish()
    }


    @Test
    fun `it should set RESULT_CANCELLED onBackPressed`() {
        subject.onBackPressed()
        verify(mockActivity).setResult(eq(Activity.RESULT_OK), any())
    }

    @Test
    fun `it should finish activity onBackPressed`() {
        subject.onBackPressed()
        verify(mockActivity).finish()
    }

}