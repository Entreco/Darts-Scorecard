package nl.entreco.dartsscorecard.play.input

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import nl.entreco.dartsscorecard.R


/**
 * Created by Entreco on 06/12/2017.
 */
class InputWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val hintView by lazy { this.findViewById<TextView>(R.id.hint) }
    private val numView by lazy { this.findViewById<TextView>(R.id.num) }
    private val accentView by lazy { this.findViewById<TextView>(R.id.accent) }

    var hint: String
        get() {
            return hintView.text.toString()
        }
        set(value) {
            hintView.text = value
        }

    init {
        // We use <merge/> as root tag, so no xml attributes from root layout are present
        LayoutInflater.from(context).inflate(R.layout.input_widget, this, true)

        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.InputWidget,
                0, 0)

        try {
            val num = a.getString(R.styleable.InputWidget_num)
            val hint = a.getString(R.styleable.InputWidget_hint)
            val accent = a.getBoolean(R.styleable.InputWidget_accent, false)
            setupAccent(num, hint, accent)

        } finally {
            a.recycle()
        }
    }

    private fun setupAccent(num: String?, hint: String?, accent: Boolean) {
        numView.visibility = if (accent) View.INVISIBLE else View.VISIBLE
        hintView.visibility = if (accent) View.INVISIBLE else View.VISIBLE
        accentView.visibility = if (accent) View.VISIBLE else View.INVISIBLE
        accentView.text = if (accent) hint else ""
        hintView.text = if (accent) "" else hint
        numView.text = num
    }
}