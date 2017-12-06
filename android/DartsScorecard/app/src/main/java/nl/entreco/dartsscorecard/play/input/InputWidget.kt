package nl.entreco.dartsscorecard.play.input

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.TextView
import nl.entreco.dartsscorecard.R


/**
 * Created by Entreco on 06/12/2017.
 */
class InputWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        // We use <merge/> as root tag, so no xml attributes from root layout are present
        val view = LayoutInflater.from(context).inflate(R.layout.input_widget, this, true)

        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.InputWidget,
                0, 0)

        try {
            view.findViewById<TextView>(R.id.num).text = a.getString(R.styleable.InputWidget_num)
            view.findViewById<TextView>(R.id.hint).text = a.getString(R.styleable.InputWidget_hint)
        } finally {
            a.recycle()
        }
    }
}