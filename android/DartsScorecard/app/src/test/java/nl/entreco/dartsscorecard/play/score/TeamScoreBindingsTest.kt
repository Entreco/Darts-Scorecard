package nl.entreco.dartsscorecard.play.score

import android.content.Context
import android.content.res.Resources
import android.view.ViewPropertyAnimator
import android.widget.ImageView
import android.widget.TextView
import org.mockito.kotlin.*
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.widget.CounterTextView
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.AdditionalMatchers.*
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 28/11/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class TeamScoreBindingsTest {

    @Mock private lateinit var mockCounterTextView: CounterTextView
    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockTheme: Resources.Theme
    @Mock private lateinit var mockTextView: TextView
    @Mock private lateinit var mockImageView: ImageView
    @Mock private lateinit var mockAnimator: ViewPropertyAnimator
    private val runnable = argumentCaptor<Runnable>()

    @Test
    fun `it should setTarget() on CounterTextView (ScorePts TextView)`() {
        TeamScoreBindings.showScore(mockCounterTextView, 50)
        verify(mockCounterTextView).setTarget(50)
    }

    @Test
    fun `it should set 'starter' drawable when player is starting player`() {
        givenPlayerStartedLeg()
        thenStarterDrawableIsShown()
    }

    @Test
    fun `it should clear 'starter' drawable when player is !starting player`() {
        givenPlayerWasNotStarting()
        thenStarterDrawableIsNotShown()
    }

    @Test(expected = NullPointerException::class)
    fun `it should handle 9-darter animation`() {
        givenNineDartEvent(true)
    }

    @Test(expected = NullPointerException::class)
    fun `it should clear 9-darter animation`() {
        givenNineDartEvent(false)
    }

    @Test
    fun `it should show specials when scoring 180`() {
        givenSpecialScore(180, 0)
        then180AnimationIsShown()
    }

    @Test
    fun `it should NOT show specials when scoring 0`() {
        givenSpecialScore(0, 0)
        thenNoAnimationIsShown()
    }

    @Test
    fun `it should clear specials when scoring all others`() {
        givenSpecialScore(1, 0)
        thenClearAnimationIsShown()
    }

    @Test
    fun `it should show current score animation gt 0`() {
        givenCurrentScore(100)
        thenShowCurrentAnimationIsDone()
    }

    @Test
    fun `it should hide current score animation lt 0`() {
        givenCurrentScore(0)
        thenHideCurrentAnimationIsDone()
    }

    @Test
    fun `it should show current team animation`() {
        givenCurrentTeam(true)
        thenCurrentTeamAnimationIsShown()
    }

    @Test
    fun `it should clear current team animation`() {
        givenCurrentTeam(false)
        thenCurrentTeamAnimationIsHidden()
    }

    @Test(expected = NullPointerException::class) // Unable to mock android.graphics.Color
    fun `it should show finish animation`() {
        givenFinish("T20 D10")
        thenFinishAnimationIsShown()
    }

    @Test(expected = NullPointerException::class)
    fun `it should hide finish animation`() {
        givenFinish("")
        thenFinishAnimationIsNotShown()
    }

    private fun givenSpecialScore(oldScore: Int, score: Int) {
        whenever(mockTextView.animate()).thenReturn(mockAnimator)
        mockAnimations()
        TeamScoreBindings.showSpecials(mockTextView, oldScore, score)
    }

    private fun givenNineDartEvent(nineDarts: Boolean) {
        whenever(mockTextView.width).thenReturn(200)
        whenever(mockTextView.measuredWidth).thenReturn(200)
        mockAnimations()
        TeamScoreBindings.showNineDarter(mockTextView, nineDarts)
    }

    private fun givenCurrentScore(score: Int) {
        whenever(mockCounterTextView.animate()).thenReturn(mockAnimator)
        whenever(mockCounterTextView.context).thenReturn(mockContext)
        whenever(mockContext.theme).thenReturn(mockTheme)
        mockAnimations()
        TeamScoreBindings.showCurrentScore(mockCounterTextView, score)
        verify(mockCounterTextView).setTarget(score.toLong())
    }

    private fun givenCurrentTeam(current: Boolean) {
        whenever(mockImageView.animate()).thenReturn(mockAnimator)
        whenever(mockImageView.width).thenReturn(200)
        mockAnimations()
        TeamScoreBindings.showCurrentTeam(mockImageView, current)
    }

    private fun givenFinish(finish: String) {
        whenever(mockTextView.width).thenReturn(200)
        mockAnimations()

        TeamScoreBindings.showFinishWithAlpha(mockTextView, finish, true)
    }

    private fun givenPlayerWasNotStarting() {
        TeamScoreBindings.showStartingPlayer(mockTextView, false)
    }

    private fun givenPlayerStartedLeg() {
        TeamScoreBindings.showStartingPlayer(mockTextView, true)
    }

    private fun thenStarterDrawableIsShown() {
        verify(mockTextView).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.starter, 0)
    }

    private fun thenStarterDrawableIsNotShown() {
        verify(mockTextView).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    }

    private fun then180AnimationIsShown() {
        verify(mockTextView).setText(R.string.score_180)
        verify(mockTextView, atLeastOnce()).animate()
        verify(mockAnimator).withStartAction(runnable.capture())
        verify(mockAnimator).withEndAction(runnable.capture())
        runnable.allValues.forEach { it.run() }
    }

    private fun thenNoAnimationIsShown() {
        verify(mockTextView, never()).setText(R.string.score_180)
        verify(mockTextView, never()).animate()
    }

    private fun thenClearAnimationIsShown() {
        verify(mockTextView, never()).setText(R.string.score_180)
        verify(mockTextView, times(1)).animate()
        verify(mockAnimator).withStartAction(runnable.capture())
        verify(mockAnimator).withEndAction(runnable.capture())
        runnable.allValues.forEach { it.run() }
    }

    private fun thenShowCurrentAnimationIsDone() {
        verify(mockCounterTextView).animate()
        verify(mockAnimator).start()
        verify(mockCounterTextView).setTextColor(anyInt())
    }

    private fun thenHideCurrentAnimationIsDone() {
        verify(mockCounterTextView, never()).setTextColor(anyInt())
    }

    private fun thenCurrentTeamAnimationIsShown() {
        verify(mockImageView).animate()
        verify(mockAnimator).translationX(0f)
    }

    private fun thenCurrentTeamAnimationIsHidden() {
        verify(mockImageView).animate()
        verify(mockAnimator).translationX(gt(10F))
    }

    private fun thenFinishAnimationIsShown() {
        verify(mockTextView).text = not(eq(""))
        verify(mockAnimator).translationX(eq(0F))
    }

    private fun thenFinishAnimationIsNotShown() {
        verify(mockTextView).text = eq("")
        verify(mockAnimator).translationX(lt(0F))
    }

    private fun mockAnimations() {
        whenever(mockAnimator.translationX(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.scaleY(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.setInterpolator(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.setStartDelay(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.setDuration(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.withStartAction(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.withEndAction(any())).thenReturn(mockAnimator)
    }

}
