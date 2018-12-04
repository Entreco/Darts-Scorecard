package nl.entreco.dartsscorecard.base

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
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
    @Mock private lateinit var mockRecyclerView: androidx.recyclerview.widget.RecyclerView
    @Mock private lateinit var mockViewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
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

        override fun onMove(p0: androidx.recyclerview.widget.RecyclerView, p1: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                            p2: androidx.recyclerview.widget.RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(p0: androidx.recyclerview.widget.RecyclerView.ViewHolder, p1: Int) {
            swiped.set(true)
        }

        override fun onChildDraw(canvas: Canvas, recyclerView: androidx.recyclerview.widget.RecyclerView, viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            drawed.set(true)
        }
    }
}