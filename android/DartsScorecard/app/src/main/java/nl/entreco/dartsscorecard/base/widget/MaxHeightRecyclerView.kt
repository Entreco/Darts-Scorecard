package nl.entreco.dartsscorecard.base.widget

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet

/**
 * Created by entreco on 14/01/2018.
 */
class MaxHeightRecyclerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.recyclerview.widget.RecyclerView(context, attrs, defStyleAttr) {

    var maxHeight: Int = 400

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val height = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
        super.onMeasure(widthSpec, height)
    }
}