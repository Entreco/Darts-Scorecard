package nl.entreco.dartsscorecard.base.widget

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import kotlin.math.round


/**
 * Created by Entreco on 20/11/2017.
 */
abstract class BaseCounterTextView : AppCompatTextView {
    private val step10 = 10
    private val speed: Long = 10
    private var mTarget: Long = 0
    private var mCurrent: Long = 0
    @Volatile private var stopped = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun tick() {
        if (mCurrent != mTarget) {
            proceedTowardsTarget(mCurrent < mTarget)
        } else {
            stopTick()
        }
        display(mCurrent)
    }

    private fun stopTick() {
        stopped = true
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

        if (!stopped) {
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
        stopped = false
        tick()
    }
}