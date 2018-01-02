package nl.entreco.dartsscorecard.setup.edit

import android.app.Activity
import android.content.Intent
import android.view.View
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
    @Mock private lateinit var mockView: View
    @Mock private lateinit var mockPlayer: Player

    private lateinit var subject: EditPlayerNavigator
    private val givenSuggestedName: String = "player 33"
    private val givenName: String = "player 12"
    private val givenId: Long = 66

    @Before
    fun setUp() {
        whenever(mockActivity.intent).thenReturn(mockIntent)
        whenever(mockIntent.getStringExtra(any())).thenReturn(givenSuggestedName)
        whenever(mockPlayer.name).thenReturn(givenName)
        whenever(mockPlayer.id).thenReturn(givenId)
        subject = EditPlayerNavigator(mockActivity)
    }

    @Test
    fun `it should set Result onSelected`() {
        subject.onSelected(mockView, mockPlayer)
        verify(mockActivity).setResult(eq(Activity.RESULT_OK), any())
    }

    @Test
    fun `it should finish activity onSelected`() {
        subject.onSelected(mockView, mockPlayer)
        verify(mockActivity).finish()
    }

}