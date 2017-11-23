package nl.entreco.dartsscorecard

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import java.text.NumberFormat


/**
 * Created by Entreco on 20/11/2017.
 */
abstract class BaseCounterTextView : AppCompatTextView {
    val STEP_10 = 10
    val speed : Long = 10
    private var mTarget: Long = 0
    private var mCurrent: Long = 0
    @Volatile private var stopped = false

    private val numberFormat : NumberFormat by lazy { NumberFormat.getInstance() }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun tick() {
        if (mCurrent != mTarget) {
            proceedTowardsTarget(mCurrent < mTarget)
        } else {
            stopTick()
        }
        display(mCurrent)
    }

    fun stopTick(){
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
        var step = STEP_10
        while (!stepFound) {
            if (diff < step) {
                stepFound = true

                if (step == STEP_10) {
                    if (increase) {
                        mCurrent++
                    } else {
                        mCurrent--
                    }
                } else {
                    val oneStep = Math.round(Math.random() * (step / 10 / 2) + step / 10 / 2)
                    if (increase) {
                        mCurrent += oneStep
                    } else {
                        mCurrent -= oneStep
                    }
                }

            } else {
                step *= STEP_10
            }
        }

        if(!stopped) {
            postDelayed({ tick() }, speed)
        }
    }

    private fun display(current: Long) {
        text = numberFormat.format(current)
    }

    fun setTarget(target: Long) {
        if (mTarget != target) {
            mTarget = target
        }
        stopped = false
        tick()
    }
}