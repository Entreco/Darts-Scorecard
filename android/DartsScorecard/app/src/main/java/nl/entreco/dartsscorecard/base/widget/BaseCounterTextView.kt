package nl.entreco.dartsscorecard.base.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.round


/**
 * Created by Entreco on 20/11/2017.
 */
abstract class BaseCounterTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val step10 = 10
    private val speed: Long = 10
    private var mTarget: Long = 0
    private var mCurrent: Long = 0
    private val stopped = AtomicBoolean(false)

    private fun tick() {
        if (mCurrent != mTarget) {
            proceedTowardsTarget(mCurrent < mTarget)
        } else {
            stopTick()
        }
        display(mCurrent)
    }

    private fun stopTick() {
        stopped.set(true)
        display(mTarget)
    }

    private fun proceedTowardsTarget(increase: Boolean) {
        val diff = if (mTarget > mCurrent) {
            mTarget - mCurrent
        } else {
            mCurrent - mTarget
        }
        var stepFound = false
        var step = step10
        while (!stepFound) {
            if (diff < step) {
                stepFound = true

                if (step == step10) {
                    if (increase) {
                        mCurrent++
                    } else {
                        mCurrent--
                    }
                } else {
                    val oneStep = round(Math.random() * (step / 10 / 2) + step / 10 / 2).toLong()
                    if (increase) {
                        mCurrent += oneStep
                    } else {
                        mCurrent -= oneStep
                    }
                }

            } else {
                step *= step10
            }
        }

        if (!stopped.get()) {
            postDelayed({ tick() }, speed)
        }
    }

    private fun display(current: Long) {
        text = current.toString()
    }

    fun setTarget(target: Long) {
        if (mTarget != target) {
            mTarget = target
        }
        stopped.set(false)
        tick()
    }
}