package nl.entreco.dartsscorecard.play.live

import android.view.View
import android.view.ViewGroup
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.dartsscorecard.play.Play01Navigator
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 24/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class LiveStatAdapterTest {

    @Mock private lateinit var mockView: View
    @Mock private lateinit var mockContainer: ViewGroup
    @Mock private lateinit var mockNavigator: Play01Navigator
    private lateinit var subject: LiveStatAdapter

    @Before
    fun setUp() {
        givenSubject()
    }

    @Test(expected = NullPointerException::class) // Unable to mock DatabindingUtil
    fun instantiateItem() {
        givenItems(0)
        whenInstantiating(0)
    }

    @Test
    fun destroyItem() {
        subject.destroyItem(mockContainer, 0, mockView)
        verify(mockContainer).removeView(mockView)
    }

    @Test
    fun `it should return true, if view is passed as obj`() {
        assertTrue( subject.isViewFromObject(mockView, mockView))
    }

    @Test
    fun `it should return false, for most common cases`() {
        assertFalse( subject.isViewFromObject(mockView, Any()))
    }

    @Test
    fun `count should be zero initially`() {
        assertEquals(0, subject.count)
    }

    @Test
    fun populate() {
        subject.populate(emptyMap())
    }

    private fun givenSubject() {
        subject = LiveStatAdapter(mockNavigator)
    }

    private fun givenItems(vararg items: Int) {
        subject.populate(items.map { Pair(it, TeamLiveStatModel(mock())) }.toMap())
    }

    private fun whenInstantiating(position: Int) {
        subject.instantiateItem(mockContainer, position)
    }

}