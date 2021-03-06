package nl.entreco.dartsscorecard.base.widget

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

/**
 * Created by entreco on 24/03/2018.
 */
class WrapContentViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val hms = getChildAt(0)?.let { child ->
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            MeasureSpec.makeMeasureSpec(child.measuredHeight, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, hms ?: heightMeasureSpec)
    }
}