package nl.entreco.dartsscorecard.base

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by entreco on 17/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class SwipeToDeleteCallbackTest {

    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockCanvas: Canvas
    @Mock private lateinit var mockRecyclerView: RecyclerView
    @Mock private lateinit var mockViewHolder: RecyclerView.ViewHolder
    private lateinit var subject : TestSwipeToDeleteCallback

    @Test(expected = NullPointerException::class)
    fun `it should swipe`() {
        givenSubject()
        whenSwiping()
        thenSwipeIsCalled()
    }

    @Test(expected = NullPointerException::class)
    fun `it should draw`() {
        givenSubject()
        whenDrawing()
        thenDrawIsCalled()
    }

    private fun givenSubject() {
        subject = TestSwipeToDeleteCallback(mockContext)
    }

    private fun whenSwiping() {
        subject.onSwiped(mockViewHolder, 1)
    }

    private fun whenDrawing() {
        subject.onChildDraw(mockCanvas, mockRecyclerView, mockViewHolder, 12F, 12F, 1, true)
    }

    private fun thenSwipeIsCalled() {
        assertTrue(subject.swiped.get())
    }

    private fun thenDrawIsCalled() {
        assertTrue(subject.drawed.get())
    }

    class TestSwipeToDeleteCallback(context: Context) : SwipeToDeleteCallback(context){
        val swiped = AtomicBoolean(false)
        val drawed = AtomicBoolean(false)
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
            swiped.set(true)
        }

        override fun onChildDraw(canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            drawed.set(true)
        }
    }
}